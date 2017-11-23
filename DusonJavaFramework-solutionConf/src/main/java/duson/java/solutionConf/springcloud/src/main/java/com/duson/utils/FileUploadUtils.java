package com.facewnd.ad.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 调用云存储工具类
 * 
 * @author 彭彩云
 * @version [版本号, 2014-12-3 下午1:44:43]
 */
public class FileUploadUtils {
	private static Logger logger = LogManager.getLogger(new FileUploadUtils().getClass());
	/**
	 * 文件上传(小文件和图片上传)
	 * 
	 * @param con_url
	 * @param file
	 * @return [参数说明]
	 * @return String 返回文件在云存储MD5文件路径
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String uploadFile(String con_url,String appId,String userId,String tokenId, InputStream is , String fileName, long range) throws Exception{
		//上传文件大小不能超过200m
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(con_url).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);

			String endline = "\r\n";
			String twoHyphens = "--";
			String boundary = "kingsoft7816510d1cnit";
			String endline_data = endline + twoHyphens + boundary + twoHyphens + endline;

			// post方式不能使用缓存
			conn.setUseCaches(false);

			// 告诉HttpUrlConnection,我们需要采用流方式上传数据，无需本地缓存数据
			conn.setChunkedStreamingMode(1024 * 1024);
			conn.setRequestMethod("POST"); // 设置请求方法为POST, 也可以为GET
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");

			// very import file segment below 表单参数处理
			StringBuffer sb = new StringBuffer();
			//安全参数对偶
			String[] props = { "appid", "uid", "token" };
			List<String> values = new ArrayList<String>();
			if(null != appId) { values.add(appId); }
			if(null != userId) { values.add(userId); }
			if(null != tokenId) { values.add(java.net.URLEncoder.encode(tokenId,"UTF-8")); }
			for (int i = 0,li=values.size();i<li; i++) {
				sb = sb.append(twoHyphens);
				sb = sb.append(boundary);
				sb = sb.append(endline);
				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"");
				sb = sb.append(endline);
				sb = sb.append(endline);
				sb = sb.append(URLEncoder.encode(values.get(i), "utf-8"));
				sb = sb.append(endline);
			}
			// very import segment above

			// very import segment below 上传文件进行处理
			{
				sb = sb.append(twoHyphens);
				sb = sb.append(boundary);
				sb = sb.append(endline);
				sb = sb.append("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"" + fileName + "\"");
				sb = sb.append(endline);
				sb = sb.append("Content-Type: application/octet-stream");
				sb = sb.append(endline);
				sb = sb.append(endline);
			}
			// very import segment above

			// 设置HTTP头
			conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("filename", fileName);
			conn.setRequestProperty("Range", "" + range);

			// 表单内容打包
			byte[] data = sb.toString().getBytes();
			// 表单内容打包

			// 打开上传文件

			DataInputStream in = new DataInputStream(is);

			// 打开上传文件

			int len = 0;
			byte[] bufferOut = new byte[1024];

			// 读取文件,指定断点续传的偏移量
			//in.skip(range);

			//http 上传文件
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			// 输出表单数据
			dos.write(data);

			// 输出文件内容
			while ((len = in.read(bufferOut)) != -1) {
				dos.write(bufferOut, 0, len);
				dos.flush();
			}
			// 输出文件内容
			// very import segment below
			dos.writeBytes(endline_data);
			// very import segment above
			// 文件上传结束打包

			// /////文件上传结束打包//////////////////
			in.close();
			dos.flush();
			dos.close();

			// response data processing
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			StringBuilder reqDataBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				reqDataBuilder.append(line);
			}

			logger.debug("上传返回JSON值:" + reqDataBuilder.toString());
			JSONObject jsonObject = JSON.parseObject(reqDataBuilder.toString());
			if (jsonObject.getString("errcode").equals("0")) {
				JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
				JSONObject dataOb = (JSONObject) jsonArray.get(0);
				return dataOb.getString("md5filename");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(conn != null) {
				conn.disconnect();
				conn = null;
			}
		}

		return null;
	}
	/**
	 * 文件上传(小文件和图片上传)
	 * 
	 * @param con_url
	 * @param file
	 * @return [参数说明]
	 * @return String 返回文件在云存储MD5文件路径
	 * @exception throws [违例类型] [违例说明]
	 */
	public static long getFileSize(String con_url , String md5FileName, boolean userCache) throws Exception{//md5.后缀名
		String result = "";
		BufferedReader in = null;
		try {
			//处理路径 比如以下路径是错误的：http://192.168.1.227/swallow/size//d41d8cd98f00b204e9800998ecf8427e.png?1453344176398
			//size后面不能有//
			con_url = (con_url.lastIndexOf("/")==con_url.length()-1)  ? con_url.substring(0,con_url.length()-1) : con_url;
			md5FileName = md5FileName.indexOf("/")==0? md5FileName.substring(1) : md5FileName;
			String urlNameString = con_url + "/" + md5FileName + (userCache? "":"?"+new Date().getTime());
			
			System.out.println("######## getFileSize url:" + urlNameString);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("get FileSize result:"+result);
			//"{"ret":-1,"errcode":-2,"msg":"calling servlet not exist"}"
			//{"ret":0,"errcode":0,"msg":"apolo success","data":[{"filename":"/d4/1d/d41d8cd98f00b204e9800998ecf8427e.png","filesize":38979}]}
			if( !"".equals(result) ){
				JSONObject jsonObject = JSON.parseObject(result);
				if (jsonObject.getString("errcode").equals("0")) {
					JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
					JSONObject dataOb = (JSONObject) jsonArray.get(0);
					result = dataOb.getString("filesize");
				}else{
					result = "";
				}
			}

		} catch (Exception e) {
			result = "";
			e.printStackTrace();
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				throw e2;
			}
		}
		result = "".equals(result)? "0" : result;
		//判断是否是整数
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(result);
		result =  m.matches()? result :"0";
		return Long.parseLong(result);
	}

	/**
	 * 上传本地文件到ftp
	 * @param con_url
	 * @param file
	 * @return [参数说明]
	 * @return String 
	 * @exception throws [违例类型] [违例说明]
	 */
	@SuppressWarnings({ "resource", "unused" })
	public static String uploadLocalFile(String con_url, File file) {
		HttpURLConnection conn = null;
		try {

			FileInputStream is = new FileInputStream(file);

			conn = (HttpURLConnection) new URL(con_url).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "kingsoft7816510d1cnit";
			// post方式不能使用缓存
			conn.setUseCaches(false);

			// 告诉HttpUrlConnection,我们需要采用流方式上传数据，无需本地缓存数据
			conn.setChunkedStreamingMode(1024 * 1024);
			conn.setRequestMethod("POST"); // 设置请求方法为POST, 也可以为GET
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("filename", file.getName());
			conn.setRequestProperty("Range", "" + 0L);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"" + file.getName() + "\"" + end);
			dos.writeBytes(end);
			byte a[]=new byte[60];
			int n;
			while((n = is.read(a)) != -1){
				dos.write(a);
			}
			// very import segment below
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			// very import segment above
			dos.flush();
			dos.close();

			// response data processing
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				logger.debug("上传返回JSON值:" + line);
				JSONObject jsonObject = JSON.parseObject(line);
				if (jsonObject.getString("errcode").equals("0")) {
					JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
					JSONObject dataOb = (JSONObject) jsonArray.get(0);
					return dataOb.getString("md5filename");
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception{
		String json = "{\"ret\":0,\"errcode\":0,\"msg\":\"success\",\"data\":[{\"appid\":\"10010\",\"uid\":\"41596098@qq.com\",\"filename\":\"QQG20140922174239.jpg\",\"md5filename\":\"39b69b4bc6b8d7e6765f903867ead441.jpg\",\"suffix\":\"jpg\",\"filesize\":107987,\"succsize\":107987,\"requstip\":\"10.10.10.180\",\"url\":\"/39/b6/39b69b4bc6b8d7e6765f903867ead441.jpg\",\"mode\":1,\"status\":0,\"errmsg\":\"apolo success\"}]}";
		JSONObject jsonObject = JSON.parseObject(json);
		if (jsonObject.getString("msg").equals("success")) {
			JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
			JSONObject dataOb = (JSONObject) jsonArray.get(0);
			String fileUrl = "http://10.10.10.180/swallow/show" + dataOb.getString("url");
			System.out.println(fileUrl);
		}

		System.out.println( getFileSize("http://192.168.1.227/swallow/size/","/d41d8cd98f00b204e9800998ecf8427e.png",false) );
	}
}
