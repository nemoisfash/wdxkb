package cn.hxz.webapp.util.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
 
 
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
 

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.mqtt.MqttAutoConfiguration;



public class MyWsHandler extends TextWebSocketHandler{
	
		private static Map<String, WebSocketSession> clients = new HashMap<>();
		
		private static String clientId;
	
	    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	        Map<String,Object> map = session.getAttributes();
	        clientId = Objects.toString(map.get("clientId"), null);
	        if (clientId != null) {
	        	clients.put(clientId, session);
	        }
	    }
	    
		@Override
	    public void handleTextMessage(WebSocketSession session, TextMessage message) {
	    	 String payload = message.getPayload();
    		if(JSONObject.isValid(payload)) {
    			JSONObject jsonObject =new JSONObject().parseObject(payload);
    			if(jsonObject.containsKey("status") && jsonObject.getInteger("status")==0) {
					MqttAutoConfiguration clientFactory =new MqttAutoConfiguration();
					JSONArray jsonArray = jsonObject.getJSONArray("topices");
					clientFactory.mqttMessageSubClient().subMessage(jsonArray.toJavaList(String.class));
				}
    		}
 	    }
		
		
		 
	    
	    /**
	     * 发送信息给客户端
	     * @param clientId
	     * @param message
	     * @return
	     */
	    public static boolean sendMessageToClient(TextMessage message) {
	        WebSocketSession session = clients.get(clientId);
	        if (!session.isOpen()) {
	            return false;
	        }
	        try {
	            session.sendMessage(message);
	        } catch (IOException e) {
	            return false;
	        }
	        return true;
	    }
	    
	    @Override
	    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	        if (session.isOpen()) {
	            session.close();
	        }
	        System.out.println("[websocket]连接出错");
	        clients.remove(clientId);
	    }
	    
	    @Override
	    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	        System.out.println("[websocket]连接已关闭：" + status);
	        clients.remove(clientId);
	    }

	    @Override
	    public boolean supportsPartialMessages() {
	        return false;
	    }
}
