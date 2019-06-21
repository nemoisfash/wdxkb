;(function($, e) {
var myColor = ['#eb2100', '#eb3600', '#d0570e', '#d0a00e', '#34da62', '#00e9db', '#00c0e9', '#0096f3', '#33CCFF', '#33FFCC'];
var optionBar = {
	    grid: {
	        left: '11%',
	        top: '12%',
	        right: '0%',
	        bottom: '8%',
	        containLabel: true
	    },
	    xAxis: [{
	        show: false,
	    }],
	    yAxis: [{
	        axisTick: 'none',
	        axisLine: 'none',
	        offset: '27',
	        axisLabel: {
	            textStyle: {
	                color: '#ffffff',
	                fontSize: '16',
	            }
	        },
	        data: ['南昌转运中心', '广州转运中心', '杭州转运中心', '宁夏转运中心', '兰州转运中心', '南宁转运中心', '长沙转运中心', '武汉转运中心', '合肥转运中心', '贵州转运中心']
	    }, {
	        axisTick: 'none',
	        axisLine: 'none',
	        axisLabel: {
	            textStyle: {
	                color: '#ffffff',
	                fontSize: '16',
	            }
	        },
	        data: ['', '', '', '第三名', '第二名', '第一名']
	    }, {
	        nameGap: '50',
	        nameTextStyle: {
	            color: '#ffffff',
	            fontSize: '16',
	        },
	        axisLine: {
	            lineStyle: {
	                color: 'rgba(0,0,0,0)'
	            }
	        },
	        data: [],
	    }],
	    series: [{
	            name: '条',
	            type: 'bar',
	            yAxisIndex: 0,
	            data: [4, 13, 25, 29, 38, 44, 50, 52, 60, 72],
	            label: {
	                normal: {
	                    show: true,
	                    position: 'right',
	                    textStyle: {
	                        color: '#ffffff',
	                        fontSize: '16',
	                    }
	                }
	            },
	            barWidth: 12,
	            itemStyle: {
	                normal: {
	                    color: function(params) {
	                        var num = myColor.length;
	                        return myColor[params.dataIndex % num]
	                    },
	                }
	            },
	            z: 2
	        }, {
	            name: '白框',
	            type: 'bar',
	            yAxisIndex: 1,
	            barGap: '-100%',
	            data: [100000, 100000, 100000, 100000, 100000, 100000],
	            barWidth: 20,
	            itemStyle: {
	                normal: {
	                    color: '#0e2147',
	                    barBorderRadius: 5,
	                }
	            },
	            z: 1
	        }, {
	            name: '外框',
	            type: 'bar',
	            yAxisIndex: 2,
	            barGap: '-100%',
	            data: [100000, 100000, 100000, 100000, 100000, 100000],
	            barWidth: 24,
	            itemStyle: {
	                normal: {
	                    color: function(params) {
	                        var num = myColor.length;
	                        return myColor[params.dataIndex % num]
	                    },
	                    barBorderRadius: 5,
	                }
	            },
	            z: 0
	        },
	        {
	            name: '外圆',
	            type: 'scatter',
	            hoverAnimation: false,
	            data: [0, 0, 0, 0, 0, 0],
	            yAxisIndex: 2,
	            symbolSize: 35,
	            itemStyle: {
	                normal: {
	                    color: function(params) {
	                        var num = myColor.length;
	                        return myColor[params.dataIndex % num]
	                    },
	                    opacity: 1,
	                }
	            },
	            z: 2
	        }
	    ]
	};

var Ranking = function(){
var	_this = this;
	_this.init();
}
	Ranking.prototype={
		init:function(){
			 this.initCharts();
		},initCharts:function(){
		 this.ranking=e.init($("[data-ranking='true']").get(0));
		 this.getData();
		},getData:function(){
			var _this = this;
			setInterval(function(){
				$.ajax({
					type :"GET",
					url :"/member/ranking.json",
					async:true,
					cache : false,
					ifModified:true,
					success : function(data) {
						_this.dataRankingInit(data);
					}
				})
			},3000)
		},dataRankingInit:function(data){
			console.info("ranking/"+data)
			var _this = this;
			var targetData= new Array;
			var _otherData = new Array();
			var names=new Array();
			 $.each(data,function(){
				 var name, val;
				 for(var key in this){
					 name =key
					 val=this[key]
				 }
				 names.push(name);
				 targetData.push(val);
			 })
			
			optionBar.series[0].data=targetData.reverse();
			optionBar.yAxis[0].data=names.reverse();
			 var max = Math.max.apply(null, targetData);
			if(1500-max<100){
				 $.each(optionBar.series[1].data,function(){
					 _otherData.push(max+1000);
				 })
				 optionBar.series[1].data=_otherData;
				 optionBar.series[2].data=_otherData;
			}
			_this.ranking.setOption(optionBar);
		}
	}
	
window.Ranking = Ranking;
})(jQuery, echarts)
