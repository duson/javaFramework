package com.facewnd.ad.modules.communication.model.cmdparams;

import java.util.Date;

import com.facewnd.ad.common.enumeration.SourceEnum;

public class ProgramUpdateParams extends CommandParamsBase {
	private Long groupId; // 终端组编号
	private SourceEnum playType; // 通道NORMAL或ORDER
	private String area; // 区域 A B C
	private Date days; // 日期
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public SourceEnum getPlayType() {
		return playType;
	}
	public void setPlayType(SourceEnum playType) {
		this.playType = playType;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getDays() {
		return days;
	}
	public void setDays(Date days) {
		this.days = days;
	}
}
