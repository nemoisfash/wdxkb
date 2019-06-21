var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$interval) {
var uuid=$("#uuid").val();
		$http({
				method: 'GET',
				url:"/admin/machine/data.json",
				cache:false,
				async:false
			}).then(function(res){
				$scope.items=res.data.resault;
				$scope.count=res.data.count;
		})
		
/*		$scope.addMachine=function(){
			layer.open({
			  type: 2,
			  title: false,
			  closeBtn: 0, //不显示关闭按钮
			  shade: [0],
			  area: ['340px', '215px'],
			  offset: 'rb', //右下角弹出
			  time: 2000, //2秒后自动关闭
			  anim: 2,
			  content: ['test/guodu.html', 'no'], //iframe的url，no代表不显示滚动条
			});
		}*/
		
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
			/*$scope.exportData(id);*/
			 /*if(typeof id=="undefined"){
				 id=0;
			 }
			 window.location.href= "/admin/machine/exportdata.json?node="+"machine_"+"&id="+id;*/
		}
		
		/*$scope.exportData=function(id){
			
			window.location.href= "/admin/machine/exportdata.json?node="+"machine_"+"&id="+id;
		}*/
		
		$scope.search=function(){
			var val= $scope.value;
			var key= $scope.selected;
			if(typeof val=="undefined"){
				layer.msg("请输入需要查询的内容");
				$scope.removeFilter(key);
				return;
			}else{
				$scope.updateFilter(key,val);
			}
		}
		
		$scope.updateFilter=function(key,val){
			Fw.updateFilter(uuid,key,val);
		}
		$scope.removeFilter=function(key){
			Fw.removeFilter(uuid,key);
		}
		$scope.keys=[{key:"name",value:"名称"},
		             {key:"machineNo",value:"机台编号"}]
		
		$scope.logingData=function(id,name,code,ip,image){
			layer.open({
				type: 1,
				title:"设备每日运行数据统计",
				offset : 'auto',
				border : [ 10, 0.3, '#000', true ],
				area : ['750px','689px'],
				content:$("#logging"),
				success:function(){
					$scope.loggingName=name;
					$scope.loggingCode=code;
					$scope.loggingIp=ip;
					$scope.loggingImage=image;
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
	
	$scope.realTimeData=function(id,name){
		$scope.crName=name;
		layer.open({
			type: 1,
			title:name+"运行实时数据",
			offset : 'auto',
			border : [ 10, 0.3, '#000', true ],
			area : ['800px','689px'],
			content:$("#realTime"),
			success:function(){
				createGauge(id);
				$interval(function(){
					console.info($scope.crName);
					getMonitor($scope.crName);
				},2000)
			}
		})
	}
	
	function getMonitor(name){
		$.ajax({
			type :"GET",
			url :"/admin/logging/monitor?name="+name,
			async:true,
			cache : false,
			ifModified:true,
			success : function(data) {
				console.info(data);
				$scope.monitor=data;
			}
		})
	}
	
	function createGauge(id,name){
		var gaugeArrys=new Array();
		$.each($("[name='gauge']"),function(){
			gaugeArrys.push(echarts.init(this));
		})
		
		$interval(function(){
			$.ajax({
				type :"GET",
				url :"/admin/logging/gauge?id="+id,
				async:true,
				cache : false,
				ifModified:true,
				success : function(data) {
					 for(var i=0;i<gaugeArrys.length;i++){
						 gaugeOption.series[0].data=data[i].data;
						 gaugeArrys[i].setOption(gaugeOption);
					 }
				}
			})
		},4000)
	}
	
	$(function(){
	    new ZouMa().Start();
	});

	function ZouMa() {
		this.maxLength = 3;  
		this.Timer = 6000; 
		this.Ul = $("div#gauges");
		var handId; 
		var self = this;
		this.Start = function () {
		    if (self.Ul.children().length < this.maxLength) {
		        self.Ul.append(self.Ul.children().clone());
		    }
		    handId = setInterval(self.Play, self.Timer);
		}
			this.Play = function () {
		    var li = self.Ul.children("div").eq(0);
		    var left = li.eq(0).width();
		    li.animate({ "marginLeft": (-1 * left) + "px" }, 1000, function () {
				$(this).css("margin-left", "auto").appendTo(self.Ul);
	        });
	    }
	}
	  
})