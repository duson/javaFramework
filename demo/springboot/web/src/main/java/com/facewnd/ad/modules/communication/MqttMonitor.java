package com.facewnd.ad.modules.communication;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Component
public class MqttMonitor {
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(MqttMonitor.class);
	
	public static final String REDIS_KEY_DEVICE_ONLINE = "online";
	private static final String REDIS_KEY_MACHINECODE_ID_MAP = "machineCode";

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
    	return new MessageHandler() {
    		@Override
    		public void handleMessage(Message<?> message) throws MessagingException {
    			logger.debug("message：" + message.getHeaders().toString() + "-----" + message.getPayload());
    			
    			String topic = message.getHeaders().get("mqtt_topic").toString();
    			
    			JSONObject jsonObj = JSON.parseObject(message.getPayload().toString());
    			if(!jsonObj.containsKey("clientid")) return;
    			String clientId = jsonObj.getString("clientid");
    			
    			Timestamp ts = jsonObj.getTimestamp("ts");
    			Date date = new Date(ts.getTime());
    			
    			String[] clientPart = clientId.split("-");
    			if(clientPart.length != 4) return;
    			if(clientPart[2].equals("device")) { // 终端设备
    				String deviceMachineCode = clientPart[3];
    				Long deviceId = getDeviceId(deviceMachineCode);
    				if(deviceId == null) return;
        			
        			if(topic.endsWith("/connected")) { // 连接
        				redisTemplate.opsForValue().setBit(REDIS_KEY_DEVICE_ONLINE, deviceId, true);
        			} else if(topic.endsWith("/disconnected")) { // 断开 
        				redisTemplate.opsForValue().setBit(REDIS_KEY_DEVICE_ONLINE, deviceId, false);
        			}
    			}
    		}
    	};
    }
    
    private Long getDeviceId(String machineCode) {
    	if(!redisTemplate.opsForHash().hasKey(REDIS_KEY_MACHINECODE_ID_MAP, machineCode)) {
	    	
	    	Long deviceId = 0L;
	    	redisTemplate.opsForHash().put(REDIS_KEY_MACHINECODE_ID_MAP, machineCode, String.valueOf(deviceId));
	    	return deviceId;
    	}
    	
    	return Long.valueOf(redisTemplate.opsForHash().get(REDIS_KEY_MACHINECODE_ID_MAP, machineCode).toString());
    }
}
