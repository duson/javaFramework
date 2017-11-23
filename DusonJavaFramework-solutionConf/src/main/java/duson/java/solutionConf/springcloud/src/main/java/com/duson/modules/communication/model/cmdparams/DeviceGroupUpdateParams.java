package com.facewnd.ad.modules.communication.model.cmdparams;

public class DeviceGroupUpdateParams extends CommandParamsBase {
	
	private Long groupId; // 终端组编号
	private String workDay; // 工作日期，1个月的哪些天（用逗号分隔），形如：1,2,3,4,5,6
	private String workTime; // 工作时间段（用逗号分隔），形如：08:00-12:00,13:30-18:00
	private Integer volume; // 音量
	private String mainArea; // 主区域
	private Long backupA; // A区域垫片
	private Long backupB; // B区域垫片
	private Long backupC; // C区域垫片
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getWorkDay() {
		return workDay;
	}
	public void setWorkDay(String workDay) {
		this.workDay = workDay;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public String getMainArea() {
		return mainArea;
	}
	public void setMainArea(String mainArea) {
		this.mainArea = mainArea;
	}
	public Long getBackupA() {
		return backupA;
	}
	public void setBackupA(Long backupA) {
		this.backupA = backupA;
	}
	public Long getBackupB() {
		return backupB;
	}
	public void setBackupB(Long backupB) {
		this.backupB = backupB;
	}
	public Long getBackupC() {
		return backupC;
	}
	public void setBackupC(Long backupC) {
		this.backupC = backupC;
	}
	
}
