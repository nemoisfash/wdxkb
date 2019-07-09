var lineOption={
	/*backgroundColor:'#11183c',*/
	grid: {
	    top: '8%',
	left: '2%',
	right: '2%',
	bottom: '0%',
	    containLabel: true,
	},
	xAxis: {
	type: 'category',
	boundaryGap: false,
	data: ['14', '15', '16', '17', '18', '19', '20', '21', '22', '23'],
	axisLabel: {
	    margin: 30,
	    color: '#B8B8B8'
	},
	axisLine: {
	    show: false
	},
	axisTick: {
	    show: true,
	    length: 25,
	    lineStyle: {
	        color: "#B8B8B8"
	    }
	},
	splitLine: {
	    show: true,
	    lineStyle: {
	        color: '#B8B8B8'
	        }
	    }
	},
	yAxis: [{
	    type: 'value',
	position: 'right',
	axisLabel: {
	    margin: 20,
	    color: '#B8B8B8'
	},
	
	axisTick: {
	    show: true,
	    length: 15,
	    lineStyle: {
	        color: "#B8B8B8",
	    }
	},
	splitLine: {
	    show: true,
	    lineStyle: {
	        color: '#B8B8B8'
	    }
	},
	axisLine: {
	    lineStyle: {
	        color: '#B8B8B8',
	            width: 2
	        }
	    }
	}],
	series: [{
	name: '注册总量',
	type: 'line',
	smooth: true, //是否平滑曲线显示
	showAllSymbol: true,
	symbol: 'circle',
	symbolSize: 6,
	lineStyle: {
	    normal: {
	        color: "#00a65a", // 线条颜色
	    },
	},
	label: {
	    show: true,
	    position: 'top',
	textStyle: {
	    color: '#00a65a',
	    }
	},
	itemStyle: {
	    color: "#00a65a",
	borderColor: "#00a65a",
	    borderWidth: 3
	},
	tooltip: {
	    show: false
	},
	areaStyle: {
	    normal: {
	        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
	                offset: 0,
	                color: '#00a65aa1'
	},{
	    offset: 1,
	    color: '#3fbbff0d'
	                }
	            ], false),
	        }
	    },
	    data: [393, 438, 485, 631, 689, 824, 987, 1000, 1100, 1200,393,393, 438, 485, 631, 689, 824, 987, 1000, 1100, 1200,393, 438, 485, 631, 689, 824, 987, 1000, 1100, 1200]
	}]
		
}