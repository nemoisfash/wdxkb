/**
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
;(function($,e){
var color =['#12b07b','#feb501', '#e65a65','#a6a5a5']

var timeLineOption = {
	    grid: {
	        containLabel: true,
	        left: 20
	    },
	    xAxis: {
	        type:'time',
	        axisLine:{
	    		show:true,
	    		lineStyle:{
	    			opacity:0
	    		}
	    	},
	        axisLabel: {
                textStyle: {
                    color: '#33cc66',//坐标值得具体的颜色
                }
            }
	    },
	    yAxis: {
	        data:[],
	        axisLabel: {
                textStyle: {
                    color: '#33cc66',//坐标值得具体的颜色
                }
            }
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
	    series: [{
	            name: '运行',
	            type: 'bar',
	            stack: '总量',
	            data: []
	        },{
	            name: '停机',
	            type: 'bar',
	            stack: '总量',
	            itemStyle: {
	                normal: {
	                    color: '#696969'
	                }
	            },
	            data: []
	        },{
	            name: '报警',
	            type: 'bar',
	            stack: '总量',
	            itemStyle: {
	                normal: {
	                    color: '#DC143C'
	                }
	            },
	            data: []
	            },
	            {
	            name: '等待',
	            type: 'bar',
	            stack: '总量',
	            data: []
	        }]
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
			_this.getOptionData();
		},getOptionData:function(){
			var _this = this;
			$.ajax({
				type :"GET",
				url :"/member/timeline.json",
				async:true,
				cache : false,
				success : function(data) {
					/*timeLineOption.xAxis[0].data=data.names;*/
					console.info(data.yAxis);
					timeLineOption.yAxis.data=data.yAxis;
					timeLineOption.series=data.series;
					_this.updateOption();
					_this.timeLine.setOption(timeLineOption);
				}
			})
	},updateOption:function(){
			var length=timeLineOption.series.length;
			console.info(length);
			console.info(typeof timeLineOption);
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
