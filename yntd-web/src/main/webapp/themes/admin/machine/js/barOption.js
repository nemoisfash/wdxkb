/**
 * 
 */
var barOption = {
	color: ['#00FF00','#696969', '#DC143C','#FFFF00'/*,'#7FFF00'*/],
	/*title:{
		text:"",
	},*/
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis:  {
        type: 'value',
        name:"持续时间:(单位:分钟)",
        show:true,
        nameTextStyle:{
    		fontSize:12
		},
    	axisLine:{
    		show:true,
    		lineStyle:{
    			opacity:0
    		}
    	}
    },
    yAxis: {
        type: 'category',
        data: ['m1','m2','m3','m4','m5','m6','m7'],
        name:"机台名称"
    },
    series: [{
        name: '直接访问',
        type: 'bar',
        stack: '总量',
        label: {
            normal: {
                show: true,
                position: 'insideRight'
            }
        },
        data: [320, 302, 301, 334, 390, 330, 320]
    },
    {
        name: '邮件营销',
        type: 'bar',
        stack: '总量',
        label: {
            normal: {
                show: true,
                position: 'insideRight'
            }
        },
        data: [120, 132, 101, 134, 90, 230, 210]
    },
    {
        name: '联盟广告',
        type: 'bar',
        stack: '总量',
        label: {
            normal: {
                show: true,
                position: 'insideRight'
            }
        },
        data: [220, 182, 191, 234, 290, 330, 310]
    },
    {
        name: '视频广告',
        type: 'bar',
        stack: '总量',
        label: {
            normal: {
                show: true,
                position: 'insideRight'
            }
        },
        data: [150, 212, 201, 154, 190, 330, 410]
    },
    {
        name: '搜索引擎',
        type: 'bar',
        stack: '总量',
        label: {
            normal: {
                show: true,
                position: 'insideRight'
            }
        },
        data: [820, 832, 901, 934, 1290, 1330, 1320]
    }]
};

var lable={
	normal:{
	     show: false,
	     position: 'insideRight'
	}
}