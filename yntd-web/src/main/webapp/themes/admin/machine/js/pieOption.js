/**
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
var optionPie = {
		color: ['#00FF00','#696969', '#DC143C','#FFFF00'/*,'#7FFF00'*/],
		title:{
			text:"",
			textStyle:{
				color:"#33cc66",
				fontWeight:"lighter",
				fontSize:14,
			},
			left:'38%',
			top:'40%'
		},
		legend: {
	        data:['运行','停机','报警','等待'/*,'手动'*/]
	    },
		tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    },
		    toolbox:{
		    	show:true,
		    	feature:{
		    		saveAsImage:{
		    			type:"png",
		    			show:true,
		    			title:'保存为图片'
		    		}
		    	}
		    	
		    },legend: {
		    	show:false,
		        orient: 'vertical',
		        x: 'left',
		        data:['运行','报警','等待'],
		        top:"middle"
		    },
		    series: [
		         {
		            name:'',
		            type:'pie',
		            radius: ['30%', '50%'],
		            avoidLabelOverlap: false,
		            clockwise: true, //饼图的扇区是否是顺时针排布
		            minAngle:14,
		            minShowLabelAngle:20,
		            itemStyle: { //图形样式
		                normal:{
		                    borderColor:'#ffffff',
		                    borderWidth: 2,
		                    label:{
		                    	show:true,
		                    	position:"outside",
		                    	color:"auto",
		                    	formatter:'{d}%',
		                    }
		                },
		            },
		            labelLine: {
		                normal: {
		                    show:true
		                }
		            },
		        }
	 ]
};