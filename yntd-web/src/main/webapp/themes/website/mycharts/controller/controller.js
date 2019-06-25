var myTimeLine = new MyTimeLine();
/*var myLine = new MyLine();*/
var ranking = new Ranking();
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

var pies = new MyPies();

$interval(function(){
	$http({
		method: 'GET',
		url:"/member/insertLogging.json",
		cache:false,
		async:false})
},3000*10)

$scope.switchStatus=function(obj){
	$.each(obj,function(){
		 var status=this.machineSignal;
		 var machineName=this.machineName;
		 $("#"+machineName+"_m").attr("class","")
		 $("#"+machineName+"_m").text(machineName);
		 $("#"+machineName+"_m").addClass("circle"+" "+"circle-"+status.toLowerCase()+" "+"headerBox");
	})
}
})

setInterval(function(){
	var now = new Date();
	var weekArray = new Array("日", "一", "二", "三", "四", "五", "六");
	var time = now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
	var date = now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate()+" "+time+" "+"星期"+weekArray[now.getDay()];
	$("#clock").text(date);
	 
},1000)

setInterval(function(){
listRoll();
},6000)

function listRoll(){
	var $fristTr =  $("tbody tr:first").clone(false);
	$("tbody tr:first").remove();
	$("tbody").append($fristTr);
}

$(function(){
    new ZouMa().Start();
});

function ZouMa() {
	this.maxLength = 3;  
	this.Timer = 6000; 
	this.Ul = $("ul#pies");
	var handId; 
	var self = this;
	this.Start = function () {
	    if (self.Ul.children().length < this.maxLength) {
	        self.Ul.append(self.Ul.children().clone());
	    }
	    handId = setInterval(self.Play, self.Timer);
	}
		this.Play = function () {
	    var li = self.Ul.children("li").eq(0);
	    var left = li.eq(0).width();
	    li.animate({ "marginLeft": (-1 * left) + "px" }, 1000, function () {
			$(this).css("margin-left", "auto").appendTo(self.Ul);
        });
    }
}