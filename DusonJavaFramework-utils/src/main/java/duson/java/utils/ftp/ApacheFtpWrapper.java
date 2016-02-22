package duson.java.utils.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPCmd;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class ApacheFtpWrapper {
	private final static int connectTimeOut = 5000;
	private static String encoding = System.getProperty("file.encoding");
	
	/*
	 * 上传文件
	 * 
	 * @param input ss
	 */
	public static boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, InputStream input) {
		boolean returnValue = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;

			int portNO = port;
			ftp.setConnectTimeout(connectTimeOut);

			int retryCount = 3; // 重试3次
			while (retryCount > 0) {
				try {
					ftp.connect(url, portNO);
					retryCount = 0;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					retryCount--;
					if (retryCount <= 0)
						throw e;
				}
			}

			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException(reply + " Ftp上传文件失败 - Ftp连接失败 " + username + " : " + password);
			}
			ftp.enterLocalPassiveMode();

			// 目录不存在则创建
			if (!ftp.changeWorkingDirectory(path)) {
				ftp.sendCommand(FTPCmd.MKD, path);
				ftp.changeWorkingDirectory(path);
			}

			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			
			retryCount = 3; // 重试3次
			while (retryCount > 0) {
				try {
					ftp.storeFile(filename, input);
					retryCount = 0;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					retryCount--;
					if (retryCount <= 0)
						throw e;
				}
			}
			// input.close();

			ftp.logout();
			returnValue = true;
		} catch (IOException e) {
			throw new RuntimeException("Ftp上传文件失败 :" + e, e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("uploadFile disconnect", ioe);
				}
			}
		}
		return returnValue;
	}

	/**
	 * 下载文件
	 */
	public static boolean downloadFile(String url, int port, String username,
			String password, String path, String fileName, String localPath) {
		boolean returnValue = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			int portNO = port;
			ftp.setConnectTimeout(connectTimeOut);
			ftp.connect(url, portNO);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException("FTP下载文件错误 - Ftp连接失败");
			}
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(new String(path.getBytes(encoding)));
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			ftp.logout();
			returnValue = true;
		} catch (IOException e) {
			throw new RuntimeException("FTP下载文件错误 :" + e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("downloadFile disconnect", ioe);
				}
			}
		}
		return returnValue;
	}

	/**
	 * 读取文件内容
	 */
	public static String readerFtpFile(String url, int port, String username,
			String password, String path, String fileName) {
		String result = "";
		InputStream ins = null;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			int portNO = port;
			ftp.setConnectTimeout(connectTimeOut);
			ftp.connect(url, portNO);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException("FTP 读取文件内容错误 - Ftp连接失败");
			}
			ftp.enterLocalPassiveMode();
			if (!ftp.changeWorkingDirectory(path)) {
				ftp.sendCommand(FTPCmd.MKD, path);
				ftp.changeWorkingDirectory(path);
			}
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			int retryCount = 3; // 重试3次
			while (retryCount > 0) {
				try {
					ins = ftp.retrieveFileStream(fileName);
					retryCount = 0;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					retryCount--;
					if (retryCount <= 0)
						throw e;
				}
			}

			if (ins != null) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(ins, "UTF-8"));
				String inLine = reader.readLine();
				while (inLine != null) {
					result += (inLine + System.getProperty("line.separator"));
					inLine = reader.readLine();
				}
				reader.close();
				ins.close();
				ftp.getReply();
			}
		} catch (Exception ex) {
			throw new RuntimeException("FTP 读取文件内容错误 :" + ex, ex);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("readerFtpFile disconnect", ioe);
				}
			}
		}
		return result;
	}
	/**
	 * 获取文件修改时间
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String getFileModificationTime(String url, int port, String username, String password, String path, String fileName) {
		String result = "";
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			int portNO = port;
			ftp.setConnectTimeout(connectTimeOut);
			ftp.connect(url, portNO);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException("FTP 获取文件修改时间 - Ftp连接失败");
			}
			ftp.enterLocalPassiveMode();
			if (!ftp.changeWorkingDirectory(path)) {
				ftp.sendCommand(FTPCmd.MKD, path);
				ftp.changeWorkingDirectory(path);
			}
			result = ftp.getModificationTime(fileName);
		} catch (Exception ex) {
			throw new RuntimeException("FTP 获取文件修改时间:" + ex, ex);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("getFileModificationTime disconnect", ioe);
				}
			}
		}
		return result;
	}
	/*
	 * 读取图片列表
	 */
	public static List<String> readFileList(String url, int port,
			String username, String password, String path, String imagePath) {
		ArrayList<String> fileList = new ArrayList<String>();
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			int portNO = port;
			ftp.setConnectTimeout(connectTimeOut);
			ftp.connect(url, portNO);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException("Ftp读取图片列表错误 - tp连接失败");
			}
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(new String(path.getBytes(encoding)));
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().indexOf(".jpg") > 0
						|| ff.getName().indexOf(".gif") > 0
						|| ff.getName().indexOf(".png") > 0
						|| ff.getName().indexOf(".bmp") > 0) {
					fileList.add(imagePath + "/" + ff.getName());
				}
			}
			ftp.logout();
		} catch (IOException e) {
			throw new RuntimeException("Ftp读取图片列表错误 :" + e, e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("readFileList disconnect", ioe);
				}
			}
		}
		return fileList;
	}

	/*
	 * 拷贝文件
	 */
	public static boolean copyFile(String url, int port, String username,
			String password, String srcFilename, String descFileName) {
		boolean returnValue = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.setConnectTimeout(connectTimeOut);
			ftp.connect(url, port);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException("Ftp拷贝文件错误 - Ftp连接失败");
			}
			ftp.enterLocalPassiveMode();

			String path = srcFilename.replace("\\", "/");
			path = srcFilename.substring(0, srcFilename.lastIndexOf('/'));
			if (!ftp.changeWorkingDirectory(path)) {
				ftp.sendCommand(FTPCmd.MKD, path);
			}

			ftp.changeWorkingDirectory("/");

			reply = ftp.sendCommand("XCFR " + srcFilename);
			if (reply != 350)
				return false;

			reply = ftp.sendCommand("XCTO " + descFileName);
			if (reply != 250)
				return false;

			ftp.logout();
			returnValue = true;
		} catch (IOException e) {
			throw new RuntimeException("Ftp拷贝文件错误:" + e, e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("copyFile disconnect", ioe);
				}
			}
		}
		return returnValue;
	}

	/*
	 * 检查文件是否存在
	 */
	public static boolean checkFileExist(String url, int port, String username,
			String password, String fileName) {
		boolean returnValue = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.setConnectTimeout(connectTimeOut);
			ftp.connect(url, port);

			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException("Ftp检查文件是否存在错误 - Ftp连接失败");
			}
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			reply = ftp.sendCommand("SIZE " + fileName);
			if (reply != 213)
				return false;

			ftp.logout();
			returnValue = true;
		} catch (IOException e) {
			throw new RuntimeException("Ftp检查文件是否存在错误:" + e, e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("checkFileExist disconnect", ioe);
				}
			}
		}
		return returnValue;
	}

	/*
	 * 删除文件
	 */
	public static boolean deleteFile(String url, int port, String username,
			String password, String srcpath, String[] fileNames) {
		boolean returnValue = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.setConnectTimeout(connectTimeOut);
			ftp.connect(url, port);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new RuntimeException("Ftp删除文件失败 - Ftp连接失败");
			}
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(new String(srcpath.getBytes(encoding)));
			for (String str : fileNames) {
				ftp.deleteFile(str);
			}
			ftp.logout();
			returnValue = true;
		} catch (IOException e) {
			throw new RuntimeException("Ftp删除文件失败:" + e, e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					throw new RuntimeException("deleteFile disconnect", ioe);
				}
			}
		}
		return returnValue;
	}
}
