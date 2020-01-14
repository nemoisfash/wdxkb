package cn.hxz.webapp.util.mqtt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.messaging.MessagingException;

import cn.hxz.webapp.util.ExcelExportUtil;

public class MqttMessagePubClient{

	
	   private static final String MQTT_PROPERTIES_PATH = "mqttconfig/mqttconfig.properties";
	   
	   private static final String MQTT_BROKER ="MQTT_HOST";
	   
	   private static final String MQTT_USERNAME ="MQTT_USERNAME";
	   
	   private static final String MQTT_PWD ="MQTT_PWD";
	   
	   private static final String MQTT_QOS ="MQTT_QOS";
	   
	   private static final String MQTT_CONNECTION_TIMEOUT ="MQTT_CONNECTION_TIMEOUT";
	   
	   private static final String MQTT_KEEPALIVE_INTERVA ="MQTT_KEEPALIVE_INTERVA";
	   
	   private static final String MQTT_CLEANSESSION ="MQTT_CLEANSESSION";
	   
	   private static final String MQTT_ASYNC ="MQTT_ASYNC";
	   
	   private static final String MQTT_PUBCLIENTID ="MQTT_PUBCLIENTID";
	   
	   private MqttClient client;
	
	public MqttMessagePubClient() {
	try {
			MqttClient client = new MqttClient(getPropertiesValue(MQTT_BROKER),getPropertiesValue(MQTT_PUBCLIENTID));
			this.client = client;
		} catch (MqttException e) {
			e.printStackTrace();
		}
	} 
	
	  private String getPropertiesValue(String key) {
	    	Properties prop = new Properties();
			InputStream in = ExcelExportUtil.class.getClassLoader().getResourceAsStream(MQTT_PROPERTIES_PATH);
			try {
				prop.load(new InputStreamReader(in, "utf-8"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return "";

			}
			return prop.getProperty(key);
	    }
	
    synchronized private MqttClient checkConnection() throws MqttException {
		if(this.client!=null && !this.client.isConnected()){
			this.client.close();
			this.client = null;
		}
		
		 if(this.client==null) {
			try {
					this.client = new MqttClient(getPropertiesValue(MQTT_BROKER),getPropertiesValue(MQTT_PUBCLIENTID));
					MqttConnectOptions connectionOptions = new MqttConnectOptions();
					connectionOptions.setUserName(getPropertiesValue(MQTT_USERNAME));
					connectionOptions.setPassword(getPropertiesValue(MQTT_PWD).toCharArray());
					connectionOptions.setConnectionTimeout(Integer.valueOf(getPropertiesValue(MQTT_CONNECTION_TIMEOUT)));
					connectionOptions.setCleanSession(Boolean.valueOf(getPropertiesValue(MQTT_CLEANSESSION)));
					connectionOptions.setKeepAliveInterval(Integer.valueOf(getPropertiesValue(MQTT_KEEPALIVE_INTERVA)));
					connectionOptions.setServerURIs(new String[]{getPropertiesValue(MQTT_BROKER)});
					this.client.connect(connectionOptions);
				}catch (Exception e) {
					if (this.client != null) {
						this.client.close();
						this.client = null;
					}
					throw new MessagingException("Failed to connect", e);
				}
		 }
		 	return this.client;
		 
	}
	
    /**
     * send mqtt message
     * @param topic   mqtt topic
     * @param content mqtt payload
     */
    public void sendMessage(String topic, String content) {
    	MqttMessage mmsMessage  = new MqttMessage();
    	mmsMessage.setPayload(content.getBytes());
    	try {
			checkConnection();
			this.client.publish(topic, mmsMessage);
		} catch (MqttException e) {
			e.printStackTrace();
		}
         
    }
    
}
