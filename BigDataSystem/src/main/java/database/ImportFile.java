package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ImportFile {
	
	public static Connection connection;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String path = "business.csv";
		String tableName = "business";
		
		importcsv(path, tableName);
	}
	
	public static void importcsv(String path, String tableName){
		connection = SqlConnection.getConnection();
		String sql = "CREATE TABLE business " +
                "(business_id INTEGER not NULL, " +
                " original_business_id VARCHAR(32), " + 
                " business_name VARCHAR(128), " + 
                "business_type VARCHAR(64), " +
                " frequency INTEGER, " + 
                "total_amount FLOAT, " +
                "lat DOUBLE, " + 
                "lng DOUBLE, " +
                "district VARCHAR(32), "+
                " PRIMARY KEY ( business_id ))"; 
		
		String sql1 = "LOAD DATA INFILE \'" + path + "\'" +
		" INTO TABLE " + tableName +" FIELDS TERMINATED BY \',\'" + 
				" OPTIONALLY ENCLOSED BY \'\"\'" +
		" lines terminated by \'\r\n\'" +
				" ignore 1 lines" + 
		" (business_id, original_business_id, business_name, business_type, frequency, total_amount, "
		+ "lat, lng, district)";
		PreparedStatement pstt  = null;
		try {
			String[] types = new String[1];
			types[0] = "TABLE";
			ResultSet result = connection.getMetaData().getTables(null, null, tableName, types);
			if(result.next() == false){
				System.out.println("table not exist, create it");
				pstt = connection.prepareStatement(sql);
				pstt.executeUpdate();
			}else{
				System.out.println("table exist, clear table");
				String sqldel = "delete from " + tableName;
				pstt = connection.prepareStatement(sqldel);
				pstt.executeUpdate();
			}
			
			pstt = connection.prepareStatement(sql1);
			pstt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(pstt != null)
				try {
					pstt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	

}
