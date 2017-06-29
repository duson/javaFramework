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

/**
 * 依赖：httpmime（org.apache.httpcomponents）
 */
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

    // 证书进行https加密传输
    private void excuteWithSSL() {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("CertLocalPath")); //加载本地的证书进行https加密传输 JAVA只需要使用apiclient_cert.p12即可
        try {
            keyStore.load(instream, "CertPassword".toCharArray());//设置证书密码  PKCS12的密码(商户ID)
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "CertPassword".toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        HttpClients httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
    }
	
	public void post() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
        // client = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient);
        client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		HttpPost post = new HttpPost("http://");
        // HttpPost post = HttpClientConnectionManager.getPostMethod(url);
		post.setHeader("auth", "");

        // 可设置请求器的配置
        // int socketTimeout = 10000; // 连接超时时间，默认10秒
        // int connectTimeout = 30000; // 传输超时时间，默认30秒
        // RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        // post.setConfig(requestConfig);
		
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

    /**
     * 执行Http请求
     * 
     * @param request 请求数据
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @return 
     * @throws HttpException, IOException 
     */
    public HttpResponse postFile(HttpRequest request, String strParaFileName, String strFilePath) throws HttpException, IOException {
        // HTTP连接管理器，该连接管理器必须是线程安全的.
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(30);
        connectionManager.getParams().setMaxTotalConnections(80);

        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(60000); /** 闲置连接超时时间, 由bean factory设置，缺省为60秒钟 */

        HttpClient httpclient = new HttpClient(connectionManager);

        // 设置连接超时 
        int connectionTimeout = 8000; /** 连接超时时间，由bean factory设置，缺省为8秒钟 */
        if (request.getConnectionTimeout() > 0) {
            connectionTimeout = request.getConnectionTimeout();
        }
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);

        // 设置回应超时
        int soTimeout = 30000; /** 回应超时时间, 由bean factory设置，缺省为30秒钟 */
        if (request.getTimeout() > 0) {
            soTimeout = request.getTimeout();
        }
        httpclient.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);

        // 设置等待ConnectionManager释放connection的时间 /** 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒*/
        httpclient.getParams().setConnectionManagerTimeout(3000);

        String charset = request.getCharset();
        charset = charset == null ? DEFAULT_CHARSET : charset;
        HttpMethod method = null;

        //get模式且不带上传文件
        if (request.getMethod().equals(HttpRequest.METHOD_GET)) {
            method = new GetMethod(request.getUrl()setCredentialCharset);
            method.getParams().(charset);

            // parseNotifyConfig会保证使用GET方法时，request一定使用QueryString
            method.setQueryString(request.getQueryString());
        } else if(strParaFileName.equals("") && strFilePath.equals("")) {
            //post模式且不带上传文件
            method = new PostMethod(request.getUrl());
            ((PostMethod) method).addParameters(request.getParameters());
            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=" + charset);
        }
        else {
            //post模式且带上传文件
            method = new PostMethod(request.getUrl());
            List<Part> parts = new ArrayList<Part>();
            for (int i = 0; i < request.getParameters().length; i++) {
                parts.add(new StringPart(request.getParameters()[i].getName(), request.getParameters()[i].getValue(), charset));
            }
            //增加文件参数，strParaFileName是参数名，使用本地文件
            parts.add(new FilePart(strParaFileName, new FilePartSource(new File(strFilePath))));
            
            // 设置请求体
            ((PostMethod) method).setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[0]), new HttpMethodParams()));
        }

        // 设置Http Header中的User-Agent属性
        method.addRequestHeader("User-Agent", "Mozilla/4.0");
        HttpResponse response = new HttpResponse();

        try {
            httpclient.executeMethod(method);
            if (request.getResultType().equals(HttpResultType.STRING)) {
                response.setStringResult(method.getResponseBodyAsString());
            } else if (request.getResultType().equals(HttpResultType.BYTES)) {
                response.setByteResult(method.getResponseBody());
            }
            response.setResponseHeaders(method.getResponseHeaders());
        } catch (UnknownHostException ex) {

            return null;
        } catch (IOException ex) {

            return null;
        } catch (Exception ex) {

            return null;
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public postFile(url, File) {
    	HttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost (url) ;

        HttpEntity entity = MultipartEntityBuilder.create()
        	.addTextBody("fileName", file.getName())
            //.addPart("file", new InputStreamBody(inputStream, fileName))
        	.addBinaryBody("file", file, org.apache.http.entity.ContentType.MULTIPART_FORM_DATA, file.getName())
        	.build();
        //FileBody fileBody = new FileBody(file, "application/octect-stream") ;

        postRequest.setEntity(entity) ;
        HttpResponse response = client.execute(postRequest) ;
        if (response != null) {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    public static String postFile(String urlString, InputStream inputStream, String fileName, Map<String, Object> params) {        
        CloseableHttpClient client = HttpClients.createSystem();
        HttpPost postRequest = new HttpPost (urlString) ;

        MultipartEntityBuilder entityBuild = MultipartEntityBuilder.create()
                .addPart("file", new InputStreamBody(inputStream, fileName));
        
        for (Map.Entry<String, Object> item : params.entrySet()) {
            if(item.getValue() instanceof String)
                entityBuild.addTextBody(item.getKey(), item.getValue().toString());
        }
        HttpEntity entity = entityBuild.build();
        postRequest.setEntity(entity) ;
        CloseableHttpResponse response = null;
        try {
            response = client.execute(postRequest);
        } catch (UnsupportedEncodingException e) {
            logger.error("请求数据编码错误:", e);
        } catch (ClientProtocolException e) {
            logger.error("协议异常:", e);
        } catch (IOException e) {
            logger.error("http协议连接超时:", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("关闭数据流异常:", e);
                }
            }
        }
        
        if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (ParseException | IOException e) {
                logger.error("请求数据解析异常:", e);
            }
        }

        return null;
    }

}
