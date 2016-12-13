package duson.java.utils.files;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

public class FilesUtils {
	
	public static String getExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i);
		}
		
		return extension;
	}
	
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.##").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static String getFileNameWithExtension(String filePath){
		if(filePath == null) return "";
		
		String fileName = "";

		int i = filePath.replace('\\', '/').lastIndexOf('/');
		if (i > 0) {
			fileName = filePath.substring(i+1);
		}
		
		return fileName;
	}
	
	public static String getFileNameWithoutExtension(String filePath){
		if(filePath == null) return "";
		
		String fileName = "";

		int i = filePath.replace('\\', '/').lastIndexOf('/');
		if (i > 0) {
			fileName = filePath.substring(i+1);
		}
		
		i = fileName.lastIndexOf('.');
		if (i > 0) {
			fileName = fileName.substring(0, i);
		}
		
		return fileName;
	}
	
	public static byte[] toByteArray(String filename) throws IOException{  
        
        File f = new File(filename);  
        if(!f.exists()){  
            throw new FileNotFoundException(filename);  
        }  
  
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());  
        BufferedInputStream in = null;  
        try{  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while(-1 != (len = in.read(buffer,0,buf_size))){  
                bos.write(buffer,0,len);  
            }  
            return bos.toByteArray();  
        }catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        }finally{  
            try{  
                in.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
            bos.close();  
        }  
    } 
	
	public static byte[] toByteArray2(String filename)throws IOException{  
        
        File f = new File(filename);  
        if(!f.exists()){  
            throw new FileNotFoundException(filename);  
        }  
          
        FileChannel channel = null;  
        FileInputStream fs = null;  
        try{  
            fs = new FileInputStream(f);  
            channel = fs.getChannel();  
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)channel.size());  
            while((channel.read(byteBuffer)) > 0){  
            }  
            return byteBuffer.array();  
        }catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        }finally{  
            try{  
                channel.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
            try{  
                fs.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }

    public static String readString(String filePath) {
    	InputStreamReader sr = new InputStreamReader(new FileInputStream("d://1.xml"));
		BufferedReader br = new BufferedReader(sr);
		StringBuilder sb = new StringBuilder();
		String rec = null;
		while ((rec = br.readLine()) != null) {
			sb.append(rec);
		}
		System.out.println(sb.toString());
    }
	
}
