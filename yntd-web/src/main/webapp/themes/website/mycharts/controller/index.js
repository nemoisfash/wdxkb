/**
 * 初始化图表
 */
initCharts()
function initCharts() {
	var pies = new MyPies();
	var ranking = new Ranking();
	var myTimeLine=  new MyTimeLine();
	setInterval(function(){
		if(localStorage.getItem("pies")!=null){
			var	piesData = JSON.parse(localStorage.getItem("pies"));
			  console.info(piesData["content"]);
			  pies.dataPieInit(piesData["content"]);
		}
		if(localStorage.getItem("ranking")!=null){
			var	rankingData = JSON.parse(localStorage.getItem("ranking"));
			ranking.dataRankingInit(rankingData["content"]);
		}
		if(localStorage.getItem("timeLineCategories")!=null && localStorage.getItem("timeLineSeriesData")!=null){
			var	timeLineCategoriesData = JSON.parse(localStorage.getItem("timeLineCategories"));
			var seriousData = JSON.parse(localStorage.getItem("timeLineSeriesData"));
			console.info(seriousData);
			myTimeLine.dataTimeLineInit(timeLineCategoriesData["content"],seriousData["content"]);
		}
	},1000)
} 
