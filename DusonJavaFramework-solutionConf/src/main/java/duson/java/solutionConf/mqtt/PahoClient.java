package com.protruly.robot.hotel.client.modules.push.impl;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.JsonParseException;
import com.protruly.robot.hotel.client.common.exception.RobotHotelException;
import com.protruly.robot.hotel.client.modules.push.model.PushMessage;
import com.protruly.robot.hotel.mq.sdk.MQCallback;
import com.protruly.robot.hotel.mq.sdk.MqConstant;
import com.protruly.robot.hotel.mq.sdk.model.MessageBase;
import com.protruly.robot.hotel.mq.sdk.model.RobotMessage;
import com.protruly.robot.hotel.mq.sdk.model.UserMessage;

public class PahoClient implements PushMqttClient {
	private String mqAddress;
	private String hotelId;
	private MqttClient mqttClient = null;
	private MqttConnectOptions connOpts = null;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public PahoClient(String mqAddress, String hotelId) {
		this.mqAddress = mqAddress;
		this.hotelId = hotelId;
		
		initMqttClient();
	}
	
	@Override
	public String send(PushMessage pushMsg, boolean user, String... targetId) {
		try {
			mqttClient.connect(connOpts);
		} catch (MqttException e) {
			logger.error("Mqtt初始化异常", e);
			throw new RobotHotelException("Mqtt初始化异常", e);
		}
		
		MqttMessage msg = new MqttMessage(JSON.toJSONBytes(pushMsg, SerializerFeature.EMPTY));
        msg.setQos(2);
        String topicPrefix = user ? MqConstant.TOPIC_PREFIX_USER : MqConstant.TOPIC_PREFIX_ROBOT;
		
        try {
        	for (String id : targetId) {
        		mqttClient.publish(topicPrefix + id, msg);
			}
		} catch (MqttException e) {
			logger.error("send异常，消息：" + JSON.toJSONString(pushMsg), e);
		}
        finally {
        	try {
        		if(mqttClient.isConnected())
        			mqttClient.disconnect();
			} catch (MqttException e) {
				logger.error("Mqtt断开连接异常", e);
			}
        }
        
		return String .valueOf(msg.getId());
	}
	
	public void setCallback(MQCallback callback) {
		mqttClient.setCallback(new MqttCallback() {
			public void connectionLost(Throwable cause) {
				logger.debug("connectionLost----------");
				callback.connectionLost(cause);
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				logger.debug("deliveryComplete---------" + token.isComplete());
				callback.deliveryComplete(token.isComplete(), String.valueOf(token.getMessageId()));
			}
			@Override
			public void messageArrived(String topicName, MqttMessage message) throws Exception {
				logger.debug("messageArrived----------" + message.toString());

				MessageBase msg = null;
				
				if(topicName.startsWith(MqConstant.TOPIC_PREFIX_USER)) {
					try {
						msg = JSON.parseObject(message.toString(), UserMessage.class);
					} catch(JsonParseException ex) {
						logger.error("反序列化用户消息异常", ex);
					}
				} else if(topicName.startsWith(MqConstant.TOPIC_PREFIX_ROBOT)) {
					try {
						msg = JSON.parseObject(message.toString(), RobotMessage.class);
					} catch(JsonParseException ex) {
						logger.error("反序列化设备消息异常", ex);
					}
				}
				
				if(msg != null)
					callback.messageArrived(topicName, msg);
			}
		});
	}
	
	private void initMqttClient() {
		try {
			mqttClient = new MqttClient(this.mqAddress, "robot-hotel-client-" + this.hotelId, new MemoryPersistence());
		} catch (MqttException e) {
			logger.error("Mqtt初始化异常", e);
			throw new RobotHotelException("Mqtt连接异常", e);
		}
		
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(false);
        //connOpts.setUserName("admin");
        //connOpts.setPassword("password".toCharArray());
        // 设置超时时间 单位为秒 
        //connOpts.setConnectionTimeout(10);
        connOpts.setAutomaticReconnect(true);
     	// 设置会话心跳时间 单位为秒 服务器会每隔1.5*N秒的时间向客户端发送个消息判断客户端是否在线
        connOpts.setKeepAliveInterval(20);
        
        mqttClient.setCallback(new MqttCallback() {
			public void connectionLost(Throwable cause) {
				logger.debug("connectionLost----------");  
			}
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				String msg = "";
				try {
					MqttMessage message = token.getMessage();
					if(message != null)
						msg = new String(message.getPayload());
				} catch (MqttException e) { }
				logger.debug("deliveryComplete---------" + token.isComplete() + msg);  
			}
			@Override
			public void messageArrived(String topicName, MqttMessage message) throws Exception {
				logger.debug("messageArrived----------" + topicName+"---"+message.toString());  
			}
		});
        
        // 如需接收客户端消息，建议专门的监听类
        // mqttClient.connect(connOpts);
        // mqttClient.subscribe(MqConstant.TOPIC_PREFIX_SERVER + "#", MqConstant.QOS);
	}
}
