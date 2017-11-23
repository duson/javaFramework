package com.facewnd.ad.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facewnd.ad.autoconfig.ConfigAutoConfiguration;

public class CloudStorageHelper {
	
	private final static Logger logger = LoggerFactory.getLogger(CloudStorageHelper.class);

	public static String uploadFile(InputStream file1Stream, String fileName) throws ParseException, IOException {
		HttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(ConfigAutoConfiguration.CloudUploadUrl);
        HttpEntity entity = MultipartEntityBuilder.create()
        	.addBinaryBody("uploadedfile", file1Stream, org.apache.http.entity.ContentType.MULTIPART_FORM_DATA, fileName)
        	.build();
  
        postRequest.setEntity(entity);
        HttpResponse response = client.execute(postRequest);
        
        HttpEntity responseEntity = response.getEntity();
        if(responseEntity != null) {
        	String result = EntityUtils.toString(responseEntity);
        	logger.debug("=====uploadFile result=" + result);
			JSONObject JSONObject = (JSONObject)JSON.parse(result);
			if (JSONObject.getString("errcode").equals("0")) {
				JSONArray jsonArray = JSONArray.parseArray(JSONObject.getString("data"));
				JSONObject dataOb = (JSONObject) jsonArray.get(0);
				return dataOb.getString("md5filename");
			}
		}
		
		return "";
	}
	
}
