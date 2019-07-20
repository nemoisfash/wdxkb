package cn.hxz.webapp.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
 

public class JdbcUtils {
	
		private static final String SLAVE_DRIVER = "slave_driver";  
		private static final String SLAVE_URL = "slave_url";
		
		private static final String SLAVE_USER ="slave_user";
		private static final String SLAVE_PWD = "slave_pwd";
		
		private static final String SELECT_P = "select_p";
		
		private static final String confgFile = "lcdqtest/selver.properties";
		
		public static Connection conn = null;
		
		private static String getValue(String key) {
			Properties prop = new Properties();
			InputStream in = ExcelExportUtils.class.getClassLoader().getResourceAsStream(confgFile);
			try {
				prop.load(new InputStreamReader(in, "utf-8"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return "";
			}
			return prop.getProperty(key);
		}
		
		public static boolean getConnection() {
			 try {
				Class.forName(getValue(SLAVE_DRIVER));
				conn = DriverManager.getConnection(getValue(SLAVE_URL),getValue(SLAVE_USER),getValue(SLAVE_PWD));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public static Map<String,Object> selectResultSet(Map<String, Object> map) throws Exception {
			Map<String,Object> entity = new HashMap<String, Object>();
			String deviceNo= Objects.toString(map.get("deviceNo"),null);
			String filter="device_no ='"+deviceNo+"' "+"AND "+"DATE_FORMAT(update_time,'%Y-%m-%d %H:%m:%i') = DATE_FORMAT(now(),'%Y-%m-%d %H:%m:%i')";
			String sql="";
			String spString=SELECT_P;
			 PreparedStatement pstmt=null;
			 ResultSet rstSet = null;
			 for(int i=0;i<6;i++) {
				sql+=getValue(spString+i);
				System.out.println(sql);
			}
			try {
				if (conn == null) {
					throw new Exception("Database not connected!");
				}
			 
				pstmt = conn.prepareStatement(sql+filter);
				rstSet= pstmt.executeQuery();
				while (rstSet.next()) {
					entity.put("deviceNo",rstSet.getString("deviceNo"));
					entity.put("updateTime",rstSet.getString("updateTime"));
					entity.put("avgen",rstSet.getString("avgen"));
					entity.put("avgbc",rstSet.getString("avgbc"));
					entity.put("avgcv",rstSet.getString("avgcv"));
					entity.put("avgcc",rstSet.getString("avgcc"));
					entity.put("avgtemperature",rstSet.getString("avgtemperature"));
					entity.put("avgsignalstrength",rstSet.getString("avgsignalstrength"));
					entity.put("avgsk",rstSet.getString("avgsk"));
					entity.put("avgal",rstSet.getString("avgal"));
					entity.put("avgdb",rstSet.getString("avgdb"));
					entity.put("avgwd1",rstSet.getString("avgwd1"));
					entity.put("avgsd1",rstSet.getString("avgsd1"));
					entity.put("avgwd2",rstSet.getString("avgwd2"));
					entity.put("avgsd2",rstSet.getString("avgsd2"));
					entity.put("avgwsk",rstSet.getString("avgwsk"));
					entity.put("avgkgk",rstSet.getString("avgkgk"));
					entity.put("avgddz",rstSet.getString("avgddz"));
					System.out.println(entity);
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}finally {
				JdbcUtils.close(pstmt);
				JdbcUtils.close(rstSet);
			}
			     return entity;
		}
				
		public static  List<String> getDeviceNos() throws Exception{
				List<String> deviceNos =new ArrayList<String>();
				if(null==conn) {
					throw new Exception("Database not connected!");
				}
				PreparedStatement pstmt=null;
				ResultSet rstSet = null;
				String sql="SELECT DISTINCT device_no as deviceNo FROM tg_in_devices_lcdq";
				pstmt = conn.prepareStatement(sql);
				rstSet= pstmt.executeQuery();
				while (rstSet.next()){
					deviceNos.add(rstSet.getString("deviceNo"));
				}
				
				JdbcUtils.close(pstmt);
				JdbcUtils.close(rstSet);
				return deviceNos;
		};
		
		public void insert(String insertSql, Object[] params) throws Exception {
				PreparedStatement pstmt = null;
				try {
						if (null == conn) {
							throw new Exception("Database not connected!");
						}
							pstmt = conn.prepareStatement(insertSql);
						if(null != params){  
							for (int i = 0; i < params.length; i++) {  
								pstmt.setObject(i + 1, params[i]);  
							}
						}
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					JdbcUtils.close(pstmt);
				}
		}
		
		private static void close(PreparedStatement pstmt){
			if(pstmt != null){						 
				try{
					pstmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				
			}
		}
		
		public static void close(Connection conn){
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		private static void close(ResultSet rs){
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
		}
}