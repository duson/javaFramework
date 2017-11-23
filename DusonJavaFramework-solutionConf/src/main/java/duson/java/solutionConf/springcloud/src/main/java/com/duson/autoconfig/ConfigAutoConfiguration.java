package com.facewnd.ad.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.facewnd.ad.common.config.PropertyConfig;

@Configuration
public class ConfigAutoConfiguration {
	public static String AppId;
	public static String AppSecret;
	
	/** 云储存上传地址 **/
	public static String CloudUploadUrl;
	/** 云储存显示图片地址 **/
	public static String CloudShowUrl;
	/** 获取云储存文件大小 **/
	public static String CloudGetSize;
	/** 转化服务器地址 **/
	public static String ConvertServerUrl;
	/** 云存储转化回调方法 **/
	public static String ConvertServerCallbackUrl;
	/** 云存储业务id**/
	public static String SystemId;
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
		CloudGetSize = config.getCloudGetSize();
		ConvertServerUrl = config.getConvertServerUrl();
		ConvertServerCallbackUrl = config.getConvertServerCallbackUrl();
		SystemId = config.getSystemId();
		MqttAddress = config.getMqttServer();
		Validate = config.getValidate();
		
		return 1;
	}

}
