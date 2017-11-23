package com.facewnd.ad.modules.communication;

import com.facewnd.ad.modules.communication.model.cmdparams.AppUpdateParams;
import com.facewnd.ad.modules.communication.model.cmdparams.DeviceChangeGroupParams;
import com.facewnd.ad.modules.communication.model.cmdparams.DeviceGroupUpdateParams;
import com.facewnd.ad.modules.communication.model.cmdparams.DeviceUnbindParams;
import com.facewnd.ad.modules.communication.model.cmdparams.ProgramUpdateParams;

public interface MqttSender {
	public static final String TOPIC_BROADCAST_PREFIX = "broadcast/";
	public static final String TOPIC_SERVER_PREFIX = "server/";
	public static final String TOPIC_GROUP_PREFIX = "group/"; 
	public static final String TOPIC_DEVICE_PREFIX = "device/"; 

	/**
	 * 节目更新
	 * @param params
	 * @return
	 */
	String programUpdate(ProgramUpdateParams params);
	
	/**
	 * 终端组设置
	 * @param params
	 * @return
	 */
	String deviceGroupUpdate(DeviceGroupUpdateParams params);
	
	/**
	 * 变更终端组 
	 * @param params
	 * @return
	 */
	String deviceChangeGroup(DeviceChangeGroupParams params);
	
	/**
	 * 终端升级
	 * @param params
	 * @return
	 */
	String appUpdate(AppUpdateParams params);
	
	/**
	 * 解绑
	 * @param params
	 * @return
	 */
	String deviceUnbind(DeviceUnbindParams params);

}
