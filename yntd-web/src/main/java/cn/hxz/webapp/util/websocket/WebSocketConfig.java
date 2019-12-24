package cn.hxz.webapp.util.websocket;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		 registry.addHandler(myHandler(), "/ws").addInterceptors(new HttpSessionHandshakeInterceptor());
	     registry.addHandler(myHandler(), "/sockjs").addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
 	}

	private WebSocketHandler myHandler() {
		return new MyWsHandler();
	}
}
