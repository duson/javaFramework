package com.facewnd.ad.modules.communication;

import java.sql.Timestamp;
import java.util.Date;

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
import com.facewnd.ad.modules.device.model.device.DeviceInfoForOnlineStat;
import com.facewnd.ad.modules.device.service.DeviceService;

@Component
public class MqttMonitor {
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private DeviceService deviceService;
	
	private static final Logger logger = LoggerFactory.getLogger(MqttMonitor.class);
	
	private static final String REDIS_KEY_DEVICEID_COMPANYID_MAP = "deviceCompanyMap";

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
    			
    			// clientId: facewnd-device-{deviceId}
    			String[] clientPart = clientId.split("-");
    			if(clientPart.length != 3) return;
    			if(clientPart[1].equals("device")) { // 终端设备
    				String deviceIdString = clientPart[2];
    				Long deviceId = Long.valueOf(deviceIdString);
    				Long companyId = getCompanyIdByDeviceId(deviceId);
    				if(companyId == null) return;
        			
    				String redisKey = DeviceService.REDIS_KEY_DEVICE_ONLINE + "." + companyId;
        			if(topic.endsWith("/connected")) { // 连接
        				redisTemplate.opsForValue().setBit(redisKey, deviceId, true);
        				
        				deviceService.updateDeviceOnlineStatus(true, deviceId);
        			} else if(topic.endsWith("/disconnected")) { // 断开 
        				redisTemplate.opsForValue().setBit(redisKey, deviceId, false);
        				deviceService.updateDeviceOnlineStatus(false, deviceId);
        			}
    			}
    		}
    	};
    }
    
    private Long getCompanyIdByDeviceId(Long deviceId) {
    	if(!redisTemplate.opsForHash().hasKey(REDIS_KEY_DEVICEID_COMPANYID_MAP, deviceId.toString())) {
    		DeviceInfoForOnlineStat deviceInfo = deviceService.getDeviceInfoForOnlineStat(deviceId);
	    	redisTemplate.opsForHash().put(REDIS_KEY_DEVICEID_COMPANYID_MAP, deviceId.toString(), deviceInfo.getCompanyId().toString());
	    	return deviceInfo.getCompanyId();
    	}
    	
    	Long companyId = Long.valueOf(redisTemplate.opsForHash().get(REDIS_KEY_DEVICEID_COMPANYID_MAP, deviceId.toString()).toString());
    	
    	return companyId;
    }
}
