package cn.hxz.webapp.util.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyCallBack implements MqttCallback {

	@Override
	public void connectionLost(Throwable cause) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		
	}

	public String messageArrived(String topic) {
		MqttMessage message = new MqttMessage();
		try {
			messageArrived(topic, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(message.getPayload());
	}
	
}
