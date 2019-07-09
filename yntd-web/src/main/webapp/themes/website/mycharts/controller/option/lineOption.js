/**
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
;(function($, e) {
	var optionLine = {
			color: ['#00FF00','#696969', '#DC143C','#FFFF00'],
		    tooltip: {
		        trigger: 'axis'
		    },
		    grid:{
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		  dataZoom: [
	               {   // 这个dataZoom组件，默认控制x轴。
	                   type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
	                   xAxisIndex: 0,
	                   start: 0,      // 左边在 10% 的位置。
	                   end: 100         // 右边在 60% 的位置。
	               },
		    ],
		    xAxis: {
		        type: 'category',
	            axisLine: {onZero: true},
	            name:"日期",
	            nameTextStyle:{
	        		color:"#FFFFFF",
	        		fontSize:12
	    		},
		        boundaryGap: false,
		        data: [],
		        axisLabel: {
	                textStyle: {
	                    color: '#33cc66',//坐标值得具体的颜色
	                }
	            }
		    },
		    yAxis: {
		        type: 'value',
		        axisLabel: {
	                textStyle: {
	                    color: '#33cc66',//坐标值得具体的颜色
	                }
	            },
	            name:'运行时间单位：分钟',
	            nameTextStyle:{
	        		color:"#FFFFFF",
	        		fontSize:12
	    		},	
		    },
			series: [{
	            name:'正常运行',
	            type:'line',
			    data:[120, 132, 101]
			}]
	};

	var MyLine = function() {
		 this.init();
	}
	MyLine.prototype = {
		init:function(){
			 this.convertCharts();
		},convertCharts: function(){
			var _this = this;
			_this.line=e.init($("[echarts-type='line']").get(0));
			setInterval(function(){
				_this.getData();
			},10000)
		},getData:function(data){
				var _this = this;
				$.ajax({
					type :"GET",
					url :"/member/line.json",
					async:true,
					cache : false,
					ifModified:true,
					success : function(data) {
						_this.dataLineInit(data.xAxis,data.series);
					}
				})
			
		},dataLineInit:function(xAxis,series){
			optionLine.xAxis.data = xAxis;
			optionLine.series[0] = series.data;
			this.line.setOption(optionLine);
		}
	}
	
window.MyLine = MyLine;
})(jQuery, echarts)


