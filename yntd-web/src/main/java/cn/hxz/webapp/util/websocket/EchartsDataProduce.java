package cn.hxz.webapp.util.websocket;

import java.util.Map;

import org.springframework.util.CollectionUtils;

public class EchartsDataProduce {
		
		private static boolean isProduced=false;

		private static boolean isConsum=false;
		
		public String producers(String dataType) {
			String string=null;
			if(dataType.equals("ranking")) {
				createRankingData();
			}
			return string;
			
		}
		
	   synchronized private void createRankingData() {
			EchartsSupport esEchartsSupport = new EchartsSupport();
			Map<String,Object> ranking=  esEchartsSupport.ranking();
			if(CollectionUtils.isEmpty(ranking)){
				isProduced=true;
			}
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void  Consumers() {
			
			
		};
		
}
