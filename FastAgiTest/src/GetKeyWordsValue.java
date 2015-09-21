import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.Statement;

//根据关键字从数据库中检索相应字段value的类
public class GetKeyWordsValue {

	private static String dbid = "jdbc:mysql://localhost:3306/bysj";
	private static String drivername = "com.mysql.jdbc.Driver";
	private static String username = "root";
	private static String password = "123456";
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static String getValue(String stringneedTOdeal) throws SQLException{
		// TODO Auto-generated method stub
		String result = "";
		System.out.println("GetKeyWordsValue在数据库中操作开始。。。");
		try {
			Class.forName(drivername);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "数据库加载失败";
			return result;
		}
		System.out.println("加载驱动成功！");
		
		conn = DriverManager.getConnection(dbid, username, password);
		System.out.println("GetKeyWordsValue连接数据库成功");
		

		KeyWordTools kw = new KeyWordTools(stringneedTOdeal);
		System.out.println("GetKeyWordsValue提取关键字成功");
		ArrayList<String> al = kw.getKeyWords(); // 获取关键字
		Iterator<String> it = al.iterator();
		while (it.hasNext()) {// 遍历关键字
			Boolean isFound = false;
			String key = it.next();
			System.out.println(".............." + key);
			//  where tkey like '%"+key+"%'
			// key="中国";
			String sqlString = "select * from keyword where tkey='中国'";// 查找是否有和关键字匹配的数据
			System.out.println(sqlString);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlString);
			while (rs.next()) {
				String tkey = rs.getString("tkey");
				String value = rs.getString("tvalue");
				isFound = true;
				result = value;
				System.out.print("....value....." + value + "...key..." + tkey);
				break; // 因为数组是按照两个汉字开始的，再按照一个汉字，所以当开始匹配成功的时候应该是最好的情况
			}

			if (isFound) {
				break;
			}

		}
		if ("".equals(result) || result.isEmpty()) { // 未查询到相关内容
			result = "未查询到相关内容";
		}

		conn.close();
		return result;
	}

}
