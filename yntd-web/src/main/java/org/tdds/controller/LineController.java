package org.tdds.controller;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.tdds.service.LogRecordService;
import org.tdds.service.MachineService;



@ServerEndpoint(value = "/line/{node}")
public class LineController {
	
	@Autowired
	private MachineService bizMachine;

	@Autowired
	private LogRecordService bizLogRecord;

	private static int onlineCount = 0;

	private String currentUser;

	/**
	 * 连接建立成功调用的方法
	 * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "user") String user,Session session) {
		 currentUser=user;
		onMessage("asdasda",session);
		System.out.println("Connected ... " + session.getId());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
    public String onMessage(String message, Session session) {
        System.out.println(currentUser + "：" + message);
        return currentUser + "：" + message;
    }
	
	//连接关闭时执行
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
    
  //连接错误时执行
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

}
