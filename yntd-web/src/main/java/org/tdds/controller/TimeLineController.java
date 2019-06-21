/*package org.tdds.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import org.tdds.entity.Machine;
import org.tdds.service.LogRecordService;
import org.tdds.service.MachineService;
import org.tdds.service.MonitoringService;

import com.alibaba.fastjson.JSONObject;

import cn.hxz.webapp.util.echarts.EchartsCreater;

@ServerEndpoint(value = "/timeLine/{node}",configurator = SpringConfigurator.class)
public class TimeLineController {
	
	private String currentCharts;
	
	@Autowired
	private MonitoringService bizMonitoring;
	
	@Autowired
	private LogRecordService bizLogRecord;
	
	private List<Map<String, Object>> timeLineOption = new ArrayList<>();
	
	private static List<String> machineNames = new ArrayList<>();
	
	@Autowired
	private MachineService bizMachine;
	
    private Session session;
    
	private static Map<String, Object> mySocket = new HashMap<>();
	
	private static final String[] STATUS = {"POWEROFF", "WARNING", "WAITING" };
    
	@OnOpen
	public void onOpen(@PathParam(value = "node") String node,Session session) {
		 this.currentCharts=node;
		 this.session=session;
			mySocket.put(node, this);
			machineNames.clear();
			List<Long> ids = bizMachine.findMachineids();
			for(Long id:ids){
					machineNames.add(bizMachine.findMachineNames(id));
			}
		System.out.println("Connected ... " + session.getId());
	}
	
	*//**
	 * 收到客户端消息后调用的方法
	 * @param time 客户端发送过来的消息
	 * @param session 可选的参数
	 * @throws IOException 
	 *//*
	@OnMessage
    public void onMessage(String message, Session session) throws IOException {
	
		if(message.equals("ping")) {
			sendData("pong");
		}else if(message.equals("data")){
			Timer timer=new Timer();
			timer.schedule(new TimerTask() {
				@Override
	            public void run() {
					try {
						sendData(createData().toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
			},1000,3000);
 			
		}
    }
	
	@ResponseBody
	private Object createData() {
		EchartsCreater.BAR_XAXIS_DATA.clear();
		EchartsCreater.BAR_SERIES_DATA.clear();
		List<Long> ids = bizMachine.findMachineids();
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String, Object> map = null;
		for (String str : STATUS) {
			 map = new HashMap<>();
			List<Double> num = new LinkedList();
			for(Long id:ids){
				num.add(bizLogRecord.selectData(id,str));
			}
			map.put(str, num);
			list.add(mapToJson(map));
		}
			Map<String, Object> names= new HashMap<>();
			names.put("name", machineNames);
			list.add(mapToJson(names));
		return list;
	}

	private JSONObject mapToJson(Map<String, Object> map){
		JSONObject json =new JSONObject(map);
		return json;
	}
	
	//连接关闭时执行
    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws IOException {
    	
        System.out.println(String.format("Session %s closed because of %s", session.getId(), closeReason));
        session.close(); 
    }
    
    //连接错误时执行
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
    
    @ResponseBody
	public Object createOptions(String time) {
		return timeLineOption;
	}
    
    public void sendData(String data) throws IOException {
    	if(session.isOpen()){
    		this.session.getAsyncRemote().sendText(data);
    	}
    }
    
}
*/