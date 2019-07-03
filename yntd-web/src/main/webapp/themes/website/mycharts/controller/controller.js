var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$interval) {
$interval(function(){
	$http({
			method: 'GET',
			url:"/member/datalist.json",
			cache:false,
			async:false}).then(function(res){
			$scope.items=res.data.resault;
			$scope.switchStatus(res.data.resault);
		})
	},1000)
	
	$scope.switchStatus=function(obj){
		$.each(obj,function(){
			 var status=this.machineSignal;
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
				var date = now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate()+" "+time+" "+"星期"+weekArray[now.getDay()];
				elem.text(date);
				if(now.getHours().toString=="0"){
					clearTimeLineData()
				}
			},1000)
			
			function clearTimeLineData(){
				$http({
					method: 'GET',
					url:"/member/clearTimeLineData.json",
					cache:false,
					async:false})
			}
		}
	}
}).directive('myScoller',function($interval){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			var maxLength=3;
			var timer =6000;
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
}).directive('timeLine',function($interval,$http){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			var myChart = echarts.init(elem.get(0));
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
			     dataZoom: [{
			            type: 'slider',
			            xAxisIndex: 0,
			            filterMode: 'weakFilter',
			            height: 20,
			            bottom: 0,
			            start: 0,
			            end: 100,
			            handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
			            handleSize: '80%',
			            showDetail: true
			        }, {
			            type: 'inside',
			            id: 'insideX',
			            xAxisIndex: 0,
			            filterMode: 'weakFilter',
			            start: 95,
			            end: 100,
			            zoomOnMouseWheel: true,
			            moveOnMouseMove: true
			        }],
			        xAxis:{
			            type: 'time',
			            position: 'top',
			            splitLine: {
			                lineStyle: {
			                    color: ['#E9EDFF']
			                }
			            },
			            axisLine: {
			                show: false
			            },
			            axisTick: {
			                lineStyle: {
			                    color: '#929ABA'
			                }
			            },
			            axisLabel: {
			                color: '#929ABA',
			                inside: false,
			                align: 'center'
			            }
			    },
			    yAxis: {
			    	type:"category",
			    	boundaryGap: ['20%', '20%'],
			    	min: 0,
		            max:8,
		            axisTick: 'none',
			    	axisLine: {
			                show: true,
			                lineStyle: {
			                    color: '#929ABA'
			                }
			        },
			        axisLabel: {
			            textStyle: {
			                color: '#ffffff',
			                fontSize: '10',
			            }
			        },
			        data: []
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
			
			$interval(function(){
			$http({
					method: 'GET',
					url:"/member/timeLine.json",
					cache:false,
					async:false}).then(function(res){
						option.yAxis.data=res.data.categories;
						option.yAxis.max=res.data.categories.length;
						option.series[0].data=res.data.data;
						myChart.hideLoading();
						myChart.setOption(option, true);
				})
			},10000)
		}
	}
}).directive('myPies',function(){
	var pies = new MyPies();
}).directive('rankingRunning',function(){
	var ranking = new Ranking();
})
