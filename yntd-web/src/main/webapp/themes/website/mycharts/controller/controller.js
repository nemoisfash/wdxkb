var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$interval) {
 $interval(function(){
	 $http({
		method: 'GET',
		url:"/member/publicMonitoring.json",
		cache:false,
		async:false}).then(function(res){
			/*	$scope.items=res.data.resault;
			console.info(res.data.resault);
			$scope.switchStatus(res.data.resault); */
		})
},10000)
	
$scope.createWebSocket=function(){
	websocket = null;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/ws.html");
    }else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://localhost:8080/ws.html");
    }
    else {
        websocket = new SockJS("http://localhost:8080/ws/socketjs.html");
    }
    websocket.onopen = $scope.onOpen;
    websocket.onmessage = $scope.onMessage;
    websocket.onerror = $scope.onError;
    websocket.onclose = $scope.onClose;
    $scope.ws=websocket;
}

window.close=function(){
	$scope.ws.onclose();
}

$scope.onOpen= function(openEvt) {
	$.get("/member/subscribe.json");
}

$scope.onMessage=function(evt) {
	 var jsonObj =JSON.parse(evt.data);
	 $scope.switchStatus(jsonObj["dataList"]);
}

$scope.onError=function(){
	
}

$scope.onClose=function onClose() {
}

setTimeout($scope.createWebSocket,4000); 

$scope.switchStatus=function(obj){
	$scope.items=obj["content"];
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
}).directive('myPies',function(){
	return function($scope, ele, attrs){
		var pies = new MyPies();
		$scope.onMessage=function(event){
			var jsonObj =JSON.parse(event.data);
			pies.dataPieInit(jsonObj["pies"]["content"]); 
		}
	}
}).directive('timeLine',function($interval,$http,$timeout){
	return function($scope, ele, attrs){
		var myChart = echarts.init(ele.get(0));
		myChart.showLoading('default', {text:'数据统计中...',maskColor: '#07112a61',textColor: '#36b0f3',});
		function renderItem(params, api) {
		    var categoryIndex = api.value(0);
		    var start = api.coord([api.value(1), categoryIndex]);
		    var end = api.coord([api.value(2), categoryIndex]);
		    var height = api.size([0, 1])[1] * 0.6;
		    var rectShape = echarts.graphic.clipRectByRect({
		        x: start[0],
		        y: start[1] - height / 2,
		        width: end[0] - start[0],
		        height: height
		    }, {
		        x: params.coordSys.x,
		        y: params.coordSys.y,
		        width: params.coordSys.width,
		        height: params.coordSys.height
		    });
		
		    return rectShape && {
		        type: 'rect',
		        shape: rectShape,
		        style: api.style()
		    };
		}
		var	option = {
			    tooltip: {
			        formatter: function (params) {
			            return params.marker + params.name + ':' + params.value[3] + '分钟';
			        }
			    },
			    grid: {
			        left: '10%',
			        top: '12%',
			        right: '2%',
			        bottom: '8%',
			        containLabel: false
			    },
		        xAxis:{
			            type: 'time',
			            position: 'top',
			            splitLine: {
			                lineStyle: {
			                    color: ['#34a9eb']
			                }
			            },
			            axisLine: {
			                show: false,
			                lineStyle: {
			                    color: '#34a9eb'
			                }
			            },
			            axisTick: {
			                lineStyle: {
			                    color: '#34a9eb'
			                }
			            },
			            axisLabel: {
			                color: '#34a9eb',
			                inside: false,
			                align: 'center'
			            }
			    },
			    yAxis: {
			    	type:"category",
			    	boundaryGap: ['20%', '20%'],
		            position:'left',
			    	axisLine: {
		                show:true,
		                lineStyle: {
		                    color: '#929ABA'
		                }
			        },
			        axisTick:{
			        	show:true,
			        	interval:0,
			        	length:2
			        },
			        axisLabel: {
			        	interval:0,
			        	margin:10,
			            textStyle: {
			                color: '#34a9eb',
			                fontSize: '8',
			            }
			        },
			        data:[],
			    },
			    series: [{
			        type: 'custom',
			        renderItem: renderItem,
			        itemStyle: {
			            normal: {
			                opacity: 0.8
			            }
			        },
			        encode: {
			            x: [1, 2],
			            y: 0
			        },
			        data: []
			    }]
			};
		
		$scope.onMessage=function(event){
			var jsonObj =JSON.parse(event.data);
			var categories=option.jsonObj["timeLineCategories"]["content"]; 
			var seriesData=option.jsonObj["timeLineSeriesData"]["content"];
			option.yAxis.data=categories.data;
			option.yAxis.max=categories.data.length-1;
			var c=option.series[0].data.concat(seriesData);
			option.series[0].data=c;
			myChart.setOption(option,{
			    notMerge:true,
			    lazyUpdate:false,
			    silent:false
			});
		}
		myChart.hideLoading();
	}
}).directive('rankingRunning',function(){
	return function($scope, ele, attrs){
		var ranking = new Ranking();
		$scope.onMessage=function(event){
			var jsonObj =JSON.parse(event.data);
			ranking.dataRankingInit(jsonObj["ranking"]["content"]);
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