package com.facewnd.ad.modules.communication.model.cmdparams;

public class DeviceUnbindParams extends CommandParamsBase {
	
	private Long deviceId; // 终端编号
	
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	
}
