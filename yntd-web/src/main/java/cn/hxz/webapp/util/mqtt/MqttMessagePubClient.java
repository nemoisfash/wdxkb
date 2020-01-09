package cn.hxz.webapp.util.mqtt;

import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class MqttMessagePubClient extends MqttPahoMessageHandler {

    public MqttMessagePubClient(String clientId, MqttPahoClientFactory clientFactory) {
        super(clientId, clientFactory);
    }

    /**
     * send mqtt message
     *
     * @param topic   mqtt topic
     * @param content mqtt payload
     */
    public void sendMessage(String topic, String content) {
        Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
        this.handleMessage(messages);
    }
    
}
