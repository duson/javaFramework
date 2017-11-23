package com.facewnd.ad.modules.communication.model;

import java.util.Date;
import java.util.UUID;

import com.facewnd.ad.modules.communication.enumeration.CommandEnum;
import com.facewnd.ad.modules.communication.model.cmdparams.CommandParamsBase;

public class DeviceMessage {
	private String messgeId = UUID.randomUUID().toString().replaceAll("-", "");
	private Date timestamp = new Date(); // 时间截
	private CommandEnum command; // 命令
	private CommandParamsBase params; // 参数
	
	public DeviceMessage(CommandEnum command, CommandParamsBase params) {
		this.command = command;
		this.params = params;
	}

	public String getMessgeId() {
		return messgeId;
	}

	public void setMessgeId(String messgeId) {
		this.messgeId = messgeId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public CommandEnum getCommand() {
		return command;
	}

	public void setCommand(CommandEnum command) {
		this.command = command;
	}

	public CommandParamsBase getParams() {
		return params;
	}

	public void setParams(CommandParamsBase params) {
		this.params = params;
	}

}
