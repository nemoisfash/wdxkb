/**
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
;(function($,e){
var color =['#12b07b','#a6a5a5','#e65a65','#feb501']
var timeLineOption = {
	    dataZoom: [{
            type: 'slider',
            xAxisIndex: 0,
            filterMode: 'weakFilter',
            height: 20,
            bottom: 0,
            start: 0,
            end:100,
            handleIcon:"M8.2,13.6V3.9H6.3v9.7H3.1v14.9h3.3v9.7h1.8v-9.7h3.3V13.6H8.2z M9.7,24.4H4.8v-1.4h4.9V24.4z M9.7,19.1H4.8v-1.4h4.9V19.1z",
            handleSize: '80%',
            showDetail: true,
            backgroundColor:"#08132f8f",
            showDataShadow:true,
            textStyle:{
            	color:"#36b0f3",
            },
            dataBackground:{
            	lineStyle:{
            		color:"#08132f8f"
            	},
            	areaStyle:{
            		color:"#08132f8f"
            	}
            }
        },{
            type: 'inside',
            id: 'insideX',
            xAxisIndex: 0,
            filterMode: 'weakFilter',
            start: 0,
            end: 100,
            zoomOnMouseWheel: true,
            moveOnMouseMove: true
        },{
            type: 'slider',
            yAxisIndex: 0,
            zoomLock: false,
            width: 10,
            right: 10,
            top: 70,
            bottom: 20,
            start: 0,
            end: 100,
            handleIcon:"M8.2,13.6V3.9H6.3v9.7H3.1v14.9h3.3v9.7h1.8v-9.7h3.3V13.6H8.2z M9.7,24.4H4.8v-1.4h4.9V24.4z M9.7,19.1H4.8v-1.4h4.9V19.1z",
            handleSize: 0,
            showDetail: true,
            showDataShadow:true,
            backgroundColor:"#36b0f3",
            textStyle:{
            	color:"#36b0f3",
            },
            dataBackground:{
            	lineStyle:{
            		color:"#08132f8f"
            	},
            	areaStyle:{
            		color:"#08132f8f"
            	}
            }
        },{
            type: 'inside',
            id: 'insideY',
            yAxisIndex: 0,
            start:0,
            end: 100,
            zoomOnMouseWheel: true,
            moveOnMouseMove: true,
            moveOnMouseWheel: true
        }],
	    xAxis: {
	        type:'time',
	        position: 'top',
	        boundaryGap:['5%','5%'],
	        axisLine:{
	    		show:false,
	    		lineStyle:{
	    			color: ['#E9EDFF']
	    		}
	    	},
			splitLine: {
			    lineStyle: {
			        color: ['#E9EDFF']
			    }
			},
	        axisLabel: {
                textStyle: {
                    color: ['#E9EDFF'],//坐标值得具体的颜色
                }
            }
	    },
	    yAxis: {
	        data:[],
	        axisLabel: {
                textStyle: {
                    color: '#ffffff',//坐标值得具体的颜色
                }
            },
            axisLine:{
	    		show:true,
	    		lineStyle:{
	    			color: ['#E9EDFF']
	    		}
	    	},
	    	boundaryGap:['5%','5%']
	    },
	 /*tooltip: {
	        trigger: 'axis',
	        formatter: function(params) {
	            var res = params[0].name + "</br>"
	            var date0 = params[0].data;
	            var date1 = params[1].data;
	            var date2 = params[2].data;
	            var date3 = params[3].data;
	            date0 = date0.getFullYear() + "-" + (date0.getMonth() + 1) + "-" + date0.getDate();
	            date1 = date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate();
	            date2 = date2.getFullYear() + "-" + (date2.getMonth() + 1) + "-" + date2.getDate();
	            date3 = date3.getFullYear() + "-" + (date3.getMonth() + 1) + "-" + date3.getDate();
	            res += params[0].seriesName + "~" + params[1].seriesName + ":</br>" + date0 + "~" + date1 + "</br>"
	            res += params[2].seriesName + "~" + params[3].seriesName + ":</br>" + date2 + "~" + date3 + "</br>"
	            return res;
	        }
	    },*/
	    series: []
};
	var MyTimeLine=function(){
		var _self =this;
		this.init();
	}
	MyTimeLine.prototype={
		init:function(){
			this.initCharts();
		},extend: function(obj, obj2) {
			for ( var k in obj2) {
				obj[k] = obj2[k];
			}
			return obj;
		},initCharts:function(){
			var _this = this;
			var pannel= $("[time-line='true']").get(0);
			_this.timeLine=e.init(pannel);
			_this.timeLine.showLoading('default', {text:'数据统计中...',maskColor: '#07112a61',textColor: '#36b0f3',});
			setInterval(function(){
				_this.getOptionData();
			},1000*5)
		},getOptionData:function(){
			var _this = this;
			$.ajax({
				type :"GET",
				url :"/member/timeline.json",
				async:true,
				cache : false,
				success : function(data) {
					/*timeLineOption.xAxis[0].data=data.names;*/
					timeLineOption.yAxis.data=data.yAxis;
					timeLineOption.series=data.series;
					_this.updateOption();
					_this.timeLine.hideLoading(); 	
					_this.timeLine.setOption(timeLineOption);
				}
			})
	},updateOption:function(){
		var length=timeLineOption.series.length;
		for(var i=0;i<length;i++){
			var itemStyle= {
			    normal: {
			        color: color[i]
			  }
			}
			timeLineOption.series[i].itemStyle=itemStyle;
		}
	}
	}
	window.MyTimeLine = MyTimeLine;
})(jQuery, echarts)
