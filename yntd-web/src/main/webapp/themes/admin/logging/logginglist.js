/**
 * 
 */
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$interval) {
	 var type=$("#type").val();
	$http({
		method: 'GET',
		url:"/admin/logging/"+type+"/data.json",
		cache:false,
		async:false
	}).then(function(res){
		$scope.items=res.data.entities.content;
		var totalElements=res.data.entities.totalElements;
		var totalPages=res.data.entities.totalPages;
		var number=res.data.number;
		pagination(totalElements,totalPages,number)
		sumTimeDiff(type)
})

function sumTimeDiff(type){
		$http({
			method: 'GET',
			url:"/admin/logging/"+type+"/sumTimeDiff.json",
			cache:false,
			async:false
		}).then(function(res){
			 $scope.sumTimeDiff=res.data.timediff;
		})
}

function pagination(totalElements,totalPages,number){
	$(".tcdPageCode").createPage({
		elementCount :totalElements,
		pageCount :totalPages,
		current :number,
		backFn : function(to){
			Fw.updateFilter($("#uuid").val(), 'page', to);
		}
	})
};

$scope.chosenTime=function(){
	layer.open({
		type: 1,
		title:"数据导出",
		offset : [ '200px', '' ],
		border : [ 10, 0.3, '#000', true ],
		area : [ '550px', '180px' ],
		content: $("#chosenTime")
	})
}

$scope.keys=[{key:"name",value:"名称"}]

$scope.search=function(){
	var val=$scope.value;
	if(typeof val=="undefined"){
		layer.msg("请输入需要查询的内容");
		$scope.removeFilter("machineName");
		return;
	}else{
		$scope.updateFilter("machineName",val);
	}
}

$scope.searchByTimeDiff=function(){
	var timediff=$scope.timediff;
	if(typeof timediff!="undefined"){
		$scope.updateFilter("timediff",timediff);
	}
}

$scope.createRecordTime=function(){
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	console.info(startTime);
	if(startTime==""){
		layer.msg("请选择开始时间！");
		return;
	}else if(endTime==""){
		layer.msg("请选择结束时间！");
		return;
	}
	var recordTime=startTime+"&"+endTime;
	$scope.updateFilter("recordTime",recordTime);
	
}

$scope.updateFilter=function(key,val){
	Fw.updateFilter($("#uuid").val(),key,val);
}

$scope.removeFilter=function(key){
	Fw.removeFilter($("#uuid").val(),key);
}

$scope.exportData=function(){
	window.location.href="/admin/logging/"+type+"/exportdata";
}

})