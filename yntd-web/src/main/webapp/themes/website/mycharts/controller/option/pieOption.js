/**
 * "RUNNING", "POWEROFF", "ALARM", "WAITTING","MANUAL"
 */
;(function($, e){
var optionPie = {
		color: ['#00FF00','#696969', '#DC143C','#FFFF00'],
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
		tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    },
		    legend: {
		    	show:false,
		        orient: 'vertical',
		        x: 'left',
		        data:['运行','停机','报警','等待'/*,'手动'*/],
		        top:"middle"
		    },
		    series: [
		         {
		            name:'',
		            type:'pie',
		            radius: ['45%', '70%'],
		            avoidLabelOverlap: false,
		            clockwise: true, //饼图的扇区是否是顺时针排布
		            minAngle:14,
		            minShowLabelAngle:20,
		            itemStyle: { //图形样式
		                normal:{
		                    borderColor:'#0b1647',
		                    borderWidth: 2,
		                    label:{
		                    	show:true,
		                    	position:"outside",
		                    	color:"auto",
		                    	formatter:'{d}%',
		                    }
		                },
		            },
		            /*label: {
		                normal: {
		                    show:false,
		                    position: 'center',
		                    formatter:'{c}\n时长占比{per|{d}%}',
		                    rich: {
		                        text: {
		                            fontSize: 10,
		                            align: 'center',
		                            verticalAlign: 'middle',
		                            padding: 3
		                        },
		                        value: {
		                            fontSize: 10,
		                            align: 'center',
		                            verticalAlign: 'middle',
		                        }
		                    }
		                  },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '12',
		                        fontWeight: 'lighter'
		                    }
		                }
		            },*/
		            labelLine: {
		                normal: {
		                    show:true
		                }
		            },
		        }
	 ]
};

var MyPies = function(){
	var	_this = this;
	_this.init();
}
MyPies.prototype={
init:function(){
	 this.initCharts();
},initCharts:function(){
	var _this=this;
	 var piesArray = new Array();
	$.each($("[echarts-type='pie']"),function(){
		piesArray.push(e.init(this));
	})
	_this.pies=piesArray;
	setInterval(function(){
		_this.getData();
	},3000)
},getData:function(){
	var _this=this;
			$.ajax({
				type :"GET",
				url :"/member/pie.json",
				async:true,
				cache : false,
				ifModified:true,
				success : function(data) {
					 _this.dataPieInit(data);
				}
			})
		
},dataPieInit:function(data){
	var _this=this;
	var pies = _this.pies;
	$.each(pies,function(i,obj){
		optionPie.title.text= data[i].machineName;
		optionPie.series[0].data=data[i].data;
		this.setOption(optionPie);
	})
}
}

window.MyPies = MyPies;
})(jQuery, echarts)
