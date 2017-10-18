package com.protruly.robot.hotel.client.modules.push.impl;

import com.protruly.robot.hotel.client.modules.push.model.PushMessage;
import com.protruly.robot.hotel.mq.sdk.MQCallback;

public interface PushMqttClient {
	String send(PushMessage pushMsg, boolean user, String... targetId);
	
	void setCallback(MQCallback callback);
}
