/**
 * 
 */
var app = angular.module('dashboradApp', []);
app.controller('myCon', function($scope,$http,$interval) {
	$(function(){
		$('#machineInfo').slideReveal({
			width: '100%',
			push: false,
			position: 'top',
			show: function (obj) {
				
			},
			hide: function (obj) {
				 window.location.reload();
			},
			hidden: function (obj) {
				window.location.reload();
			},
		});
		switchStatus();
	})
	
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
	},10000)
	
	
	function switchStatus(){
		var machineNames = localStorage.getItem("machineNames").split("&");
		$.each(machineNames,function(){
			var obj= JSON.parse(localStorage.getItem(this));
			if(obj!=null){
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
			})
			 setTimeout(function(){
				switchStatus()
			},2000);
	 };
  
	$scope.getMahineinfo=function(name){
		$('#machineInfo').slideReveal('show');
	    var cnc= JSON.parse(localStorage.getItem(name));
	    console.info(cnc);
		if(cnc==null){
	    	return;
	    }
		$scope.cncName=cnc["machineName"];
		$scope.cncMainproname=cnc["cnc_mainproname"];
		$scope.cncSeq=cnc["cnc_seq"];
		$scope.cncAlivetime=cnc["cnc_alivetime"];
		$scope.cncCycletime=cnc["cnc_cycletime"];
		$scope.cncCycletime=cnc["cnc_cycletime"];
		$scope.cncProducts=cnc["cnc_products"];
		
		$scope.cncActfspeed=cnc["cnc_actfspeed"];
		$scope.cncActspeed=cnc["cnc_actspeed"];
		 $('#myCarousel').carousel({
			  interval:10000,
		 });
		 
		 setTimeout(function(){
			 initLoadLine(name)
		 },1000)
		 
		 $interval(function(){
			  initCoordinate(cnc)
		 },1000)
	}
	
	function initCoordinate(cnc){
		/***********机械坐标**************/
		$scope.cncMcX=cnc["cnc_mcX"];
		$scope.cncMcY=cnc["cnc_mcY"];
		$scope.cncMcZ=cnc["cnc_mcZ"];
		$scope.cncMcA=cnc["cnc_mcA"];
		/***********机械坐标**************/
		/***********相对坐标**************/
		$scope.cncRcX=cnc["cnc_rcX"];
		$scope.cncRcY=cnc["cnc_rcY"];
		$scope.cncRcZ=cnc["cnc_rcZ"];
		$scope.cncRcA=cnc["cnc_rcA"];
		/***********相对坐标**************/
		
		/***********绝对坐标**************/
		$scope.cncAbX=cnc["cnc_abX"];
		$scope.cncAbY=cnc["cnc_abY"];
		$scope.cncAbZ=cnc["cnc_abZ"];
		$scope.cncAbA=cnc["cnc_abA"];
		/***********绝对坐标**************/
	}
	
	 function initLoadLine(machineName){
		 	 var lineLoadObj=[];
			 var loadChartsContrainer =$("div[name='loadChartsContainer']");
			 var chartsArry=[];
			 $.each(loadChartsContrainer,function(index,element){
				 var k={name:"",charts:null}
				  k.name=$(this).attr("data-type");
				  k.charts=echarts.init(this);
				  chartsArry.push(k);
			 })
			 
			  $interval(function(){
				  initLoadLineOption(chartsArry,machineName)
			  },3000)
	 }
	 
	 function initLoadLineOption(eArrays,machineName){
		 var now = new Date();
		 var time = now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
		 var cnc= JSON.parse(localStorage.getItem(machineName));
		 $.each(eArrays,function(i,em){
			 var name=this.name;
			 if(cnc[name]){
				 var lineLoadOpt=loadoptions[i];
				/* lineLoadOpt.series[0]
				 lineLoadOpt.xAxis[0].data*/
				 var seriesData=[];
				 var xAxisData =[];
				 /* var yAxisData =lineLoadOpt.yAxis[0].data;*/
				 xAxisData.push(time);
				 lineLoadOpt.xAxis[0].data=lineLoadOpt.xAxis[0].data.concat(xAxisData);
				 var n = Number(cnc[name]);
				 var i=0;
				 if(cnc[name] && !isNaN(n)){
					 i=parseInt(cnc[name]);
				 }else{
					 i=0;
				 }
				 seriesData.push(i);
				/* yAxisData.push(i);*/
				 $scope[name]=i;
				 seriesData.name=name;
				 lineLoadOpt.series[0].data= lineLoadOpt.series[0].data.concat(seriesData);
				 this.charts.setOption(lineLoadOpt,true);
			 }
			
		 })
		 
		
		 intiGauge(machineName)
	 }
	 
	 
	 function intiGauge(machineName){
		 var gagueCharts = $("#gagues").children("div");
		 var gagueArrays=[];
		 $.each(gagueCharts,function(){
			 var g={name:"",e:null};
			 g.name=$(this).attr("data-type");
			 g.e=echarts.init(this);
			 gagueArrays.push(g);
		 })
		 
		  $interval(function(){
			   setGagueoPtion(gagueArrays,machineName);
		  },1000)
		 
	 }
	 
	 var dataZH={
		 cnc_srate:"主轴倍率",
		 cnc_frate:"进给倍率",
		 cnc_rapidfeed:"快速移动倍率"
	 }
	 
	 function setGagueoPtion(eArrays,machineName){
		 var cnc= JSON.parse(localStorage.getItem(machineName));
		 $.each(eArrays,function(i,em){
			 var name=this.name;
			 var n = Number(cnc[name]);
			 var i=0;
			 var gOpt=gaugeOptions[i];
			 if(cnc[name]){
				 if(!isNaN(n)){
					 i=parseInt(cnc[name]);
				 }else{
					 i=10;
				 }
			 }
			 var m={name:"",value:0};
			 m.name=dataZH[name];
			 m.value=i;
			 gOpt.series[0].data[0]= m;
			 this.e.setOption(gOpt);
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
