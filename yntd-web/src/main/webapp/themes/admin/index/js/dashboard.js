/**
 * 
 */
var app = angular.module('dashboradApp', []);
app.controller('myCon', function($scope,$http,$interval) {
	 $interval(function(){
			$http({
					method: 'GET',
					url:"/admin/index/datalist.json",
					cache:false,
					async:false}).then(function(res){
					$scope.switchStatus(res.data.resault);
					$scope.getStatusData()
			})
	},10000)
		 
	$scope.getStatusData=function(){
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
	},
	
	$scope.switchStatus=function(obj){
		$.each(obj,function(){
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
	
	$scope.getMahineinfo=function(name){
	   var cnc= JSON.parse(localStorage.getItem(jo[name]));
		$scope.cncName=cnc["machineName"];
		$scope.cncProducts=cnc["cnc_products"];
		$scope.cncAlivetime=cnc["cnc_alivetime"];
		$scope.cncCycletime=cnc["cnc_cycletime"];
		/***********机械坐标**************/
		$scope.cncMcX=cnc["cnc_mcX"];
		$scope.cncMcY=cnc["cnc_mcY"];
		$scope.cncMcZ=cnc["cnc_mcZ"];
		$scope.cncMcA=cnc["cnc_mcA"];
		/***********机械坐标**************/
		/***********相对坐标**************/
		$scope.cncRcX=cnc["cnc_rcX"];
		$scope.cncRcY=cnc["cnc_rcX"];
		$scope.cncRcZ=cnc["cnc_rcX"];
		$scope.cncRcA=cnc["cnc_rcX"];
		/***********相对坐标**************/
		
		/***********绝对坐标**************/
		$scope.cncRcAbX=cnc["cnc_abX"];
		$scope.cncRcAbY=cnc["cnc_abY"];
		$scope.cncRcAbZ=cnc["cnc_abZ"];
		$scope.cncRcAbA=cnc["cnc_abA"];
		/***********绝对坐标**************/
	}
	
	 initLine()
	 function initLine(){
		 var lineLoad=loadoption;
		 console.info(lineLoad);
		 var loadCharts=echarts.init($("#lineLoad").get(0));
		 loadCharts.setOption(lineLoad,true)
		 intiGauge();
	 }
	 
	 function intiGauge(){
		 var gOption=gaugeOption;
		 var gagueCharts = $("#gagues").children("div"); 
		 $.each(gagueCharts,function(){
			 echarts.init(this).setOption(gOption,true);
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
