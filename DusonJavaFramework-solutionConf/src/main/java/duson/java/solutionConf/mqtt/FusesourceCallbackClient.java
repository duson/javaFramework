package com.protruly.robot.hotel.client.modules.push.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.protruly.robot.hotel.client.modules.push.model.PushMessage;
import com.protruly.robot.hotel.mq.sdk.MQCallback;
import com.protruly.robot.hotel.mq.sdk.MqConstant;
import com.protruly.robot.hotel.mq.sdk.enumeration.PushUserCommandEnum;
import com.protruly.robot.hotel.mq.sdk.model.MessageBase;
import com.protruly.robot.hotel.mq.sdk.model.RobotMessage;
import com.protruly.robot.hotel.mq.sdk.model.UserMessage;

public class FusesourceCallbackClient implements PushMqttClient {
	
	private final static long RECONNECTION_ATTEMPT_MAX = 6;  
	private final static long RECONNECTION_DELAY = 2000;  
    private final static short KEEP_ALIVE = 30; // 低耗网络，但是又需要及时获取数据，心跳30
    private final static int SEND_BUFFER_SIZE = 2 * 1024 * 1024; // 发送最大缓冲为2M

	private String mqAddress;
	private MQTT mqttClient = null;
	private MQCallback callback;
	private CallbackConnection connection = null;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public FusesourceCallbackClient(String mqAddress, String hotelId) {
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
		
		connection = mqttClient.callbackConnection();
        
        connection.connect(new Callback<Void>() {
            public void onSuccess(Void v) {
            	logger.debug("connection success----------");
            }

            public void onFailure(Throwable value) {
            	logger.error("connection fail----------", value);
            }
        });
	}
	
	@Override
	public String send(PushMessage pushMsg, boolean user, String... targetId) {
		final MQCallback cb = this.callback;
		
        String topicPrefix = user ? MqConstant.TOPIC_PREFIX_USER : MqConstant.TOPIC_PREFIX_ROBOT;
        for (String id : targetId) {
			connection.publish(topicPrefix + id, JSON.toJSONBytes(pushMsg, SerializerFeature.EMPTY), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
	            public void onSuccess(Void v) {
	            	logger.debug("消息发送成功，" + id + "，消息：" + JSON.toJSONString(pushMsg));
		        	if(cb != null)
		        		cb.deliveryComplete(true, pushMsg.getMessgeId());
	            }
	            public void onFailure(Throwable e) {
	            	logger.error("send异常，" + id + "，消息：" + JSON.toJSONString(pushMsg), e);
	                //connection.disconnect(null);
	            }
	          });
        }

    	try {
			connection.disconnect(null);
		} catch (Exception e) {
			logger.error("disconnect异常", e);
		}
        
        return "";
	}

	@Override
	public void setCallback(MQCallback callback) {
		this.callback = callback;
        
        connection.listener(new Listener() {
			@Override
			public void onConnected() {
				logger.debug("onConnected----------"); 
			}

			@Override
			public void onDisconnected() {
				logger.debug("onDisconnected----------");
				if(callback != null)
					callback.connectionLost(null);
			}

			@Override
			public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
				String topicName = new String(topic.toByteArray());
				String message = new String(body.toByteArray());
				logger.debug("messageArrived----------" + topicName + "---" + message);
				
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
				
				if(msg != null && callback != null) {
					try {
						callback.messageArrived(topicName, msg);
					} catch (Exception e) {
						logger.error("反序列化设备消息异常", e);
					}
				}

				ack.run();
			}

			@Override
			public void onFailure(Throwable value) {
				logger.error("onFailure----------", value);
				//connection.disconnect(null);
				if(callback != null)
					callback.connectionLost(value);
			}
        });
	}
	
	public void subscribe(String... topics) {
		List<Topic> topicList = Lists.newArrayList();
        for (String topic : topics) {
        	topicList.add(new Topic(topic, QoS.AT_LEAST_ONCE));
		}
        connection.subscribe(topicList.toArray(new Topic[0]), new Callback<byte[]>() {
            public void onSuccess(byte[] value) {
            	logger.debug("subscribe-onSuccess ----------");
            }

            public void onFailure(Throwable value) {
            	logger.error("subscribe-disconnect----------", value);
            }
        });
	}
	
	public void unsubscribe(String... topics) {
		List<UTF8Buffer> topicList = Lists.newArrayList();
        for (String topic : topics) {
        	topicList.add(new UTF8Buffer(topic));
		}

		connection.unsubscribe(topicList.toArray(new UTF8Buffer[0]), null);
	}
	
	public static void main(String[] args) {
		FusesourceCallbackClient client = new FusesourceCallbackClient("tcp://192.168.1.234:1883", "test1");
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
		client.subscribe(MqConstant.TOPIC_PREFIX_SERVER + "#");
		//$SYS/brokers/+/clients/#
		
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
