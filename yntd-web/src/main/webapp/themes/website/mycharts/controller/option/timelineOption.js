var timeLineOption=function createOption(){
	 return{
		    dataZoom: [{
		        type: 'slider',
		        filterMode: 'weakFilter',
		        showDataShadow: false,
		        top: 400,
		        height: 10,
		        borderColor: 'transparent',
		        backgroundColor: '#e2e2e2',
		        handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7v-1.2h6.6z M13.3,22H6.7v-1.2h6.6z M13.3,19.6H6.7v-1.2h6.6z', // jshint ignore:line
		        handleSize: 20,
		        handleStyle: {
		            shadowBlur: 6,
		            shadowOffsetX: 1,
		            shadowOffsetY: 2,
		            shadowColor: '#aaa'
		        },
		        labelFormatter: ''
		    }, {
		        type: 'inside',
		        filterMode: 'weakFilter'
		    }],
		    grid: {
		        height:300
		    },
		    xAxis: {
		    	min:0,
		        scale: true,
		        axisLabel: {
		            formatter: function (val) {
		                return Math.max(0, val - startTime) + ' ms';
		            }
		        }
		    },
		    yAxis: {
		        data:[]
		    },
		    series: [{
		        type: 'custom',
		        renderItem:{},
		        itemStyle: {
		            normal: {
		                opacity: 0.8
		            }
		        },
		        encode: {
		            x: [1, 2],
		            y: 0
		        },
		        data:[],
		    }]
	 }
}
