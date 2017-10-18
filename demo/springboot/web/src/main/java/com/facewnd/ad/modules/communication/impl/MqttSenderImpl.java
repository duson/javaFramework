package com.facewnd.ad.modules.communication.impl;

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
import com.facewnd.ad.modules.communication.model.cmdparams.ProgramUpdateParams;

@Service
public class MqttSenderImpl implements MqttSender {
	
	@Autowired
	private MqttPahoMessageHandler mqtt;
	@Autowired
	private MqttGateway gateway;
	
	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MqttGateway {
		void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
    }

	@Override
	public String programUpdate(ProgramUpdateParams params) {
		DeviceMessage msg = new DeviceMessage(CommandEnum.PROGRAM_UPDATE, params);
		Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(msg)).setHeader(MqttHeaders.TOPIC, TOPIC_GROUP_PREFIX + params.getGroupId()).build();
		mqtt.handleMessage(message);
		
		//gateway.sendToMqtt(JSON.toJSONString(msg), TOPIC_GROUP_PREFIX + params.getGroupId());
		
		return msg.getMessgeId();
	}

}
