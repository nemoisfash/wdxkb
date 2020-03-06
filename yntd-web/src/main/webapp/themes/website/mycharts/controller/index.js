/**
 * 初始化图表
 */
initCharts()
function initCharts() {
	var pies = new MyPies();
	var ranking = new Ranking();
	var myTimeLine=  new MyTimeLine();
		if(localStorage.getItem("pies")!=null){
			var	piesData = JSON.parse(localStorage.getItem("pies"));
			  pies.dataPieInit(piesData["content"]);
		}
		if(localStorage.getItem("ranking")!=null){
			var	rankingData = JSON.parse(localStorage.getItem("ranking"));
			ranking.dataRankingInit(rankingData["content"]);
		}
		if(localStorage.getItem("timeLine")!=null){
			var	timeLineData = JSON.parse(localStorage.getItem("timeLine"));
			myTimeLine.dataTimeLineInit(timeLineData["categories"],timeLineData["seriesdata"]);
		}
		
	 setTimeout(function(){
		initCharts()
	 },10000)
} 
