import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Test {

	static int lport = 4321;//本地端口
	static String rhost = "localhost";//远程MySQL服务器
	static int rport = 3306;//远程MySQL服务端口
	static JSch jsch;
	static Session session;
	public static void go() {
		String user = "ubuntu-user";//SSH连接用户名
		String password = "ubuntu";//SSH连接密码
		String host = "58.205.208.71";//SSH服务器
		
		int port = 6006;//SSH访问端口
		try {
			jsch = new JSch();
		    session = jsch.getSession(user, host, port);
		 	session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println(session.getServerVersion());//这里打印SSH服务器版本信息
			int assinged_port = session.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void close(){
		session.disconnect();
	}
	public static void sql() {
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://58.205.208.71:6000/mysql", "root", "123456");
			st = conn.createStatement();
			String sql = "show tables;";
			rs = st.executeQuery(sql);
			while (rs.next())
				System.out.println(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		//go();
		sql();
		//close();
	}
}
	