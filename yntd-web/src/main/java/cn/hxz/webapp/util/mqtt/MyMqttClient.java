package cn.hxz.webapp.util.mqtt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.web.socket.TextMessage;

import cn.hxz.webapp.util.websocket.MyWsHandler;

public class MyMqttClient {

	private String clientId;

	private MqttClient client;

	public String content;

	public String curtopic;

	public int curQos;

	public boolean isSend;

	private static final String confgFile = "mqttconfig/mqttconfig.properties";

	private static final String MQTT_HOST = "MQTT_HOST";

	private static final String MQTT_USERNAME = "MQTT_USERNAME";
	private static final String MQTT_PWD = "MQTT_PWD";
	private static final String MQTT_CONNECTION_TIMEOUT = "MQTT_CONNECTION_TIMEOUT";
	private static final String MQTT_KEEPALIVE_INTERVA = "MQTT_KEEPALIVE_INTERVA";

	public MyMqttClient(String clientId, boolean isSend) {
		this.clientId = clientId;
		this.isSend = isSend;
	}

	private void initClientConnected() {
		try {
			MqttConnectOptions options = new MqttConnectOptions();
			String host = getValue(MQTT_HOST);
			options.setUserName(getValue(MQTT_USERNAME));
			options.setPassword(getValue(MQTT_PWD).toCharArray());
			options.setKeepAliveInterval(Integer.valueOf(getValue(MQTT_KEEPALIVE_INTERVA)));
			options.setConnectionTimeout(Integer.valueOf(getValue(MQTT_CONNECTION_TIMEOUT)));
			options.setCleanSession(false);
			options.setAutomaticReconnect(true);
			client = new MqttClient(host, clientId);
			client.connect(options);
			client.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable cause) {
					try {
						client.reconnect();
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {

				}

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {

				}

			});
		} catch (MqttException e) {
			System.out.println("链接失败");
			System.out.println(e.getMessage().toString());
			e.printStackTrace();
		}

	}

	private static String getValue(String key) {
		Properties prop = new Properties();
		InputStream in = MyMqttClient.class.getClassLoader().getResourceAsStream(confgFile);
		try {
			prop.load(new InputStreamReader(in, "utf-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";

		}
		return prop.getProperty(key);
	}

	@SuppressWarnings("unused")
	public void sub(String topic, int qos) {
		if (client == null || !client.isConnected()) {
			initClientConnected();
		}
		try {
			client.subscribe(topic, qos, new IMqttMessageListener() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					String data = null;
					if (message.getPayload().length>0) {
						data = new String(message.getPayload());
						content = new String(data);
						if (isSend) {
							TextMessage tMsg = new TextMessage(content);
							MyWsHandler.sendMessageToClient(tMsg);
						}
					}
					outPutMqttData(data);
				}

			});
		} catch (MqttException e) {
			System.out.println(e.getMessage().toString());
		}
	}

	public String outPutMqttData(String jsonData) {
		return jsonData;
	}

	public void pub(String topic, String payload, int qos, boolean retain) throws Exception {
		if (client == null || !client.isConnected()) {
			initClientConnected();
		}
		try {
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload(payload.getBytes());
			mqttMessage.setQos(qos);
			mqttMessage.setRetained(retain);
			client.publish(topic, mqttMessage);
		} catch (MqttException e) {
			throw new Exception("推送失败 topic: " + topic);
		}
	}

	public void sub(String[] topics, int[] qos) {
		for (int i = 0; i < topics.length; i++) {
			try {
				sub(topics[i], qos[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
