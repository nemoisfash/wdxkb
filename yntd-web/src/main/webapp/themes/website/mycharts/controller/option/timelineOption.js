/**
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
;(function($,e){
var timeLineOption = {
	    baseOption:{
	        timeline: {
	            axisType: 'category',
            autoPlay: true,
            timeLineIndex:0,
            playInterval:3000,
            data: ['08:00','9:00','10:00', '11:00', '12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','19:00','20:00','21:00','22:00','23:00','24:00'],
            label:{
            	color:"#FFFFFF"
            }
	    },
        title: {
        },
        calculable: true,
        grid: {
            top: 80,
            bottom: 100
        },
        xAxis: [{
            type: 'category',
            name:'设备名称',
            nameTextStyle:{
        		color:"#FFFFFF",
        		fontSize:12
            },
            axisLabel: {
                nterval: 0,
                textStyle: {
	                color: '#33cc66',//坐标值得具体的颜色
	            }
            },
            data: [],
            splitLine: {
                show: true,
                interval:0
            }
        }],
        yAxis: [{
        	type: 'value',
        	name:'单位：分钟',
        	nameTextStyle:{
        		color:"#FFFFFF",
        		fontSize:12
    		},	
    		axisLabel: {
	            textStyle: {
	                color: '#33cc66',//坐标值得具体的颜色
	            }
        }}],
        series: [{
					name: '正常运行',
					yAxisIndex: 0,
					type:"bar",
					itemStyle:{
						color:"#00FF00"
					}
					
        		},{
					name: '停机',
					yAxisIndex: 0,
					type:"bar",
					itemStyle:{
						color:"#696969"
					}
				},{
				    name: '报警',
					yAxisIndex: 0,
					type: 'bar',
					itemStyle:{
						color:"#DC143C"
					}
				}, {
					name: '等待',
					yAxisIndex: 0,
					type: 'bar',
					itemStyle:{
						color:"#FFFF00"
					}
				}/*, {
					name:'手动',
					yAxisIndex: 0,
					type: 'bar',
					itemStyle:{
						color:"#7FFF00"
					}
        }*/] },
    options:[]
};
	
	var MyTimeLine=function(){
		var _self =this;
		this.init();
	}
	MyTimeLine.prototype={
		init:function(){
			this.initCharts();
			this.event();
		},extend : function(obj, obj2) {
			for ( var k in obj2) {
				obj[k] = obj2[k];
			}
			return obj;
		},initCharts:function(){
			var pannel= $("[time-line='true']").get(0);
			this.timeLine=e.init(pannel);
		},event:function(data){
			var _this = this;
			_this.timeLine.on('timelinechanged',function(index){
				var time=timeLineOption.baseOption.timeline.data[index.currentIndex]; 
				_this.getOptionData(time);
			})
				_this.timeLine.setOption(timeLineOption);
		},getOptionData:function(time){
			var _this = this;
			console.info(time);
			$.ajax({
				type :"GET",
				url :"/member/timeline.json?time="+time,
				async:true,
				cache : false,
				success : function(data) {
					console.info("timeLine"+"/",data.names)
					console.info("timeLine"+"/",data.options)
					timeLineOption.baseOption.xAxis[0].data=data.names;
					timeLineOption.options=data.options;
					_this.timeLine.setOption(timeLineOption);
				}
			})
		}
	}
	window.MyTimeLine = MyTimeLine;
})(jQuery, echarts)
