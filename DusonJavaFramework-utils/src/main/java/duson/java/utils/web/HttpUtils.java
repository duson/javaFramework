package duson.java.utils.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import org.apache.http.entity.StringEntity;
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
