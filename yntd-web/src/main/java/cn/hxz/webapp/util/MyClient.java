package cn.hxz.webapp.util;

import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyClient  {
	public static String msg="";
	public static String HOST = "";  
    private static MqttClient client;  
    private static MqttConnectOptions options;  
    public static String USERNAME = ""; 
    public static String PASSWORD = "";
    @SuppressWarnings("unused")
    private ScheduledExecutorService scheduler;  

    public static void start(String topic ,String clientId) throws MqttException{
    	clientId = clientId + "_dingyue";
        client = new MqttClient(HOST, clientId, new MemoryPersistence());  
        options = new MqttConnectOptions();  
        options.setCleanSession(true);  
        options.setUserName(USERNAME);  
        options.setPassword(PASSWORD.toCharArray());  
        options.setConnectionTimeout(10);  
        options.setKeepAliveInterval(20);  
        client.setCallback(new MqttCallback(){  
            public void connectionLost(Throwable cause) {  
            	
            }  
            public void deliveryComplete(IMqttDeliveryToken token) {  
                System.out.println("deliveryComplete---------" + token.isComplete());  
            }  
			public void messageArrived(String topic, MqttMessage message) throws Exception {  
                try {     
            		if(StringUtils.isNotEmpty(message.toString())) {
            			msg = message.toString();
            		}else {
            			msg="{"+"Message:"+"没有订阅到消息"+"}";
            		}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });  
        client.connect(options);  
        int[] Qos  = {0};  
        String[] topic1 = {topic};
        client.subscribe(topic1, Qos);  
    }
    
    public static void stop() {
    	try {
			client.close();
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
    }
}
