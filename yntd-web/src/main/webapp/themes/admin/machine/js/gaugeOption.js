var gaugeOption = {
		  backgroundColor: 'white',
			title : {
				top : 5,
				left : 'center',
				textStyle : {
					color : '#000'
				}
			},
			toolbox :{"feature":{"saveAsImage":{"show":true}},"top":10,"left":"right"},
			tooltip : {
				show : true,
				formatter : "{c}%",
				backgroundColor : '#F7F9FB',
				borderColor : '#92DAFF',
				borderWidth : '1px',
				textStyle : {
					color : 'black'
				}
			},
			series : [{
				name : '达标率',
				type : 'gauge',
				max : 150,
				radius : '75%', //图表尺寸
				center : [ "50%", "45%" ],
				axisLine : {
					show : true,
					lineStyle : {
						width : 5,
						shadowColor : 'rgba(0,0,0,0.4)',
						shadowOffsetY : 10,
						shadowBlur : 10,
						color : [ [ 0.3, '#DD3F36' ], [ 0.7, '#FAE521' ],
								[ 1, '#37B70C' ] ]
					}
				},
				axisTick : {
					show : false,
					splitNumber : 10
				},
				splitLine : {
					show : true,
					length : 10,
					lineStyle : {
						color : 'rgba(0,0,0,0.6)'
					}
				},
				axisLabel : {
					distance : 5,
					textStyle : {
						color : "#000",
						fontSize : 10,
					},
					formatter : function(e) {
						return e
					}
				},
				pointer : {
					show : true,
					length : '80%',
					width : 2
				},
				detail : {
					show : true,
					offsetCenter : [ 0, '80%' ],
					formatter:"{value}%",
					width : 56,
					height : 20,
					textStyle : {
						fontSize : 13,
						color : "black",
						fontWeight : 'bolder'
					},
					backgroundColor : '#A9DAFF',
					borderWidth : 2,
					borderColor : '#45E4D0',
					shadowColor : 'black', //默认透明
					shadowBlur : 5

				},
				title : {
					textStyle : {
						fontSize : 16,
						fontWeight : 'bolder',
						fontStyle : 'normal',
						color : "black"
					},
					offsetCenter : [ 0, '30%' ]
				},
				data:[{name:"asdasd",value:100}]
				
			}]
};