/**
 * 
 */
var gaugeOption = {
    tooltip : {
        formatter: "{a} <br/>{b} : {c}%"
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    series:[{
    		data: [{value: 50, name: '完成率'}],
            type: 'gauge',
            radius: '80%',
            center: ['50%', '50%'],
            min: 0,
            max: 1440,
            splitNumber:10,
            axisLine: {
                lineStyle: {
                    width: 10,
                    color: [[0.2, '#62c87f'], [0.8, '#5d9cec'], [1, '#f15755']]
                }
            },axisTick: {
                length: 9,  
                lineStyle: { 
                    color: 'auto'
                }
            },splitLine: {
                length: 18,
                lineStyle: {
                    color: 'auto'
                }
            },axisLabel: {
                color: 'green',
            },detail:{
                formatter:'{value}分钟',
                fontSize: 24,
                offsetCenter: [0, '85%']
              },title:{
                  fontSize: 12,
                  color:'#56e308',
                  offsetCenter:[0, '55%']
                },
        }]
    
};

