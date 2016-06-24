package duson.java.utils.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	
	public static String get(String url, String params) throws IOException {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + params;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
 
            // 建立实际的连接
            conn.connect();
            
            // 获取所有响应头字段
            /*Map<String, List<String>> map = conn.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
 
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
	
	public void postFile(String url, String fileName, byte[] fileData) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "<Boundary>"; // 分隔字符串
		conn.setUseCaches(false); // post方式不能使用缓存

		// 采用流方式上传数据，无需本地缓存数据
		conn.setChunkedStreamingMode(1024 * 1024);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
		conn.setRequestProperty("filename", fileName);
		conn.setRequestProperty("Range", "" + 0L);
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		dos.writeBytes(twoHyphens + boundary + end);
		dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"" + fileName + "\"" + end);
		dos.writeBytes(end);
		dos.write(fileData);
		// very import segment below
		dos.writeBytes(end);
		dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
		// very import segment above
		dos.flush();
		dos.close();

		// 读取返回内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
		}
	}
	
	public void get() throws Exception {  
        // 创建HttpClient实例  
        HttpClient client = new DefaultHttpClient();  
        // 根据URL创建HttpGet实例  
        HttpGet get = new HttpGet("http://");  
        // 执行get请求，得到返回体  
        HttpResponse response = client.execute(get);  
        // 判断是否正常返回  
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
            // 解析数据  
            String data = EntityUtils.toString(response.getEntity());  
            System.out.println(data);  
        }  
    }
	
	public void post() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://");
		post.setHeader("auth", "");
		
		// 构造post参数  
        List<NameValuePair> params = new ArrayList<NameValuePair>();  
        params.add(new BasicNameValuePair("name", "11"));  
        // 编码格式转换  
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
        
        // Post Body
		//StringEntity entity = new StringEntity("Post内容", "UTF-8");
		//entity.setContentType("application/json");
		
        // 传入请求体  
        post.setEntity(entity);
		
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
            // 解析数据  
            String data = EntityUtils.toString(response.getEntity());  
            System.out.println(data);  
        }
	}
}
