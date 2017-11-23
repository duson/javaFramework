package com.facewnd.ad.modules.communication.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.facewnd.ad.modules.communication.MqttSender;
import com.facewnd.ad.modules.communication.enumeration.CommandEnum;
import com.facewnd.ad.modules.communication.model.DeviceMessage;
import com.facewnd.ad.modules.communication.model.cmdparams.AppUpdateParams;
import com.facewnd.ad.modules.communication.model.cmdparams.CommandParamsBase;
import com.facewnd.ad.modules.communication.model.cmdparams.DeviceChangeGroupParams;
import com.facewnd.ad.modules.communication.model.cmdparams.DeviceGroupUpdateParams;
import com.facewnd.ad.modules.communication.model.cmdparams.DeviceUnbindParams;
import com.facewnd.ad.modules.communication.model.cmdparams.ProgramUpdateParams;

@Service
public class MqttSenderImpl implements MqttSender {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	//@Autowired
	//private MqttPahoMessageHandler mqtt;
	@Autowired
	private MqttGateway gateway;
	
	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MqttGateway {
		void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
    }

	@Override
	public String programUpdate(ProgramUpdateParams params) {
		return this.sendMsg(CommandEnum.PROGRAM_UPDATE, params, TOPIC_GROUP_PREFIX + params.getGroupId());
	}

	@Override
	public String deviceGroupUpdate(DeviceGroupUpdateParams params) {
		return this.sendMsg(CommandEnum.GROUP_UPDATE, params, TOPIC_GROUP_PREFIX + params.getGroupId());
	}

	@Override
	public String deviceChangeGroup(DeviceChangeGroupParams params) {
		return this.sendMsg(CommandEnum.DEVICE_CHANGE_GROUP, params, TOPIC_DEVICE_PREFIX + params.getDeviceId());
	}

	@Override
	public String appUpdate(AppUpdateParams params) {
		return this.sendMsg(CommandEnum.APP_UPDATE, params, TOPIC_DEVICE_PREFIX + params.getDeviceId());
	}

	@Override
	public String deviceUnbind(DeviceUnbindParams params) {
		return this.sendMsg(CommandEnum.DEVICE_UNBIND, params, TOPIC_DEVICE_PREFIX + params.getDeviceId());
	}
	
	private String sendMsg(CommandEnum command, CommandParamsBase params, String targetTopic) {
		DeviceMessage msg = new DeviceMessage(command, params);
		
		//Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(msg)).setHeader(MqttHeaders.TOPIC, targetTopic).build();
		//mqtt.handleMessage(message);
		
		String message = JSON.toJSONString(msg);
		logger.debug("send cmd: topic:{}, content:{}", targetTopic, message);		
		gateway.sendToMqtt(message, targetTopic);
		
		return msg.getMessgeId();
	}

}
