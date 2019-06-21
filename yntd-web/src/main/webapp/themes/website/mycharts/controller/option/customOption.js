var optionCustom = {
	color: ['#32CD32', '#778899', '#B22222', '#F5DEB3'],
    tooltip : {
        trigger: 'axis',
        axisPointer : {            
            type : 'shadow'       
        }
    },
    legend: {
        data:['停机', '运行','报警','等待']
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            data:["2:00","3:00","4:00","5:00","6:00","7:00","8:00"]
        }
    ],
    yAxis : [
        {
            type : 'category',
            data : ['m1','m2','m3','m4','m5','m6','m7']
        }
    ],
    series : [
        {
            name:'直接访问',
            type:'custom',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[320, 302, 301, 334, 390, 330, 320]
        },
        {
            name:'邮件营销',
            type:'custom',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[120, 132, 101, 134, 90, 230, 210]
        },
        {
            name:'联盟广告',
            type:'custom',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[220, 182, 191, 234, 290, 330, 310]
        },
        {
            name:'视频广告',
            type:'custom',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[150, 212, 201, 154, 190, 330, 410]
        },
        {
            name:'搜索引擎',
            type:'custom',
            stack: '总量',
            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            data:[820, 832, 901, 934, 1290, 1330, 1320]
        }
    ]
};
                    