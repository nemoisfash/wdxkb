var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$timeout,$interval) {
$.get("/member/getAllTopics.json",function(data){
	if(data.success){
		var others = ["pies", "ranking", "timeLineCategories","timeLineSeriesData"];
		var topices=data.data.concat(others);
		localStorage.setItem("topices",topices);
		$timeout(function(){
			connection(topices)
		},1000)
	}
})

function connection(topices){
	var websocket;
	var host = window.location.host;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://"+host+"/ws.html");
    }else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://"+host+"/ws.html");
    }
    else {
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
    	$timeout(function(){
    		clearData(jsonData)
    	},1000)
    };
    
    websocket.onopen= function(event) {
    	console.info("通道已建立");
    	websocket.send(JSON.stringify({"status":0,"isFinished":false,"topices":topices}));
    }
}
	
function clearData(jo){
		jo["machineName"]=deviceName[jo["machineName"]];
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
		}
		
		if (jo["wz"] && jo["wz"]=="1") {
			jo["machineSignal"]="WAITING";
		}
		
		if (jo["az"] && jo["az"]=="1") {
			jo["machineSignal"]="ALARM";
		}
		if(jo["device_state"]) {
			if (jo["device_state"]=="0") {
				jo["machineSignal"]="RUNNING";
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
			}
		}
		if (jo["cnc_alarm"]) {
			if(jo["cnc_alarm"]!="[]"){
				jo["machineSignal"]="ALARM";
				jo["cnc_alarm_message"]=jo["cnc_alarm"];
			}
		}
		
		if(jo["cnc_mecpos"]){
			var jsonData= JSON.parse(jo["cnc_mecpos"]);
			$.each(jsonData,function(){
				jo["cnc_mc"+this["axis"]]=this["value"]
			}) 
		}
		
		if(jo["cnc_relpos"]){
			var jsonData= JSON.parse(jo["cnc_mecpos"]);
			$.each(jsonData,function(){
				jo["cnc_rc"+this["axis"]]=this["value"]
			}) 
		}
		
		if(jo["mcx"]){
			jo["cnc_mcX"]=jo["mcx"];
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
		
		if(jo["cnc_fload"]){
			var jsonData= JSON.parse(jo["cnc_fload"]);
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
		
	$timeout(function(){
		replaceKey(jo)
	},1000)
}	

$scope.devices={
}
function replaceKey(jo){
	if(jo.success){
		for(var key in deviceParameters){
			if(jo[key]){
				deviceParameters[key]=jo[key];
			}
		}
	}
	localStorage.setItem(deviceParameters["machineName"], JSON.stringify(deviceParameters));
	$timeout(function(){
		$scope.switchStatus(deviceParameters);
	},1000)
}

//0,1,2,3,4,5
/*var pies = new MyPies();
var ranking = new Ranking();
var myTimeLine =new MyTimeLine();
$scope.onmessage=function(evt) {
	console.info(evt.data);
/*	var JsonObject = JSON.parse(evt.data);
	console.info(JsonObject);
	$scope.switchStatus(JsonObject["dataList"]);
	pies.dataPieInit(JsonObject["pies"]); 
	ranking.dataRankingInit(JsonObject["ranking"]);
	console.info(JsonObject["timeLineSeriesData"]);
	myTimeLine.dataTimeLineInit(JsonObject["timeLineCategories"],JsonObject["timeLineSeriesData"]);*/

/*window.close=function(){
	$scope.ws.onclose();
}*/

$scope.callbackReportData =function(){
	 $.get("/member/callbackReportData.json");
}

$scope.switchStatus=function(obj){
	var status="";
	 if(obj.machineSignal==null||obj.machineSignal==""){
		 status="UNKNOW";
	 }else{
		 status=obj.machineSignal;
	 }
	 var machineName=obj.machineName;
	 $("#"+machineName+"_m").attr("class","")
	 $("#"+machineName+"_m").text(machineName);
	 $("#"+machineName+"_m").addClass("circle"+" "+"circle-"+status.toLowerCase()+" "+"headerBox");
	 $timeout(function(){
		 $scope.createList();
	 },1000)
}

$scope.createList=function(){
	$scope.devices = []
	for(var key in deviceName){
		if(localStorage.getItem(deviceName[key])!=null){
			$scope.devices.push(JSON.parse(localStorage.getItem(deviceName[key])))
		}
	}
	console.info($scope.devices);
}


}).directive('myClock',function($interval,$http){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			var weekArray = new Array("日", "一", "二", "三", "四", "五", "六");
			$interval(function(){
				var now = new Date();
				var time = now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
				if(time==="0:0:0"){
					localStorage.setItem('flash',"true");
				}
				var date = now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate()+" "+time+" "+"星期"+weekArray[now.getDay()];
				elem.text(date);
			},1000)
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
})