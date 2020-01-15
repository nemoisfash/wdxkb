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
 getMahineinfo();
 function getMahineinfo(){
	   var cnc= JSON.parse(localStorage.getItem("V10"));
		$scope.cncName=cnc["machineName"];
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
		$scope.cncMainproname=cnc["cnc_mainproname"];
		$scope.cncSeq=cnc["cnc_seq"];
		$scope.cncAlivetime=cnc["cnc_alivetime"];
		$scope.cncCycletime=cnc["cnc_cycletime"];
		$scope.cncCycletime=cnc["cnc_cycletime"];
		$scope.cncProducts=cnc["cnc_products"];
	}
 
	 setTimeout(function(){
		 initLoadLine()
	 },1000)
	 function initLoadLine(){
		 	 var lineLoadObj=[];
			 var loadChartsContrainer =$("div[name='loadChartsContainer']");
			 var chartsArry=[];
			 $.each(loadChartsContrainer,function(index,element){
				 var k={name:"",charts:null}
				  k.name=$(this).attr("data-type");
				  k.charts=echarts.init(this);
				  chartsArry.push(k);
			 })
			 
			  $('#myCarousel').carousel({
				  interval:6000,
			  });
			 
			  $interval(function(){
				  initLoadLineOption(chartsArry)
			  },1000)
			
	 }
	 
	 function initLoadLineOption(eArrays){
		 var now = new Date();
		 var time = now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
		 var cnc= JSON.parse(localStorage.getItem("V10"));
		 $.each(eArrays,function(i,em){
			 var name=this.name;
			 if(cnc[name]){
				 var lineLoadOpt=loadoptions[i];
				 var seriesData= lineLoadOpt.series[0];
				 var xAxisData =lineLoadOpt.xAxis[0].data;
				 xAxisData.push(time);
				 var n = Number(cnc[name]);
				 var i=0;
				 if(cnc[name] && !isNaN(n)){
					 i=parseInt(cnc[name]);
				 }else{
					 i=0;
				 }
				 seriesData.data.push(i);
				 $scope[name]=i;
				 seriesData.name=name;
				 this.charts.setOption(lineLoadOpt,true);
			 }
			
		 })
	 }
	 
	 intiGauge();
	 function intiGauge(){
		 var gagueCharts = $("#gagues").children("div");
		 var gagueArrays=[];
		 $.each(gagueCharts,function(){
			 var g={name:"",e:null};
			 g.name=$(this).attr("data-type");
			 g.e=echarts.init(this);
			 gagueArrays.push(g);
		 })
		 
		  $interval(function(){
			   setGagueoPtion(gagueArrays);
		  },1000)
		 
	 }
	 
	 var dataZH={
			 cnc_srate:"主轴倍率",
			 cnc_frate:"进给倍率",
			 cnc_rapidfeed:"快速移动倍率"
	 }
	 
	 function setGagueoPtion(eArrays){
		 var cnc= JSON.parse(localStorage.getItem("V10"));
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
