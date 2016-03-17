package duson.java.utils.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

/**
	Iterator<String> itr = request.getFileNames();
	MultipartFile mpf = null;
		
	while (itr.hasNext()) {
		mpf = request.getFile(itr.next());
		
		try(SocketFTP ftp = new SocketFTP()){
			ftp.connect(serverIP, serverPort, userName, pwd);
			ftp.bin();
	
			long offset = -1; // -1时，Ftp读取文件大小，再续传
			if(request.getParameter("chunk") != null) {
				long chunk = Long.parseLong(request.getParameter("chunk"));
				//long chunks = Long.parseLong(request.getParameter("chunks"));
				offset = chunk * mpf.getSize();
			}
	
			ftp.stor(mpf.getInputStream(), serverPath, offset);
		} catch (IOException e) { }
	}
 */
public class SocketFTP implements AutoCloseable {
	private static boolean DEBUG = false;
	
	private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;

	public File connect(String host, int port, String user, String pass) throws IOException
    {
        File myFile=null;
        if (socket != null)
        {
            throw new IOException("SimpleFTP is already connected. Disconnect first.");
        }
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String response = readLine();
        while(!response.startsWith("220 ")) {
        	response = readLine();
        }
        /*if (!response.startsWith("220 "))
        {
            throw new IOException("SimpleFTP received an unknown response when connecting to the FTP server: " + response);
        }*/

        sendLine("USER " + user);
        response = readLine();

        if (!response.startsWith("331 ")) {
            throw new IOException("SimpleFTP received an unknown response after sending the user: " + response);
        }
        sendLine("PASS " + pass);

        response = readLine();
        if (!response.startsWith("230 ")) {
            throw new IOException("SimpleFTP was unable to log in with the supplied password: " + response);
        }

        return myFile;
        // Now logged in.
    }


    /**
     * Disconnects from the FTP server.
     */
    public  void disconnect() throws IOException
    {
        try
        {
            sendLine("QUIT");
        }
        finally {
            socket = null;
        }
    }

    /**
     * Returns the working directory of the FTP server it is connected to.
     */
    public String pwd() throws IOException
    {
        sendLine("PWD");
        String dir = null;
        String response = readLine();
        if (response.startsWith("257 "))
        {
            System.out.println("PWD "+ response);
            int fsLastIndex = response.lastIndexOf(System.getProperty("file.separator"));
            if (fsLastIndex > 0)
                dir = response.substring(fsLastIndex + 1);
            else
                dir = response.substring(4);
        }
        return dir;
    }

    /**
     * Returns a list of files from a directory specified.
     */
    public String ls() throws IOException
    {
        sendLine("LIST");
        String list = null;
        String response = readLine();
        if (response.startsWith("200 ")) list = response.split(" ")[1];
        System.out.println("Got list from server : " + list);

        // End of Json difference
        return list;
    }

    /**
     * Changes the working directory (like cd). Returns true if successful.
     */
    public boolean cwd (String dir) throws IOException
    {
        sendLine("CWD " + dir);
        String response = readLine();
        return (response.startsWith("250 "));
    }

    public boolean stor(File file) throws IOException
    {
        if (file.isDirectory()) {
            throw new IOException("SimpleFTP cannot upload a directory.");
        }
        String filename = file.getName();
        return stor (new FileInputStream(file), filename, -1);
    }

    /**
     * Sends a file to be stored on the FTP server.
     * Returns true if the file transfer was successful.
     * The file is sent in passive mode to avoid NAT or firewall problems
     * at the client end.
     */
    public  boolean stor(InputStream inputStream, String filename, long offset) throws IOException {
        BufferedInputStream input = new BufferedInputStream(inputStream);
        
        sendLine("PASV");
        String response = readLine();
        if (!response.startsWith("227 ")) {
            throw new IOException("SimpleFTP could not request passive mode: " + response);
        }

        String ip = null;
        int port = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
            }
            catch (Exception e) {
                throw new IOException("SimpleFTP received bad data link information: " + response);
            }
        }
        
        if(offset != 0) {
	        sendLine("SIZE " + filename);
	        response = readLine();
	        if (response.startsWith("213 ")) {
	        	if(offset < 0)
	        		offset = Long.parseLong(response.substring(4));
	        	//input.skip(offset);
	        	sendLine("REST " + offset);
	        	
		        //sendLine("APPE " + filename);
		        response = readLine();
		        if (!response.startsWith("350 ")) {
		            throw new IOException("REST Fail: " + response);
		        }
	        }
        }
        
        sendLine("STOR " + filename);

        Socket dataSocket = new Socket(ip, port);

        response = readLine();
        if (!(response.startsWith("125 ") || response.startsWith("150 "))) {
            throw new IOException("SimpleFTP was not allowed to send the file: " + response);
        }

        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();

        response = readLine();
        return response.startsWith("226 ");
    }

    public Boolean download(File file, String dest) throws IOException {
        sendLine("PASV");
        String response = readLine();
        if (!response.startsWith("227 ")) {
            throw new IOException("SimpleFTP could not request passive mode: " + response);
        }


        String ip = null;
        int port = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
            }
            catch (Exception e) {
                throw new IOException("SimpleFTP received bad data link information: " + response);
            }
        }


        try {
            sendLine("RETR " + file.getAbsolutePath());
            System.out.println (ip+":"+port);
            Socket sock = new Socket(ip, port);
            response = readLine();
            if (!(response.startsWith("125 ") || response.startsWith("150 "))) {
                throw new IOException("SimpleFTP was not allowed to download the file: " + response);
            }

            OutputStream os = new FileOutputStream(new File(
                    dest
                            + System.getProperty("file.separator")
                            + file.getName()));

            BufferedInputStream is = new BufferedInputStream(sock.getInputStream());
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            os.close();
            is.close();
            sock.close();
            System.out.println("RETR " + file.getName() + " : File received");
        } catch (IOException e) {
            try {
                sendLine("error");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        response = readLine();
        return response.startsWith("226 ");
    }

    /**
     * Enter binary mode for sending binary files.
     */
    public boolean bin() throws IOException {
        sendLine("TYPE I");
        String response = readLine();
        return (response.startsWith("201 "));
    }

    /**
     * Enter ASCII mode for sending text files. This is usually the default
     * mode. Make sure you use binary mode if you are sending images or
     * other binary data, as ASCII mode is likely to corrupt them.
     */
    public boolean ascii() throws IOException {
        sendLine("TYPE A");
        String response = readLine();
        return (response.startsWith("202 "));
    }

    /**
     * Sends a raw command to the FTP server.
     */
    private void sendLine(String line) throws IOException {
        if (socket == null) {
            throw new IOException("SimpleFTP is not connected.");
        }
        try {
            writer.write(line + "\r\n");
            writer.flush();
            if (DEBUG) {
                System.out.println("> " + line);
            }
        }
        catch (IOException e) {
            socket = null;
            throw e;
        }
    }

    private String readLine() throws IOException {
        String line = reader.readLine();
        if (DEBUG) {
            System.out.println("< " + line);
        }
        return line;
    }
	
    /* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		disconnect();
	}

}
