/**
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
;(function($, e){
	function renderItem(params, api) {
	    var categoryIndex = api.value(0);
	    var start = api.coord([api.value(1), categoryIndex]);
	    var end = api.coord([api.value(2), categoryIndex]);
	    var height = api.size([0, 1])[1] * 0.6;
	    var rectShape = echarts.graphic.clipRectByRect({
	        x: start[0],
	        y: start[1] - height / 2,
	        width: end[0] - start[0],
	        height: height
	    }, {
	        x: params.coordSys.x,
	        y: params.coordSys.y,
	        width: params.coordSys.width,
	        height: params.coordSys.height
	    });
	
	    return rectShape && {
	        type: 'rect',
	        shape: rectShape,
	        style: api.style()
	    };
	}
	
	var option={
		    tooltip: {
		        formatter: function (params) {
		            return params.marker + params.name + ':' + params.value[3] + '分钟';
		        }
		    },
		    grid: {
		        left: '10%',
		        top: '12%',
		        right: '2%',
		        bottom: '8%',
		        containLabel: false
		    },
	        xAxis:{
		            type: 'time',
		            position: 'top',
		            splitLine: {
		                lineStyle: {
		                    color: ['#34a9eb']
		                }
		            },
		            axisLine: {
		                show: false,
		                lineStyle: {
		                    color: '#34a9eb'
		                }
		            },
		            axisTick: {
		                lineStyle: {
		                    color: '#34a9eb'
		                }
		            },
		            axisLabel: {
		                color: '#34a9eb',
		                inside: false,
		                align: 'center'
		            }
		    },
		    yAxis: {
		    	type:"category",
		    	boundaryGap: ['20%', '20%'],
	            position:'left',
		    	axisLine: {
	                show:true,
	                lineStyle: {
	                    color: '#929ABA'
	                }
		        },
		        axisTick:{
		        	show:true,
		        	interval:0,
		        	length:2
		        },
		        axisLabel: {
		        	interval:0,
		        	margin:10,
		            textStyle: {
		                color: '#34a9eb',
		                fontSize: '8',
		            }
		        },
		        data:[],
		    },
		    series: [{
		        type: 'custom',
		        renderItem: renderItem,
		        itemStyle: {
		            normal: {
		                opacity: 0.8
		            }
		        },
		        encode: {
		            x: [1, 2],
		            y: 0
		        },
		        data: []
		    }]
	}
	
var MyTimeLine = function(){
	var	_this = this;
	_this.init();
}
	
MyTimeLine.prototype={
			init:function(){
				 this.initCharts();
			},initCharts:function(){
				var _this=this;
				_this.timeLine=e.init($("#timeLine").get(0));
				
			},dataTimeLineInit:function(timeLineCategories,timeLineSeriesData){
				var _this=this;
				option.yAxis.data=timeLineCategories
				var c=option.series[0].data.concat(timeLineSeriesData);
				option.series[0].data=c;
				_this.timeLine.setOption(option,{
				    notMerge:true,
				    lazyUpdate:false,
				    silent:false
				});
			}
}
window.MyTimeLine = MyTimeLine;
})(jQuery, echarts)
