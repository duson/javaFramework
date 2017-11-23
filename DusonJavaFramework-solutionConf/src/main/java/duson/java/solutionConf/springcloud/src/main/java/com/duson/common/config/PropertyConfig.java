package com.facewnd.ad.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class PropertyConfig {
	@Value("${sms.sign}")
	private String smsSign;//短信签名
	@Value("${sms.template}")
	private String smsTemplate;//短信模板
	@Value("${sms.system.url}")
	private String smsSystemUrl;
	@Value("${sms.system.name}")
	private String smsSystemName;
	@Value("${sms.system.pwd}")
	private String smsSystemPwd;
	@Value("${sms.validSecond}")
	private long smsValidSecond;// 有效时间 （单位：秒）
	// 是否启用登录验证码功能
	@Value("${app.enable.authcode}")
	private Boolean enbaleAuthCode;
	@Value("${sys.validate.enable}")
	private Boolean validate;

	@Value("${mqtt.server.address}")
	private String mqttServer;
	@Value(value = "${systemId}")
	private String systemId;
	@Value(value = "${cloud.userId}")
	private String cloudUserId;
	@Value(value = "${cloud.tokenId}")
	private String cloudTokenId;
	// #云存储HTTP上传服务地址
	@Value(value = "${cloud.HttpURLConnection.upload.url}")
	private String cloudUploadUrl;
	// #云存储HTTP查看服务地址
	@Value(value = "${cloud.HttpURLConnection.show.url}")
	private String cloudShowUrl;
	// 云存储HTTP获取文件大小服务地址
	@Value(value = "${cloud.HttpURLConnection.getSize.url}")
	private String cloudGetSize;
	// 转化服务器地址
	@Value(value = "${convertServer.url}")
	private String convertServerUrl;
	// 云存储转化回调方法
	@Value(value = "${convertServer.callback.url}")
	private String convertServerCallbackUrl;

	@Value(value = "${apply.company.licenseNumb}")
	private int licenseNumb;
	@Value(value = "${apply.company.licenseDays}")
	private int licenseDays;

	@Value(value = "${sys.appId}")
	private String appId;
	@Value(value = "${sys.appSecret}")
	private String appSecret;
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSmsSign() {
		return smsSign;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}

	public String getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

	public String getSmsSystemName() {
		return smsSystemName;
	}

	public void setSmsSystemName(String smsSystemName) {
		this.smsSystemName = smsSystemName;
	}

	public String getSmsSystemUrl() {
		return smsSystemUrl;
	}

	public void setSmsSystemUrl(String smsSystemUrl) {
		this.smsSystemUrl = smsSystemUrl;
	}

	public String getSmsSystemPwd() {
		return smsSystemPwd;
	}

	public void setSmsSystemPwd(String smsSystemPwd) {
		this.smsSystemPwd = smsSystemPwd;
	}

	public long getSmsValidSecond() {
		return smsValidSecond;
	}

	public void setSmsValidSecond(long smsValidSecond) {
		this.smsValidSecond = smsValidSecond;
	}

	public Boolean getEnbaleAuthCode() {
		return enbaleAuthCode;
	}

	public void setEnbaleAuthCode(Boolean enbaleAuthCode) {
		this.enbaleAuthCode = enbaleAuthCode;
	}

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

	public String getCloudGetSize() {
		return cloudGetSize;
	}

	public void setCloudGetSize(String cloudGetSize) {
		this.cloudGetSize = cloudGetSize;
	}

	public String getConvertServerUrl() {
		return convertServerUrl;
	}

	public void setConvertServerUrl(String convertServerUrl) {
		this.convertServerUrl = convertServerUrl;
	}

	public String getConvertServerCallbackUrl() {
		return convertServerCallbackUrl;
	}

	public void setConvertServerCallbackUrl(String convertServerCallbackUrl) {
		this.convertServerCallbackUrl = convertServerCallbackUrl;
	}

	public int getLicenseNumb() {
		return licenseNumb;
	}

	public void setLicenseNumb(int licenseNumb) {
		this.licenseNumb = licenseNumb;
	}

	public int getLicenseDays() {
		return licenseDays;
	}

	public void setLicenseDays(int licenseDays) {
		this.licenseDays = licenseDays;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
