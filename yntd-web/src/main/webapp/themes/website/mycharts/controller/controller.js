var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$timeout,$interval) {
$.ajaxSetup({
	  async: false
});

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
    	replaceMachineName(jsonData)
    };
    
    
    websocket.onopen= function(event) {
    	console.info("通道已建立");
    	websocket.send(JSON.stringify({"status":0,"isFinished":false,"topices":topices}));
    }
}
	
function replaceMachineName(jo){
	jo["machineName"]=deviceName[jo["machineName"]];
	for(var key in deviceParameters){
		if(jo[key]){
			deviceParameters[key]=jo[key];
		}else{
			deviceParameters[key]="";
		}
	}
	$scope.switchStatus(deviceParameters);
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
	pies.dataPieInit(JsonObject["pies"]["content"]); 
	ranking.dataRankingInit(JsonObject["ranking"]["content"]);
	console.info(JsonObject["timeLineSeriesData"]["content"]);
	myTimeLine.dataTimeLineInit(JsonObject["timeLineCategories"]["content"],JsonObject["timeLineSeriesData"]["content"]);*/

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
	 $scope.creatList(obj);
} 

$scope.creatList=function(data){
	$scope.alarmList=[];
	$scope.runningList=[];
	if(data.machineSignal=="ALARM"){
		$scope.alarmList.push(data);
	}
	if(data.machineSignal=="POWEROFF"){
		$scope.runningList.push(data);
	}
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