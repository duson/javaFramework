package com.facewnd.ad.modules.communication.model.cmdparams;

public class AppUpdateParams extends CommandParamsBase {
	
	private Long deviceId; // 终端编号
	private String appVersion; // app版本(可选），形如：1.0.1
	
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
}
