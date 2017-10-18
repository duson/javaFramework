package com.facewnd.ad.api.modules.device.reponseparams;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="App升级结果实体")
public class AppUpgradeReponse {
	
	@ApiModelProperty(value="版本号")
	private String version;
	
	@ApiModelProperty(value="下载地址")
	private String downloadUrl;
	
	@ApiModelProperty(value="升级说明")
	private String note;
	
	@ApiModelProperty(value="更新时间")
	private String updateTime;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


}
