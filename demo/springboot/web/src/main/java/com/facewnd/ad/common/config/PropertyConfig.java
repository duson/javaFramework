package com.facewnd.ad.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class PropertyConfig {

	@Value("${sys.validate.enable}")
    private Boolean validate;
	
	@Value("${mqtt.server.address}")
	private String mqttServer;
	
	@Value(value = "${cloud.userId}")
	private String cloudUserId;
	@Value(value = "${cloud.tokenId}")
	private String cloudTokenId;
	//#云存储HTTP上传服务地址
	@Value(value = "${cloud.HttpURLConnection.upload.url}")
	private String cloudUploadUrl;
	//#云存储HTTP查看服务地址
	@Value(value = "${cloud.HttpURLConnection.show.url}")
	private String cloudShowUrl;
	
	public Boolean getValidate() {
		return validate;
	}
	public void setValidate(Boolean validate) {
		this.validate = validate;
	}

	public String getCloudUserId() {
		return cloudUserId;
	}
	public void setCloudUserId(String cloudUserId) {
		this.cloudUserId = cloudUserId;
	}
	public String getCloudTokenId() {
		return cloudTokenId;
	}
	public void setCloudTokenId(String cloudTokenId) {
		this.cloudTokenId = cloudTokenId;
	}
	public String getCloudUploadUrl() {
		return cloudUploadUrl;
	}
	public void setCloudUploadUrl(String cloudUploadUrl) {
		this.cloudUploadUrl = cloudUploadUrl;
	}
	public String getCloudShowUrl() {
		return cloudShowUrl;
	}
	public void setCloudShowUrl(String cloudShowUrl) {
		this.cloudShowUrl = cloudShowUrl;
	}
	public String getMqttServer() {
		return mqttServer;
	}
	public void setMqttServer(String mqttServer) {
		this.mqttServer = mqttServer;
	}

}
