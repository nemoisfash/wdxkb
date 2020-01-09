package cn.hxz.webapp.util.websocket;

import com.alibaba.fastjson.JSONObject;

public class WsReMessageListener{

	public Object messageHandle(String message) {
		if(JSONObject.isValid(message)) {
			return null;
		}
		JSONObject jsonObject =new JSONObject().parseObject(message);
		
		return messageHandle(jsonObject);
	};
	
	private Object messageHandle(JSONObject jsonObject) {
		if(!jsonObject.containsKey("status")) {
			return jsonObject;
		}
		if(jsonObject.getInteger("status")==0) {
			return produceTopics(jsonObject);
		}
		
		return jsonObject;
	}

	private Object produceTopics(JSONObject jsonObject) {
		
		return jsonObject;
	};

}
