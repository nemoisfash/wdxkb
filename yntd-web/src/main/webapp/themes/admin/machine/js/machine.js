var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$interval) {
$(function(){
	$('#realTime').slideReveal({
		width: '100%',
		push: false,
		position: 'top',
		show: function (obj) {
		},
		hide: function (obj) {
		},
		hidden: function (obj) {
		},
		overlay: true,
		overlayClick: false
	});
})

/*****************websocket************************/
function connection(){
	var websocket;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/ws.html");
    }else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://localhost:8080/ws.html");
    }
    else {
        websocket = new SockJS("http://localhost:8080/ws/socketjs.html");
    }
    websocket.onError=$scope.onError;
    websocket.onClose=$scope.onClose;
    websocket.onopen=$scope.onopen;
    websocket.onmessage=$scope.onmessage;
}


$scope.onError=function(openEvt){
	connection();
}

$scope.onClose=function onClose() {
	console.info("链接已关闭");
}

$scope.onopen= function(event) {
	console.info("链接已建立");
	ws.send("clientId","machinelist");
	$scope.callbackTimeLineData();
}

$scope.callbackTimeLineData =function(){
  $.get("/admin/logging/callbackTimeLineData.json");
}
 
var myTimeLine =new MyTimeLine();
$scope.onmessage=function(evt) {
	myTimeLine.dataTimeLineInit(JsonObject["timeLineCategories"]["content"],JsonObject["timeLineSeriesData"]["content"]);
}
/*****************websocket************************/
window.close=function(){
	$scope.ws.onclose();
}



var uuid=$("#uuid").val();
		$http({
				method: 'GET',
				url:"/admin/machine/data.json",
				cache:false,
				async:false
			}).then(function(res){
				$scope.items=res.data.resault.content;
				$scope.count=res.data.count;
				var totalElements=res.data.resault.totalElements;
				var totalPages=res.data.resault.totalPages;
				var number=res.data.number;
				pagination(totalElements,totalPages,number)
		})
		
		$scope.editImg=function(id,imageUrl){
			$scope.machineId=id;
			if(typeof imageUrl!="undefined"){
				$("#imgPreview").attr("src",imageUrl);
 			}
			$scope.myLayer("图片编辑",$("#boxPreview"));
		}
		
		$scope.myLayer=function(title,obj){
			layer.open({
				type: 1,
				title:title,
				offset : [ '200px', '' ],
				border : [ 10, 0.3, '#000', true ],
				area : [ '649px', '557px' ],
				content: obj,
			})
		}
		
		var filedInput=document.getElementById("upload");
		var formData = new FormData();
		filedInput.addEventListener('change',function(){
			var file = this.files[0];
			formData.append("upload",file);
			var reader =new FileReader();
			reader.onloadend = function () {
				$("#imgPreview").attr("src",reader.result);
			}
			if (file){
				reader.readAsDataURL(file);
			}
		},false)
		
		$scope.uploadImg=function(){
			$.get("/admin/machine/removeImage.json",{"imageUri":$scope.imageUrl,"id":$scope.machineId});
			$.ajax({
				type: "post",
				url :  "/admin/machine/uploadImg.json?id="+$scope.machineId,
				cache: false,
				data: formData,
				timeout: 5000,
				processData: false,
				contentType: false,
				xhrFields: {
					withCredentials: true
				},
				success: function(data) {
					if(data.success){
						layer.msg("上传成功");
						setTimeout(function(){
							layer.closeAll()
							window.location.reload();
						},2000)
					}
				}
		})
		}	
		
		$scope.exportData=function(id){
			layer.open({
				type: 1,
				title:"数据导出",
				offset : [ '200px', '' ],
				border : [ 10, 0.3, '#000', true ],
				area : [ '550px', '210px' ],
				content: $("#chosenTime"),
				success:function(){
					 
				}
			})
		 
		}
		
		$scope.search=function(){
			var val= $scope.value;
			if(typeof val=="undefined"){
				layer.msg("请输入设备名称");
				$scope.removeFilter("name");
				return;
			}else{
				$scope.updateFilter("name",val);
			}
		}
		
		$scope.updateFilter=function(key,val){
			Fw.updateFilter(uuid,key,val);
		}
		$scope.removeFilter=function(key){
			Fw.removeFilter(uuid,key);
		}
		
		$scope.logingData=function(id,name,code,ip,image){
			layer.open({
				type: 1,
				title:"设备每日运行数据统计",
				offset : 'auto',
				border : [ 10, 0.3, '#000', true ],
				area : ['750px','689px'],
				content:$("#logging"),
				success:function(){
					createPie(id,name);
					createLine(id,name);
				}
			})
		}
		
	function createPie(id,name){
		 var pie = echarts.init(document.getElementById('piechart'));
		$.ajax({
			type :"GET",
			url :"/admin/logging/pie?id="+id,
			async:true,
			cache : false,
			ifModified:true,
			success : function(data) {
				optionPie.series[0].data=data.resault;
				optionPie.title.text=name;
				pie.setOption(optionPie);
			}
		})
	}	
	
	function createLine(id){
		var line = echarts.init(document.getElementById('lineCharts'));
		$.ajax({
			type :"GET",
			url :"/admin/logging/line?id="+id,
			async:true,
			cache : false,
			ifModified:true,
			success : function(data) {
				optionLine.xAxis.data=data.xAxis;
				optionLine.series=data.series;
				line.setOption(optionLine);
				
			}
		})
	}
	
	$scope.machineData=function(id,name,image,status){
		$('#realTime').slideReveal('show');
		/*$("#machineStatus-header").css("display","block");*/
		$("#machineStatus-header").addClass("box-header"+" "+"machineStatus-"+status.toLowerCase());
		$('#realTime').addClass(' show');
		$scope.crName=name;
		
		$scope.machineId=id;
		$scope.machineName=name;
		$scope.machineImage=image;
		
		
/*		layer.open({
			type: 1,
			title:name+"运行实时数据",
			offset : 'auto',
			border : [ 10, 0.3, '#000', true ],
			area : ['800px','689px'],
			content:$("#realTime"),
			success:function(){
				createGauge(id);
				$interval(function(){
					getMonitor($scope.crName);
				},5000)
			},cancel:function(){
				window.location.reload();
			}
		})*/
		
		
	}
	
	function getMonitor(name){
		$.ajax({
			type :"GET",
			url :"/admin/logging/monitor?name="+name,
			async:true,
			cache : false,
			ifModified:true,
			success : function(data) {
				$scope.monitor=data;
			}
		})
	}
	
	function createGauge(id){
		var gauges= [];
		$.each($("#gauges").find("div"),function(){
			var myGauges=echarts.init(this);
			gauges.push(myGauges);
		})
		$http({
			method: 'GET',
			url:"/admin/logging/gauge.json?machineId="+id,
			cache:false,
			async:false}).then(function(res){
				for(var i=0;i<gauges.length;i++){
					gaugeOption.series[0].data[0]=res.data[i];
					gauges[i].setOption(gaugeOption);
				}
		})
		 
	
	}
	
	function pagination(totalElements,totalPages,number){
		$(".tcdPageCode").createPage({
			elementCount :totalElements,
			pageCount :totalPages,
			current :number,
			backFn : function(to){
				Fw.updateFilter(uuid, 'page', to);
			}
		})
	};
})