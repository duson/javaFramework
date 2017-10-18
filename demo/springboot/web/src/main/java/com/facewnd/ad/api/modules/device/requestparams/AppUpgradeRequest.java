package com.facewnd.ad.api.modules.device.requestparams;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="App升级请求实体")
public class AppUpgradeRequest {
	
	@ApiModelProperty(value="终端Id", required=true)
	@NotNull(message="终端Id不能为空")
	private Long deviceId;
	
	@ApiModelProperty(value="App类型，0：Android，1：IOS", required=true)
	private Short appType;

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Short getAppType() {
		return appType;
	}

	public void setAppType(Short appType) {
		this.appType = appType;
	}
	
}
