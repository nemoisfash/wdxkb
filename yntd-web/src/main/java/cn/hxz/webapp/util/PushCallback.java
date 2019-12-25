package cn.hxz.webapp.util;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PushCallback implements MqttCallback {
	 
	public static String MESSAGE;

	 @Override
    public void connectionLost(Throwable cause) {
        System.out.println("连接断开，可以做重连");
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        MESSAGE=new String(message.getPayload());
    }
    
}