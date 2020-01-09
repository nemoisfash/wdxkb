package cn.hxz.webapp.util.mqtt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.messaging.MessagingException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.websocket.MyWsHandler;
public class MqttMessageSubClient implements MqttCallback{
	
	private  MqttPahoClientFactory clientFactory;
	
	private  String clientId;
	
	private  IMqttAsyncClient client;
	
	private  MqttConnectOptions connectionOptions;
	
	public  String content;

	private  Map<String, Object> preRecord = new HashMap<String, Object>();
	
	public static final long DEFAULT_COMPLETION_TIMEOUT = 30000L;

	public MqttMessageSubClient(String clientId, MqttPahoClientFactory clientFactory) {
			this.clientFactory = clientFactory;
			this.clientId = clientId;
			this.connectionOptions =clientFactory.getConnectionOptions();
	}
	
	private void subMessage(String[]topics, int[] qos) {
		try {
			checkConnection();
			client.subscribe(topics, qos);
			client.setCallback(new MqttCallback() {
				@SuppressWarnings("static-access")
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					String msg = new String(message.getPayload());
					JSONObject jsonObject = new JSONObject();
					Map<String, Object> map =new HashMap<String, Object>();
					map.put("machineName", topic);
					if(new JSONObject().isValid(msg)) {
						jsonObject = (JSONObject) new JSONObject().parse(msg);
						if(jsonObject.containsKey("limo")) {
							map.put("success", false);
							map.put("machineSignal", "POWEROFF");
						}else {
							map.put("success", true);
							map.put("content", jsonObject);
						}
					}else {
						map.put("success", false);
						map.put("message", msg);
						map.put("machineSignal", "POWEROFF");
					}
					String messageJson = new JSONObject(map).toJSONString();
					preRecord.put(topic, messageJson);
					TextMessage textMessage = new TextMessage(messageJson);
					MyWsHandler.sendMessageToClient(textMessage);
				}
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					
				}
				
				@Override
				public synchronized void connectionLost(Throwable cause) {
					try {
						checkConnection();
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (MqttException e) {
			System.out.print(e.getMessage());
			TextMessage textMessage = new TextMessage(new JSONObject(preRecord).toJSONString());
			MyWsHandler.sendMessageToClient(textMessage);
		}
	}
	
	private synchronized IMqttAsyncClient checkConnection() throws MqttException {
		if (this.client != null && !this.client.isConnected()) {
			this.client.setCallback(null);
			this.client.close();
			this.client = null;
		}
		if (this.client == null) {
			try {
				MqttConnectOptions connectionOptions =this.connectionOptions;
				this.client = this.clientFactory.getAsyncClientInstance(connectionOptions.getServerURIs()[0], this.clientId);
				this.client.setCallback(this);
				this.client.connect(connectionOptions).waitForCompletion(DEFAULT_COMPLETION_TIMEOUT);
			}
			catch (MqttException e) {
				if (this.client != null) {
					this.client.close();
					this.client = null;
				}
				throw new MessagingException("Failed to connect", e);
			}
		}
		return this.client;
		 
	}
	 
	public Boolean subMessage(List<String> topics) {
		String [] topicLiString = new String [topics.size()];
		String [] topices= topics.toArray(topicLiString);
		int[] qos = new int[topices.length];
		for (int i=0; i<qos.length; i++) {
			qos[i] = 2;
		}
		 subMessage(topices,  qos);
		 return true;
	}

	@Override
	public void connectionLost(Throwable cause) {
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	};
}

