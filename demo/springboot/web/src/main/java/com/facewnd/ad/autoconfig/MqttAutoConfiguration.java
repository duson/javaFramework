package com.facewnd.ad.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.facewnd.ad.common.config.PropertyConfig;

@Configuration
public class MqttAutoConfiguration {
	
	@Autowired
	private PropertyConfig config;

	@Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(config.getMqttServer());
        factory.setKeepAliveInterval(120);
        //factory.setUserName("username");
        //factory.setPassword("password");
        return factory;
    }
	
	// publisher

	@Bean
	public IntegrationFlow mqttOutFlow() {
		return IntegrationFlows
				.from(mqttOutboundChannel())
				.transform(p -> p + " sent to MQTT")
				.handle(mqttOutbound())
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("ad-monitor", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultQos(1);
		messageHandler.setBeanName("mqtt");
		//messageHandler.setDefaultTopic("");
		return messageHandler;
	}
	
	@Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
	
	/*@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MyGateway {
        void sendToMqtt(String data);
    }*/

	// consumer
		
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public IntegrationFlow mqttInFlow() {
	    return IntegrationFlows.from(mqttInbound())
	            .transform(p -> p + ", received from MQTT")
	            //.handle(handler()) // 消息处理，可指定其它类的方法
	            .get();
	}
	
    @Bean
    public MessageProducerSupport mqttInbound() {
    	MqttPahoMessageDrivenChannelAdapter adapter =
    			new MqttPahoMessageDrivenChannelAdapter(config.getMqttServer(), "ad-monitor", 
    					"$SYS/brokers/+/clients/#");
    	adapter.setCompletionTimeout(5000);
    	adapter.setConverter(new DefaultPahoMessageConverter());
    	adapter.setQos(1);
    	adapter.setOutputChannel(mqttInputChannel());
    	return adapter;
    }
    
   /* @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
    	return new MessageHandler() {
    		@Override
    		public void handleMessage(Message<?> message) throws MessagingException {
    			
    		}
    	};
    }*/
    
}
