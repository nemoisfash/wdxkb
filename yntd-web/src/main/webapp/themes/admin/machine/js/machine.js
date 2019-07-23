var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope,$http,$interval) {
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
				createGauge(name);
				$interval(function(){
					getMonitor($scope.crName);
				},5000)
			},cancel:function(){
				window.location.reload();
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
				$scope.monitor=data;
			}
		})
	}
	
	function createGauge(name){
		var gauges= [];
		$.each($("#gauges").find("div"),function(){
			var myGauges=echarts.init(this);
			gauges.push(myGauges);
		})
		$http({
			method: 'GET',
			url:"/admin/logging/gauge.json?machineName="+name,
			cache:false,
			async:false}).then(function(res){
				console.info(res.data); 
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