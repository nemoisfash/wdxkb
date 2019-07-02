/var myTimeLine = new MyTimeLine();*/
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
	},2000)

//生产计划
	
var ranking = new Ranking();
var pies = new MyPies();
$scope.switchStatus=function(obj){
	$.each(obj,function(){
		 var status=this.machineSignal;
		 var machineName=this.machineName;
		 $("#"+machineName+"_m").attr("class","")
		 $("#"+machineName+"_m").text(machineName);
		 $("#"+machineName+"_m").addClass("circle"+" "+"circle-"+status.toLowerCase()+" "+"headerBox");
	})
}

}).directive('myClock',function($interval){
	return{
		restrict:'A',
		link:function(scope,elem,attrs){
			var weekArray = new Array("日", "一", "二", "三", "四", "五", "六");
			$interval(function(){
				var now = new Date();
				var time = now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
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
})
