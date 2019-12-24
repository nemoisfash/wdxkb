package cn.hxz.webapp.util.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class WebSocketInterceptor implements HandshakeInterceptor {

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {

	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse res, WebSocketHandler wh,
			Map<String, Object> attributes) throws Exception {
			if(req instanceof ServletServerHttpRequest) {
				ServletServerHttpRequest sreq = (ServletServerHttpRequest)req;
				HttpSession session= sreq.getServletRequest().getSession();
				if (session != null) {
					attributes.put("clientId",session.getId());
	            }
			}
			return true;
	}

}
