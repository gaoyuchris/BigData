package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
private static int port = DefaultConstant.PORT;
	
	private static String host = DefaultConstant.HOST;
	private static String user = DefaultConstant.USER;
	private static String psw = DefaultConstant.PSW;
	private static String database = DefaultConstant.DATABASE;
	private static Connection connection;
	
	public static Connection getConnection(String host, int port, String user, String psw, String database){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String conUrl = "jdbc:mysql://" + host + ":"  + port + "/" + database + "?useUnicode=true&characterEncoding=utf8";
		try {
			connection = DriverManager.getConnection(conUrl, user, psw);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}
	
	public static Connection getConnection(String database){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String conUrl = "jdbc:mysql://" + host + ":"  + port + "/" + database + "?useUnicode=true&characterEncoding=utf8";
		try {
			connection = DriverManager.getConnection(conUrl, user, psw);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;

	}
	
	public static Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String conUrl = "jdbc:mysql://" + host + ":"  + port + "/" + database + "?useUnicode=true&characterEncoding=utf8";
		try {
			connection = DriverManager.getConnection(conUrl, user, psw);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}
}
