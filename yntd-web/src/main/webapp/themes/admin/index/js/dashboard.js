/**
 * 
 */
var app = angular.module('dashboradApp', []);
app.controller('myCon', function($scope,$http,$interval) {
	$interval(function(){
		$http({
			method: 'GET',
			url:"/admin/index/data.json",
			cache:false,
			async:false}).then(function(res){
			$scope.running=res.data.RUNNING;
			$scope.poweroff=res.data.POWEROFF;
			$scope.alarm=res.data.ALARM;
			$scope.waiting=res.data.WAITING;
		})
	},2000)
	
	 $interval(function(){
		$http({
				method: 'GET',
				url:"/admin/index/datalist.json",
				cache:false,
				async:false}).then(function(res){
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
	 
}).directive('lineCharts',function($interval,$http){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			 var myLineCharts=echarts.init(elem.get(0));
			 myLineCharts.showLoading('default', {text:'数据统计中...',maskColor: '#0000004a',textColor: '#4caf50'});
			 $interval(function(){
				 $http({
						method: 'GET',
						url:"/admin/index/line.json",
						cache:false,
						async:false
					 }).then(function(res){
						lineOption.xAxis.data=res.data.xAxis;
						lineOption.series[0].data=res.data.data;
						myLineCharts.hideLoading();
						myLineCharts.setOption(lineOption,true);
					 })
			 },10000)
		
		}
	}
	
}).directive('barCharts',function($interval,$http){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			 var myLineCharts=echarts.init(elem.get(0));
			 myLineCharts.showLoading('default', {text:'数据统计中...',maskColor: '#0000004a',textColor: '#4caf50'});
			 $interval(function(){
				 $http({
						method: 'GET',
						url:"/admin/index/bar.json",
						cache:false,
						async:false
					 }).then(function(res){
						 barOption.xAxis.data=res.data.yAxisData;
						 barOption.series=res.data.series;
						 myLineCharts.hideLoading();
						 myLineCharts.setOption(barOption,true);
					 })
			 },5000)
		
		}
	}
	
}).directive('timeLine',function($interval,$http){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			 var myLineCharts=echarts.init(elem.get(0));
			 myLineCharts.showLoading('default', {text:'数据统计中...',maskColor: '#0000004a',textColor: '#4caf50'});
			 $interval(function(){
				 $http({
						method: 'GET',
						url:"/admin/index/bar.json",
						cache:false,
						async:false
					 }).then(function(res){
						 barOption.xAxis.data=res.data.yAxisData;
						 barOption.series=res.data.series;
						 myLineCharts.hideLoading();
						 myLineCharts.setOption(barOption,true);
					 })
			 },5000)
		
		}
	}
})
