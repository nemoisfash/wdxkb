package net.chenke.playweb.support.dynamicDataSource;

public class DataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
	
	// 设置数据源名
	 public static void setDB(String dbType) {
	  System.out.println("切换到{"+dbType+"}数据源");
	  	contextHolder.set(dbType);
	  }
	 
	 public static String getDB() {
		return contextHolder.get();
	 }
	 
	 public static void clearDB(){
		 contextHolder.remove();
	 }
}
