package cn.hxz.webapp.util;


/**
 * Created by Administrator on 17-2-10.
 */

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * Title:Server Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * 
 * @author admin 2017年2月10日下午17:41:10
 */
public class MqttSendData{
	private final String CLIENT_ID = "wdx_1";
	public final String BROKER = "tcp://120.253.42.243:1883";
	private final String USERNAME = "admin";
	private final String PASSWORD = "public";
	public final String TOPIC = "iot/wdx_1";
	private static String content;
	private MqttClient client;
	private MqttTopic topic;
	private MqttMessage message;
	
	private static MqttSendData mqttSendData = new MqttSendData();

	private MqttSendData() {
		try {
			client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
		} catch (MqttException e) {
			e.printStackTrace();
		}
		connect();
	}

	private void connect() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setUserName(USERNAME);
		options.setPassword(PASSWORD.toCharArray());
		// 设置超时时间
		options.setConnectionTimeout(10);
		// 设置会话心跳时间
		options.setKeepAliveInterval(20);
		topic = client.getTopic(TOPIC);
//		// setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//		options.setWill(topic, (CLIENT_ID+"已挂").getBytes(), 2, true);
		try {
			client.setCallback(new MyCallback(CLIENT_ID));
			client.connect(options);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println("message is published completely! " + token.isComplete());
	}

	public static void uploadDataBYMqttData(String data) {
		mqttSendData.message = new MqttMessage();
		mqttSendData.message.setQos(2);
		mqttSendData.message.setRetained(false);
		mqttSendData.message.setPayload(data.getBytes());
		try {
			mqttSendData.publish(mqttSendData.topic, mqttSendData.message);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}
