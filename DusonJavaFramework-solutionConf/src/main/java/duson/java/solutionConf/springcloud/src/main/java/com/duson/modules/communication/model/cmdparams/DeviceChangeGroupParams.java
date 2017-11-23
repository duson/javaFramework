package com.facewnd.ad.modules.communication.model.cmdparams;

public class DeviceChangeGroupParams extends CommandParamsBase {
	
	private Long deviceId; // 终端编号
	private Long changeGroupId; // 变更终端组编号
	
	public DeviceChangeGroupParams() { }
	
	public DeviceChangeGroupParams(Long deviceId, Long changeGroupId) {
		this.deviceId = deviceId;
		this.changeGroupId = changeGroupId;
	}
	
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public Long getChangeGroupId() {
		return changeGroupId;
	}
	public void setChangeGroupId(Long changeGroupId) {
		this.changeGroupId = changeGroupId;
	}
	
}
