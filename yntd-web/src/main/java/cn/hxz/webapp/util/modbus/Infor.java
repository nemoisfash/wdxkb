package cn.hxz.webapp.util.modbus;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Infor {
	
	private static Connection connect()
    { 
        Connection conn = null;//定义数据库连接对象

        try {
            String url = "jdbc:sqlite:"+"";  //定义连接数据库的url(url:访问数据库的URL路径),test为数据库名称
             Class.forName("org.sqlite.JDBC");//加载数据库驱动
            conn = DriverManager.getConnection(url);    //获取数据库连接
        } 
        //捕获异常信息
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("数据库连接失败！"+e.getMessage());
        }
        return conn;//返回一个连接
    }

	
	public static void close(){
		if(connect()!=null){
			try {
				connect().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 实时数据
	 * @param name
	 * @return
	 */
    public ResultSet selectAll(String name) {  
    	//选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
        String sql="Select * from Monitoring_List where Machine_Name = '"+name+"'";
        //将从表中查询到的的所有信息存入sql
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();//得到Statement实例
            ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集
            // 当返回的结果集不为空时，并且还有记录时，循环输出记录
            //System.out.println(rs.getString("Machine_Name") + "\n" + rs.getString("Update_Date")+ "\n" +rs.getInt("Update_Time"));
            return rs;
        } 
        catch (SQLException e) {
            System.out.println("查询数据时出错！"+e.getMessage());
        }
		return null;
    }

    /**
     * 历史数据
     * @param name
     * @return
     */
    public List<Map<String, Object>> hisdata(String count,String name) {   //选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	String a = "";
    	if(name != null && !name.equals("")){
    		a = "where Machine_Name = '"+name+"'";
    	}
    	String sql="Select * from Running_Log "+a+" ORDER BY Record_Date , Record_Time DESC limit 0,"+count;//将从表中查询到的的所有信息存入sql
    	try {
    		Connection conn = this.connect();
    		Statement stmt = conn.createStatement();//得到Statement实例
    		ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集
    		ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
    		int columnCount = md.getColumnCount();   //获得列数 
    		// 当返回的结果集不为空时，并且还有记录时，循环输出记录
    		//System.out.println(rs.getString("Machine_Name") + "\n" + rs.getString("Update_Date")+ "\n" +rs.getInt("Update_Time"));
    		while (rs.next()) {
    			Map<String,Object> rowData = new HashMap<String,Object>();
    			for (int i = 1; i <= columnCount; i++) {
    				rowData.put(md.getColumnName(i), rs.getObject(i));
    			}
    			list.add(rowData);
    		}
    		return list;
    	} 
    	catch (SQLException e) {
    		System.out.println("查询数据时出错！"+e.getMessage());
    	}
    	return null;
    }
    
    /**
	 * 实时状态
	 * @param name
	 * @return
	 */
    public ResultSet status(String name) {   //选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
        String sql="Select Machine_Signal from Monitoring_List where Machine_Name = '"+name+"'";//将从表中查询到的的所有信息存入sql
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();//得到Statement实例
            ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集
            // 当返回的结果集不为空时，并且还有记录时，循环输出记录
            //System.out.println(rs.getString("Machine_Name") + "\n" + rs.getString("Update_Date")+ "\n" +rs.getInt("Update_Time"));
            return rs;
        } 
        catch (SQLException e) {
            System.out.println("查询数据时出错！"+e.getMessage());
        }
		return null;
    }
    
    
    /**
     * 数据报表
     * @param name
     * @return
     */
    public List<Map<String, Object>> report(String time , String name) {   //选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	String a = "";
    	if(name != null && !name.equals("")){
    		a = "where Machine_Name = '"+name+"' AND Record_Date = '"+ time +"'";
    	}
    	String sql="Select Machine_Signal,Record_Time from Running_Log "+a+" ORDER BY Record_Date , Record_Time ASC";//将从表中查询到的的所有信息存入sql
    	try {
    		Connection conn = this.connect();
    		Statement stmt = conn.createStatement();//得到Statement实例
    		ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集
    		ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
    		int columnCount = md.getColumnCount();   //获得列数 
    		// 当返回的结果集不为空时，并且还有记录时，循环输出记录
    		//System.out.println(rs.getString("Machine_Name") + "\n" + rs.getString("Update_Date")+ "\n" +rs.getInt("Update_Time"));
    		while (rs.next()) {
    			Map<String,Object> rowData = new HashMap<String,Object>();
    			for (int i = 1; i <= columnCount; i++) {
    				rowData.put(md.getColumnName(i), rs.getObject(i));
    			}
    			list.add(rowData);
    		}
    		return list;
    	} 
    	catch (SQLException e) {
    		System.out.println("查询数据时出错！"+e.getMessage());
    	}
    	return null;
    }
    
    /**
	 * 实时状态
	 * @param name
	 * @return
	 */
    public ResultSet oneStatus(String name) {   //选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
        String sql="Select * from Monitoring_List where Machine_Name = '"+name+"'";//将从表中查询到的的所有信息存入sql
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();//得到Statement实例
            ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集
            // 当返回的结果集不为空时，并且还有记录时，循环输出记录
            //System.out.println(rs.getString("Machine_Name") + "\n" + rs.getString("Update_Date")+ "\n" +rs.getInt("Update_Time"));
            return rs;
        } 
        catch (SQLException e) {
            System.out.println("查询数据时出错！"+e.getMessage());
        }
		return null;
    }
    
    /**
     * 实时产量
     * @param name
     * @return
     */
    public List<Map<String, Object>> yield(String time , String name) {   //选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	String a = "";
    	if(name != null && !name.equals("")){
    		a = "where Machine_Name = '"+name+"' AND Record_Date = '"+ time +"'";
    	}
    	String sql="Select PartsCount_Result,Record_Time from Running_Log "+a+" ORDER BY Record_Time DESC limit 0,7";//将从表中查询到的的所有信息存入sql
    	try {
    		Connection conn = this.connect();
    		Statement stmt = conn.createStatement();//得到Statement实例
    		ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集
    		ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
    		int columnCount = md.getColumnCount();   //获得列数 
    		// 当返回的结果集不为空时，并且还有记录时，循环输出记录
    		//System.out.println(rs.getString("Machine_Name") + "\n" + rs.getString("Update_Date")+ "\n" +rs.getInt("Update_Time"));
    		while (rs.next()) {
    			Map<String,Object> rowData = new HashMap<String,Object>();
    			for (int i = 1; i <= columnCount; i++) {
    				rowData.put(md.getColumnName(i), rs.getObject(i));
    			}
    			list.add(rowData);
    		}
    		return list;
    	} 
    	catch (SQLException e) {
    		System.out.println("查询数据时出错！"+e.getMessage());
    	}
    	return null;
    }
    
    /**
     * 历史数据导出
     * @param name
     * @return
     */
    public List<Map<String, Object>> hisdataExport(String name,String time,String status) {   
    	//选择 文本区 中的所有文本。在 null 或空文档上不执行任何操作。
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	String a = "";
    	if(name != null && !name.equals("")){
    		a = a + "and a.Machine_Name = '" + name + "'";
    	}
    	if(time != null && !time.equals("")){
    		a = a + "and a.Record_Date = '" + time + "'";
    	}
    	if(status != null && !status.equals("")){
    		a = a + "and a.Machine_Signal = '" + status + "'";
    	}
    	
    	String sql="Select a.Machine_Name,"
    			+ "a.Machine_Status,"
    			+ "a.Alarm_No,"
    			+ "a.Alarm_Message,"
    			+ "a.Tool_No,"
    			+ "a.PartsCount_Result,"
    			+ "a.Override_Spindle, "
    			+ "a.Record_Date, "
    			+ "a.Record_Time "
    			+ "from Running_Log a "
    			+ "where 1 = 1 "
    			+ a
    			+ " ORDER BY a.Record_Date , a.Record_Time DESC";//将从表中查询到的的所有信息存入sql
    	System.out.println(sql);
    	try {
    		Connection conn = this.connect();
    		Statement stmt = conn.createStatement();//得到Statement实例
    		ResultSet rs = stmt.executeQuery(sql);//执行SQL语句返回结果集
    		ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
    		int columnCount = md.getColumnCount();   //获得列数 
    		// 当返回的结果集不为空时，并且还有记录时，循环输出记录
    		//System.out.println(rs.getString("Machine_Name") + "\n" + rs.getString("Update_Date")+ "\n" +rs.getInt("Update_Time"));
    		while (rs.next()) {
    			Map<String,Object> rowData = new HashMap<String,Object>();
    			for (int i = 1; i <= columnCount; i++) {
    				rowData.put(md.getColumnName(i), rs.getObject(i));
    			}
    			list.add(rowData);
    		}
    		return list;
    	} 
    	catch (SQLException e) {
    		System.out.println("查询数据时出错！"+e.getMessage());
    	}
    	return null;
    }
}
