/**
 * 西部大森 running=manual
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
	var optionLine = {
			color: ['#00FF00','#696969', '#DC143C','#FFFF00'/*,'#7FFF00'*/],
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		    	data:['运行','停机','报警','等待'/*,'手动'*/]
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },toolbox:{
		    	show:true,
		    	feature:{
		    		saveAsImage:{
		    			type:"png",
		    			show:true,
		    			title:'保存为图片'
		    		}
		    	}
		    	
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
		        boundaryGap: false,
		        name:"日期",
		        data: [],
		        axisLabel: {
	                textStyle: {
	                    color: '#33cc66',//坐标值得具体的颜色
	                }
	            }
		    },
		    yAxis: {
		        type: 'value',
		        name:'运行时间单位：分钟',
		        axisLabel: {
	                textStyle: {
	                    color: '#33cc66',//坐标值得具体的颜色
	                }
	            }
		    },
			series: [{
	            name:'正常运行',
	            type:'line',
			    data:[120, 132, 101]
			}]
	};