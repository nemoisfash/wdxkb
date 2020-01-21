var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$timeout,$interval) {
getTopices()
function getTopices(){
	$.ajax({
		method:"get",
		url:"/member/getAllTopics.json",
		async:false,
		dataType:"json",
		contentType:"application/json",
		success:function(data){
			var chatsTopices = ["pies", "ranking", "timeLine"];
			localStorage.setItem("chatsTopic",chatsTopices.join("&"));
			var machineTopices = data.topices;
			localStorage.setItem("machineTopices",machineTopices.join("&"));
			var machineNames =data.machineNames;
			localStorage.setItem("machineNames",machineNames.join("&"));
			$scope.topices=machineTopices.concat(chatsTopices);
			$scope.topicesLength=$scope.topices.length;
			connection()
		}
		
	})
}	
 
var msgFactory={"products":[],"isFull":false,"capacity":$scope.topicesLength};
function connection(){
	var websocket;
	var host = window.location.host;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://"+host+"/ws.html");
    }else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://"+host+"/ws.html");
    }else {
        websocket = new SockJS("http://"+host+"/ws/socketjs.html");
    }
    websocket.onError=function(){
    	connection();
    };
    websocket.onClose=function(){
    	console.info("链接已关闭");
    };
    
    websocket.onmessage=function(evn){
    	var jsonData= JSON.parse(evn.data);
    	if(!msgFactory.isFull){
    		msgFactoryCreate(jsonData);
    	}
    };
    
    websocket.onopen= function(event) {
    	console.info("通道已建立");
    	websocket.send(JSON.stringify({"status":0,"isFinished":false,"topices":$scope.topices}));
    }
}

function msgFactoryCreate(obj){
	if(msgFactory.products.length<=msgFactory.capacity){
	   msgFactory.products.push(obj);
	}else{
	   msgFactory.isFull=true;
	   $.each(msgFactory.products,function(i,e){
		   clearData(jo)
		   msgFactory.products.remove(i);
	   })
   		if(msgFactory.products.length==0){
   			msgFactory.isFull=false;
   		}
	}
	return;
}

function clearData(jo){
			if (jo["co1"] && jo["co1"]=="1") {
				jo["cnc_mode"]="快速移动状态";
			}
		
			if (jo["co2"] && jo["co2"]=="1") {
				jo["cnc_mode"]="直线切削状态";
			}
		
			if (jo["co3"] && jo["co3"]=="1") {
				jo["cnc_mode"]="顺时针圆弧状态";
			}
			
			if (jo["co4"] && jo["co4"]=="1") {
				jo["cnc_mode"]="逆时针圆弧状态";
			}
			
			if (jo["rz"] && jo["rz"]=="1") {
				jo["machineSignal"]="RUNNING";
				jo["machineSignalZH"]="运行";
			}
		
			if (jo["wz"] && jo["wz"]=="1") {
				jo["machineSignal"]="WAITING";
				jo["machineSignalZH"]="等待";
			}
		
		if (jo["az"] && jo["az"]=="1") {
			jo["machineSignal"]="ALARM";
			jo["machineSignalZH"]="报警";
		}
		if(jo["device_state"]) {
			if (jo["device_state"]=="0") {
				jo["machineSignal"]="RUNNING";
				jo["machineSignalZH"]="运行";
					if (jo["cnc_runstatus"]) {
						if (jo["cnc_runstatus"]=="0") {
							jo["cnc_runstatus"]= "RESET";
						}
						if (jo["cnc_runstatus"]=="1") {
							jo["cnc_runstatus"]="STOP";
						}
						if (jo["cnc_runstatus"]=="2") {
							jo["cnc_runstatus"]="HOLD";
						}
						if (jo["cnc_runstatus"]=="3") {
							jo["cnc_runstatus"]="START";
						}
						if (jo["cnc_runstatus"]=="4") {
							jo["cnc_runstatus"]="MSTR";
						}
						if(jo["cnc_runstatus"]=="5") {
							jo["cnc_runstatus"]="Other";
						}
					}
			}
			if(jo["device_state"]=="1") {
				jo["machineSignal"]="POWEROFF";
				jo["machineSignalZH"]="关机";
			}
		}
		if (jo["cnc_alarm"]) {
			if(jo["cnc_alarm"]!="[]"){
				jo["machineSignal"]="ALARM";
				jo["machineSignalZH"]="报警";
				jo["cnc_alarm_message"]=jo["cnc_alarm"];
			}
		}
		
	if(jo["cnc_mecpos"]){
			var jsonData= JSON.parse(jo["cnc_mecpos"]);
			$.each(jsonData,function(){
				jo["cnc_mc"+this["axis"]]=this["value"]
			}) 
		}
		
		if(jo["cnc_ablpos"]){
			var jsonData= JSON.parse(jo["cnc_ablpos"]);
			$.each(jsonData,function(){
				jo["cnc_ab"+this["axis"]]=this["value"]
			}) 
		}
		
		if(jo["cnc_relpos"]){
			var jsonData= JSON.parse(jo["cnc_relpos"]);
			$.each(jsonData,function(){
				jo["cnc_rc"+this["axis"]]=this["value"];
			}) 
		}
		
		if(jo["mcx"]){
			jo["cnc_mcX"]=jo["mcx"];
		}
		
		if(jo["cnc_actfspeed"]){
			 var cncActfspeed =jo["cnc_actfspeed"];
			 if(cncActfspeed.indexOf(".")>-1){
				 jo["cnc_actfspeed"]=cncActfspeed.split(".")[0];
			 }
		}
	 
		if(jo["cnc_actspeed"]){
			 var cncActspeed =jo["cnc_actspeed"];
			 if(cncActfspeed.indexOf(".")>-1){
				 jo["cnc_actspeed"]=cncActfspeed.split(".")[0];
			 }
		}
		
		if(jo["mcy"]){
			jo["cnc_mcY"]=jo["mcy"];
		}
		if(jo["mcz"]){
			jo["cnc_mcZ"]=jo["mcz"];
		}
		if(jo["mca"]){
			jo["cnc_mcA"]=jo["mca"];
		}
		
		if(jo["rcx"]){
			jo["cnc_rcX"]=jo["rcx"];
		}
		if(jo["rcy"]){
			jo["cnc_rcY"]=jo["rcy"];
		}
		if(jo["rcz"]){
			jo["cnc_rcZ"]=jo["rcz"];
		}
		if(jo["rca"]){
			jo["cnc_rcA"]=jo["rca"];
		}
		
		if(jo["cnc_fload"] && jo["cnc_fload"].indexOf("[")>-1){
			$.each(jsonData,function(){
				jo["cnc_l"+this["axis"]]=this["value"]
			})
		} 
		
		if(jo["lx"]){
			jo["cnc_lX"]=jo["lx"];
		}
		if(jo["ly"]){
			jo["cnc_lY"]=jo["ly"];
		}
		if(jo["lz"]){
			jo["cnc_lZ"]=jo["lz"];
		}
		
	jo.machineName=deviceName[jo["code"]]
	replaceKey(jo)
	return;
}	

function replaceKey(jo){
		for(var key in deviceParameters){
			if(jo[key]){
				deviceParameters[key]=jo[key];
			}
		}
	localStorage.setItem(deviceParameters["machineName"], JSON.stringify(deviceParameters));
	/*$scope.switchStatus(deviceParameters);*/
	return;
}

$scope.switchStatus=function(jo){
		var machineNames = localStorage.getItem("machineNames").split("&");
		var obj;
		if($.inArray(jo["machineName"],machineNames)>0){
			obj= JSON.parse(localStorage.getItem(jo["machineName"]));
		var status="";
		if(obj.machineSignal==null||obj.machineSignal==""){
			status="UNKNOW";
		}else{
			status=obj.machineSignal;
		}
		var machineName=obj.machineName;
		$("#"+machineName+"_m").attr("class","")
		$("#"+machineName+"_m").addClass("circle"+" "+"circle-"+status.toLowerCase()+" "+"headerBox");
	 } 
		$scope.createList();
		return;
}

$scope.createList=function(){
	 $scope.devices = [];
	 $scope.alarmList=[];
	for(var key in deviceName){
		if(localStorage.getItem(deviceName[key])!=null){
			var obj = JSON.parse(localStorage.getItem(deviceName[key]));
			$scope.devices.push(obj);
			if(obj["machineSignal"]=="ALARM"){
				$scope.alarmList.push(obj);
			}
		}
		
	}
	insertMontoring();
	if($scope.alarmList.length>0){
		alarm();
	}else{
		layer.closeAll();
	}
	return;
}

function alarm(){
	layer.open({
		  type: 1,
		  shade: false,
		  area: ['600px', '350px'],
		  title: ["设备报警"],
		  closeBtn: 0,
		  content: $('#alarmList'),
		  scrollbar:false
	 });
}
 
function insertMontoring(){
	 var machineNames = localStorage.getItem("machineNames").split("&");
	 if(machineNames!=null){
		 for(var i=0;i<machineNames.length;i++){
			/* console.info(localStorage.getItem(machineNames[i]));
			 console.info(typeof localStorage.getItem(machineNames[i]))*/
			 if(localStorage.getItem(machineNames[i])!=null){
			 	 $.ajax({
					type: "post",
					url: "/member/insertMonitor.json",
					data:localStorage.getItem(machineNames[i]),
				 	contentType:"application/json;charset=utf-8",
					dataType:"json",
					timeout:3000,
					success:function(data){
						 if(data.success){
							 console.info(data.success);
						 }
					}
				 }) 
			 }
		 }
	 }
	 return;
} 
 
$interval(function(){
	/*publishPieData()*/
},5000)
function publishPieData(){
	$.get("/member/publishPieData.json");
	$timeout(function(){
		publishRanking()
	},2000)
}

function publishRanking(){
	$.get("/member/publishRanking.json") 
	$timeout(function(){
		publishTimeLine()
	},5000)
}

function publishTimeLine(){
	 $.get("/member/timeLine.json")
	 publicMachineInfo();
}

function publicMachineInfo(){
	 $.get("/member/publicMachineInfo.json")
}

}).directive('myClock',function($interval,$http){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			$(function() {
				var monthNames = [ "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" ]; 
				var dayNames= ["星期一","星期二","星期三","星期四","星期五","星期六","星期天"]
				var newDate = new Date();
				newDate.setDate(newDate.getDate());
				$('#Date').html(newDate.getFullYear() + " " + monthNames[newDate.getMonth()]+ ' ' +newDate.getDate() +"日"+ ' ' +dayNames[newDate.getDay()-1]);
				setInterval( function() {
					var seconds = new Date().getSeconds();
					$("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
					},1000);
				setInterval( function() {
					var minutes = new Date().getMinutes();
					$("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
				    },1000);
					
				setInterval( function() {
					var hours = new Date().getHours();
					$("#hours").html(( hours < 10 ? "0" : "" ) + hours);
				    }, 1000);	
				});
		}
	}
}).directive('myScoller',function($interval){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			var maxLength=3;
			var timer =6000*5;
			elem.ready(function(){
			    if (elem.children().length < maxLength) {
			    	elem.append(elem.children().clone());
			    }
			    $interval(function(){
			    	var fchild=elem.children(":first");
			    	var left=fchild.width();
			    	fchild.animate({ "marginLeft": (-1 * left) + "px" },2000, function () {
						$(this).css("margin-left", "auto").appendTo(elem);
			        });
			    },timer);
			})
		}
	}
}).directive('rollUp',function($interval){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			scope.$on('repeatFinished',function(event){
				$interval(function(){
					var childFr=elem.children(".roll-up").eq(0);
					childFr.remove(); 
				 	elem.append(childFr.clone(true));
				},6000)
			})
		}
	}
}).directive('ngRepeatFinished',function($timeout){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			if(scope.$last==true){
				$timeout(function() {
                    scope.$emit('repeatFinished');
                });
			}
		}
	}
}).directive('watchDevice',function($timeout){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			if(scope.$last==true){
				$timeout(function() {
                    scope.$emit('repeatFinished');
                });
			}
		}
	}
})