package com.protruly.robot.hotel.client.modules.push.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.protruly.robot.hotel.client.modules.push.model.PushMessage;
import com.protruly.robot.hotel.mq.sdk.MQCallback;
import com.protruly.robot.hotel.mq.sdk.MqConstant;
import com.protruly.robot.hotel.mq.sdk.enumeration.PushUserCommandEnum;
import com.protruly.robot.hotel.mq.sdk.model.MessageBase;

public class FusesourceClient implements PushMqttClient {
	
	private final static long RECONNECTION_ATTEMPT_MAX = 6;  
	private final static long RECONNECTION_DELAY = 2000;  
    private final static short KEEP_ALIVE = 30; // 低耗网络，但是又需要及时获取数据，心跳30
    private final static int SEND_BUFFER_SIZE = 2 * 1024 * 1024; // 发送最大缓冲为2M

	private String mqAddress;
	private MQTT mqttClient = null;
	private MQCallback callback;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public FusesourceClient(String mqAddress, String hotelId) {
		this.mqAddress = mqAddress;
		
		mqttClient = new MQTT();
		try {
			mqttClient.setHost(this.mqAddress);
		} catch (URISyntaxException e) {
			logger.error("init - mqttClient.setHost异常", e);
		}
		
		mqttClient.setClientId("robot-hotel-client-" + hotelId);
		// 连接前清空会话信息  
		mqttClient.setCleanSession(false); 
		// 设置重新连接的次数  
		mqttClient.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);  
        // 设置重连的间隔时间  
		mqttClient.setReconnectDelay(RECONNECTION_DELAY);  
        // 设置心跳时间  
		mqttClient.setKeepAlive(KEEP_ALIVE);  
        // 设置缓冲的大小  
		mqttClient.setSendBufferSize(SEND_BUFFER_SIZE);
	}
	
	@Override
	public String send(PushMessage pushMsg, boolean user, String... targetId) {
		BlockingConnection connection = mqttClient.blockingConnection();
		try {
			if(!connection.isConnected())
				connection.connect();
		} catch (Exception e) {
			logger.error("connect异常", e);
			return "";
		}
		
        String topicPrefix = user ? MqConstant.TOPIC_PREFIX_USER : MqConstant.TOPIC_PREFIX_ROBOT;
        for (String id : targetId) {
        	try {
				connection.publish(topicPrefix + id, JSON.toJSONBytes(pushMsg, SerializerFeature.EMPTY), QoS.AT_LEAST_ONCE, false);
			} catch (Exception e) {
				logger.error("send异常，to：" + id + "，消息：" + JSON.toJSONString(pushMsg), e);
			}
        	
        	logger.debug("消息发送成功，to：" + id + "，消息：" + JSON.toJSONString(pushMsg));
        	
        	if(this.callback != null)
        		this.callback.deliveryComplete(true, pushMsg.getMessgeId());
        }

        if(connection.isConnected()) {
        	try {
				connection.disconnect();
			} catch (Exception e) {
				logger.error("disconnect异常", e);
			}
        }
        
        return "";
	}

	@Override
	public void setCallback(MQCallback callback) {
		this.callback = callback;
	}
	
	public static void main(String[] args) {
		PushMqttClient client = new FusesourceClient("tcp://192.168.1.234:1883", "test1");
		client.setCallback(new MQCallback() {
			
			@Override
			public void messageArrived(String topicName, MessageBase message) {
				System.out.println("messageArrived " + JSON.toJSONString(message));
			}
			
			@Override
			public void deliveryComplete(boolean isComplete, String messageId) {
				System.out.println("deliveryComplete " + messageId);
			}
			
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println("connectionLost");
			}
		});
		
		try {
			System.in.read();
			// 发送消息
			PushMessage pushMsg = new PushMessage("title", "message", PushUserCommandEnum.CHECKIN, null);
			client.send(pushMsg, true, "test2");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
