package cn.hxz.webapp.util.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;



/**
 * mqtt auto configuration
 */
@Configuration
public class MqttAutoConfiguration {
    
   private static final String MQTT_PROPERTIES_PATH = "mqttconfig/mqttconfig.properties";
   
   private static final String MQTT_HOST ="MQTT_HOST";
   
   private static final String MQTT_USERNAME ="MQTT_USERNAME";
   
   private static final String MQTT_PWD ="MQTT_PWD";
   
   private static final String MQTT_QOS ="MQTT_QOS";
   
   private static final String MQTT_CONNECTION_TIMEOUT ="MQTT_CONNECTION_TIMEOUT";
   
   private static final String MQTT_KEEPALIVE_INTERVA ="MQTT_KEEPALIVE_INTERVA";
   
   private static final String MQTT_CLEANSESSION ="MQTT_CLEANSESSION";
   
   private static final String MQTT_ASYNC ="MQTT_ASYNC";
   
   private static final String MQTT_PUBCLIENTID ="MQTT_PUBCLIENTID";
   
   private static final String MQTT_SUBCLIENTID ="MQTT_SUBCLIENTID";
   
   public DefaultMqttPahoClientFactory clientFactory() {
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        String username = getPropertiesValue(MQTT_USERNAME);
        String password = getPropertiesValue(MQTT_PWD);
        if(username != null) {
            connectOptions.setUserName(username);
        }
        if (password != null) {
            connectOptions.setPassword(password.toCharArray());
        }
        String[] serverURIs =new String[]{getPropertiesValue(MQTT_HOST)};
        if (serverURIs == null || serverURIs.length == 0) {
            throw new NullPointerException("serverURIs can not be null");
        }
        connectOptions.setCleanSession(Boolean.parseBoolean(getPropertiesValue(MQTT_CLEANSESSION)));
        connectOptions.setKeepAliveInterval(Integer.valueOf(getPropertiesValue(MQTT_KEEPALIVE_INTERVA)));
        connectOptions.setServerURIs(serverURIs);
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(connectOptions);
        return factory;
    }
	/*
	 * public MqttMessagePubClient mqttMessagePubClient() { MqttMessagePubClient
	 * client = new MqttMessagePubClient(getPropertiesValue(MQTT_PUBCLIENTID),
	 * clientFactory());
	 * client.setAsync(Boolean.valueOf(getPropertiesValue(MQTT_ASYNC)));
	 * client.setDefaultQos(Integer.valueOf(getPropertiesValue(MQTT_QOS)));
	 * client.setCompletionTimeout(Long.valueOf(getPropertiesValue(
	 * MQTT_CONNECTION_TIMEOUT))); return client; }
	 */
    
    public MqttMessageSubClient mqttMessageSubClient() {
    	MqttMessageSubClient client = new MqttMessageSubClient(getPropertiesValue(MQTT_SUBCLIENTID), clientFactory());
        return client;
    }
    
    
    private String getPropertiesValue(String key) {
    	Properties prop = new Properties();
		InputStream in =MqttAutoConfiguration.class.getClassLoader().getResourceAsStream(MQTT_PROPERTIES_PATH);
		try {
			prop.load(new InputStreamReader(in, "utf-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";

		}
		return prop.getProperty(key);
    }
}
