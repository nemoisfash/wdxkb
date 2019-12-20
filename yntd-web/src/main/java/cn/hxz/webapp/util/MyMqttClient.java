package cn.hxz.webapp.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyMqttClient {
	private static String HOST = "MQTT_HOST";
	private static String USERNAME = "MQTT_USERNAME";
	private static String PASSWORD = "MQTT_PWD";
	private static String CONNECTION_TIMEOUT = "MQTT_CONNECTION_TIMEOUT";
	private static String KEEPALIVE_INTERVAL = "MQTT_KEEPALIVE_INTERVA";
	private static String QOS = "MQTT_QOS";
	private static final String CONFG_FILE = "mqttconfig/mqttconfig.properties";

	private static MqttClient client;
	private static MqttMessage mqttMessage;
	private static MqttTopic mqttTopic;
	@SuppressWarnings("unused")
	private ScheduledExecutorService scheduler;

	private static void initClient(String topic, String clientId) throws MqttException {
		client = new MqttClient(getValue(HOST), clientId, new MemoryPersistence());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setUserName(getValue(USERNAME));
		options.setPassword(getValue(PASSWORD).toCharArray());
		options.setConnectionTimeout(Integer.valueOf(getValue(CONNECTION_TIMEOUT)));
		options.setKeepAliveInterval(Integer.valueOf(getValue(KEEPALIVE_INTERVAL)));
		try {
			client.connect(options);
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static String subscribe(String topic, String clientId) {
		try {
			if (client == null||!client.isConnected()) {
				initClient(topic, clientId);
			}
		} catch (MqttException e1) {
			System.out.println(e1.getMessage());
		}
		try {
			client.subscribe(topic, Integer.valueOf(getValue(QOS)));
		} catch (MqttException e) {
			e.printStackTrace();
		}
		client.setCallback(new PushCallback());
		return PushCallback.MESSAGE;
	}

	public static void publish(String topic, String clientId, String data) {
		try {
			if (client == null||!client.isConnected()) {
				initClient(topic, clientId);
			}
			mqttMessage = new MqttMessage();
			mqttMessage.setQos(Integer.valueOf(getValue(QOS)));
			mqttMessage.setRetained(false);
			mqttMessage.setPayload(data.getBytes());
			mqttTopic = client.getTopic(topic);
			mqttTopic.publish(mqttMessage).waitForCompletion();
		} catch (MqttException e1) {
			e1.printStackTrace();
		}
	}

	private static String getValue(String key) {
		Properties prop = new Properties();
		InputStream in = ExcelExportUtil.class.getClassLoader().getResourceAsStream(CONFG_FILE);
		try {
			prop.load(new InputStreamReader(in, "utf-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
		return prop.getProperty(key);
	}

}
