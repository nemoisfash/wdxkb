package cn.hxz.webapp.util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

public class MqttSubscribe {

	@Autowired
	private MqttPahoMessageHandler mqttHandler;

	@Autowired
	private DefaultMqttPahoClientFactory clientFactory;

	
	private MqttClient client;

	public MqttPahoMessageHandler getMqttHandler() {
		return mqttHandler;
	}

	public void setMqttHandler(MqttPahoMessageHandler mqttHandler) {
		this.mqttHandler = mqttHandler;
	}

	public DefaultMqttPahoClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setClientFactory(DefaultMqttPahoClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
 
	public String subscribe(String topic,String clientId) {
		 try {
			client = clientFactory.getClientInstance("tcp://120.253.42.243:1883", clientId);
		} catch (MqttException e) {
			e.printStackTrace();
		}
		client.setCallback(new PushCallback());
		return PushCallback.MESSAGE;
	}

}
