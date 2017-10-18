package com.facewnd.ad.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.facewnd.ad.common.config.PropertyConfig;

@Configuration
public class ConfigAutoConfiguration {
	/** 云储存上传地址 **/
	public static String CloudUploadUrl;
	/** 云储存显示图片地址 **/
	public static String CloudShowUrl;
	
	/** Mqtt地址 **/
	public static String MqttAddress;
	
	/** Api请求是否开启签名校验 **/
	public static Boolean Validate;
	
	@Autowired
	private PropertyConfig config;
	
	@Bean
    public int init() {
		CloudUploadUrl = config.getCloudUploadUrl();
		CloudShowUrl = config.getCloudShowUrl();
		MqttAddress = config.getMqttServer();
		Validate = config.getValidate();
		
		return 1;
	}

}
