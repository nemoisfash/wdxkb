package cn.hxz.webapp.util.mqtt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.messaging.MessagingException;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.websocket.MyWsHandler;
public class MqttMessageSubClient implements MqttCallback{
	
	private  MqttPahoClientFactory clientFactory;
	
	private  String clientId;
	
	private  MqttClient client;
	
	private  MqttConnectOptions connectionOptions;
	
	public  String content;

	private  Map<String, Object> offlinemessage = new HashMap<String, Object>();
	
	public static final long DEFAULT_COMPLETION_TIMEOUT = 30000L;
	
	private String []topices ;
	
	private int []qos;

	public MqttMessageSubClient(String clientId, MqttPahoClientFactory clientFactory) {
			this.clientFactory = clientFactory;
			this.clientId = clientId;
			this.connectionOptions =clientFactory.getConnectionOptions();
	}
	
	private void subMessage() throws MqttException {
			try {
				checkConnection();
			} catch (Exception e) {
				// TODO: handle exception
			}
			client.setTimeToWait(5000);
			final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
			            new LinkedBlockingQueue<Runnable>());
			client.subscribe(topices, qos);
			client.setCallback(new MqttCallbackExtended() {
				@SuppressWarnings("static-access")
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					String msg = new String(message.getPayload());
					if(JSONObject.isValid(msg)){
						if(msg.indexOf("limo")>-1) {
							Map<String, Object> limoMap = new HashMap<String, Object>();
							limoMap.put("code",topic.split("/")[1]);
							limoMap.put("machineSignal","POWEROFF");
							limoMap.put("machineSignalZH","关机");
							TextMessage textMessage = new TextMessage(new JSONObject(limoMap).toJSONString());
							MyWsHandler.sendMessageToClient(textMessage);
						}else {
							TextMessage textMessage = new TextMessage(msg);
							MyWsHandler.sendMessageToClient(textMessage);
						}
					}else {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("code",topic.split("/")[1]);
						map.put("machineSignal","POWEROFF");
						map.put("machineSignalZH","关机");
						TextMessage textMessage = new TextMessage(new JSONObject(map).toJSONString());
						MyWsHandler.sendMessageToClient(textMessage);
					}
					
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
				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					 /**
	                 * 客户端连接成功后就需要尽快订阅需要的 topic
	                 */
	                executorService.submit(new Runnable() {
	                    @Override
	                    public void run() {
	                        try {
	                        	client.subscribe(topices, qos);
	                        } catch (MqttException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                });
				}
			});
	}
	
	private synchronized MqttClient checkConnection() throws MqttException {
		if (this.client != null && !this.client.isConnected()) {
			this.client.setCallback(null);
			this.client.close();
			this.client = null;
		}
		if (this.client == null) {
			try {
				MqttConnectOptions connectionOptions =this.connectionOptions;
				final MemoryPersistence memoryPersistence = new MemoryPersistence();
				this.client = new MqttClient(connectionOptions.getServerURIs()[0],clientId,memoryPersistence);
				this.client.setCallback(this);
				this.client.setTimeToWait(5000);
				this.client.connect(connectionOptions);
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
		this.topices=topics.toArray(topicLiString);
		int[] qos = new int[topices.length];
		for (int i=0; i<qos.length; i++) {
			qos[i] = 2;
		}
			this.qos=qos;
		 try {
			subMessage();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		 return true;
	}

	@Override
	public void connectionLost(Throwable cause) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		
	};
}

