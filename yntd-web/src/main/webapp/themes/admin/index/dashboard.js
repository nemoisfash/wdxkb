/**
 * 
 */
var app = angular.module('dashboradApp', []);
app.controller('myCon', function($scope,$http,$interval) {
	var bar = echarts.init(document.getElementById('stackBar'));
	$interval(function(){
		$http({
			method: 'GET',
			url:"/admin/index/data.json",
			cache:false,
			async:false}).then(function(res){
			$scope.runningTimes=res.data.RUNNING;
			$scope.poweroffTimes=res.data.POWEROFF;
			$scope.alarmTimes=res.data.ALARM;
			$scope.waitingTimes=res.data.WAITING;
			$scope.manualTimes=res.data.MANUAL;
			createBar();
			MonitoringList();
		})
	},2000)
	
	function MonitoringList(){
			$.ajax({
				type :"GET",
				url :"/admin/index/monitoring.json",
				async:true,
				cache : false,
				ifModified:true,
				success : function(data) {
					$scope.entities=data.resault;
				}
		})
	}
	
	function createBar(){
		$.ajax({
			type :"GET",
			url :"/admin/index/bar.json",
			async:true,
			cache : false,
			ifModified:true,
			success : function(data) {
				console.info(data);
				barOption.yAxis.data=data.yAxisData;
				barOption.series=data.series;
				addLables(barOption.series);
				bar.setOption(barOption);
			}
		})
		
	}
	
	function addLables(obj){
		$.each(obj,function(){
			this.label=lable;
		})
	}
})
