var dom = document.getElementById("timeLine");
			var myChart = echarts.init(dom);
			var app = {};
			option = null;
			
			var data = [];
			var dataCount = 10;
			var startTime = +new Date();
			var categories = [];
			 
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
			
			function testFun(){
			var arr= new Array();
				for(var hours=0;hours<24;hours++){
					for(var min=0;min<60;min++){
						for(var sec=0;sec<60;sec++){
							var bu_h,bu_m,bu_s;
							hours<10?bu_h="0"+hours:bu_h=String(hours);
							min<10?bu_m="0"+min:bu_m=String(min);
							sec<10?bu_s="0"+sec:bu_s=String(sec);
							arr.push(bu_h+":"+bu_m+":"+bu_s);
							}
						}
					}
				return arr;
				}
			
			option = {
			    tooltip: {
			        formatter: function (params) {
			            return params.marker + params.name + ':' + params.value[3] + '分钟';
			        }
			    },
			     dataZoom: [{
			            type: 'slider',
			            xAxisIndex: 0,
			            filterMode: 'weakFilter',
			            height: 20,
			            bottom: 0,
			            start: 0,
			            end: 100,
			            handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
			            handleSize: '80%',
			            showDetail: true
			        }, {
			            type: 'inside',
			            id: 'insideX',
			            xAxisIndex: 0,
			            filterMode: 'weakFilter',
			            start: 95,
			            end: 100,
			            zoomOnMouseWheel: true,
			            moveOnMouseMove: true
			        }],
				    xAxis:[{
				        show: true,
				        position: 'top',
				        data:testFun(),
				        splitLine:{
				        	show:true
				        },
				        axisLabel: {
				            textStyle: {
				                color: '#ffffff',
				                fontSize: '10',
				            }
				        },
				        axisLine:{
				    		show:true,
				    		lineStyle:{
				    			color: ['#E9EDFF']
				    		}
				    	},
				    }],
			    yAxis: {
			    	type:"category",
			    	boundaryGap: ['20%', '20%'],
			    	min: 0,
		            max:8,
		            axisTick: 'none',
			    	axisLine: {
			                show: true,
			                lineStyle: {
			                    color: '#929ABA'
			                }
			        },
			        axisLabel: {
			            textStyle: {
			                color: '#ffffff',
			                fontSize: '10',
			            }
			        },
			        data: []
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
			};
			
			if (option && typeof option === "object") {
				setInterval(function(){
					$.ajax({
						type :"GET",
						url :"/member/timeLine.json",
						async:true,
						cache : false,
						ifModified:true,
						success : function(data) {
							option.yAxis.data=data.categories;
							option.yAxis.max=data.categories.length;
							option.series[0].data=data.data;
							console.info(data.data);
							myChart.setOption(option, true);
						}
					})
				},10000)
			}