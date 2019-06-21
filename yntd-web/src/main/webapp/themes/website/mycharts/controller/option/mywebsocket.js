/**
 * 
 */
;(function(w) {
	var option={
		_url:"wss",
	}
	
	var Mywebsocket = function(url){
		option._url=url;
		this.init();
	}
	
	Mywebsocket.prototype={
		init:function(){
			var ws = new WebSocket(option._url);
			this.ws=ws;
			this.event();
		},event:function(){
			var _this=this;
			var _ws = _this.ws;
			_ws.onopen=function(event){
				var heartCheck= _this.heartCheck();
				heartCheck.reset().start();
				console.info("与socket服务器链接成功");
					_ws.send("data");
			 }
			
			_ws.onerror = function(evnt) {
				_this.reconnect();
	             console.info("与socket服务器链接出错");
	         };
	         
	         _ws.onclose = function(evnt) {
	        	 _this.reconnect();
				console.info("与socket服务器断开链接");
	         }
	         _ws.onmessage = function(evnt) {
	        	var heartCheck= _this.heartCheck();
				heartCheck.reset().start(); 
	        	 if(event.data!="pong"){
	        		 console.info(event.data);
	        		 _this.retrunData(event.data);
	        	 }
	         };
	         
	         w.onbeforeunload = function(event) {
	        	 _ws.close();
			 }
			
		},heartCheck:function(){
			var _this=this;
			var heartCheck={
				timeout:54000,
				timeoutObj: null,
				serverTimeoutObj: null,
				reset:function(){
					clearTimeout(this.timeoutObj);
			        clearTimeout(this.serverTimeoutObj);
			        return this;
				},start:function(){
					 	var self = this;
				        this.timeoutObj = setTimeout(function(){
				        	_this.ws.send("ping");
				            self.serverTimeoutObj = setTimeout(function(){ 
				            	_this.ws.close();     
				            }, self.timeout)
				     }, this.timeout)
				}
		  
		  }
			return heartCheck;
		},reconnect:function(){
			var _this=this;
			var lockReconnect= false;
			if(lockReconnect) return;
		    lockReconnect = true;
		    setTimeout(function () {//没连接上会一直重连，设置延迟避免请求过多
		    	_this.init();
		        lockReconnect = false;
		    }, 5000);
		},retrunData:function(){
			 var obj = JSON.parse(event.data);
			 return obj;
		}
	}
w.Mywebsocket = Mywebsocket;
})(window)
			
