var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$timeout,$interval) {
$(function(){
	connection()
});

var websocket;
function connection(){
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/ws.html");
    }else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://localhost:8080/ws.html");
    }
    else {
        websocket = new SockJS("http://localhost:8080/ws/socketjs.html");
    }
    websocket.onError=$scope.onError;
    websocket.onClose=$scope.onClose;
    websocket.onopen=$scope.onopen;
    websocket.onmessage=$scope.onmessage;
}

$scope.onError=function(openEvt){
	connection();
}

$scope.onClose=function onClose() {
	console.info("链接已关闭");
}

$scope.onopen= function(event) {
	console.info("链接已建立");
	var data={"sources":"MEMBER"};
	websocket.send(JSON.stringify(data));
	$scope.callbackReportData();
}

var pies = new MyPies();
var ranking = new Ranking();
var myTimeLine =new MyTimeLine();
$scope.onmessage=function(evt) {
	var JsonObject = JSON.parse(evt.data);
	$scope.switchStatus(JsonObject["dataList"]);
	pies.dataPieInit(JsonObject["pies"]["content"]); 
	ranking.dataRankingInit(JsonObject["ranking"]["content"]);
	myTimeLine.dataTimeLineInit(JsonObject["timeLineCategories"]["content"],JsonObject["timeLineSeriesData"]["content"]);
}

window.close=function(){
	$scope.ws.onclose();
}

$scope.callbackReportData =function(){
 /* $.get("/member/callbackReportData.json");*/
}

$scope.switchStatus=function(obj){
	$scope.items=obj["content"];
	$scope.creatAlarmList(obj["content"]);
	$.each(obj["content"],function(){
		var status="";
		 if(this.machineSignal==null||this.machineSignal==""){
			 status="UNKNOW";
		 }else{
			 status=this.machineSignal;
		 }
		 var machineName=this.machineName;
		 $("#"+machineName+"_m").attr("class","")
		 $("#"+machineName+"_m").text(machineName);
		 $("#"+machineName+"_m").addClass("circle"+" "+"circle-"+status.toLowerCase()+" "+"headerBox");
	})
} 

$scope.creatAlarmList=function(data){
	$scope.alarmList=[];
	$.each(data,function(){
		if(this.machineSignal!=null && this.machineSignal.toLowerCase=="alarm"){
			console.info(machineSignal);
			$scope.alarmList.push(this);
		}
	})
	
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