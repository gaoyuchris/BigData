package cluster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.SqlConnection;
import enity.Business;

public class TableQuery {
	private static Connection connection;
	
	private static final double LatShift = 0.00785;
	private static final double LngShift = 0.01408;
	/**
	 * 
	 * @param tableName
	 * @param frequency0    查询频率大于frequency0
	 * @param total_amount0 查询总额大于total_amount0
	 * @return
	 */
	public static List<Business> readData(String tableName, int frequency0, float total_amount0,
			double lat0, double lat1, double lng0, double lng1){
		
		List<Business> data = new ArrayList<Business>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = SqlConnection.getConnection();
			String sql = "select * from business where frequency >= " + frequency0 +
					" and total_amount >= " + total_amount0 + " and lat >= " + lat0 +
					" and lat <= " + lat1 + " and lng >= " + lng0 + " and lng <= " + lng1 + 
					 " and lat != 39.92998578";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				int business_id = rs.getInt("business_id");
				String original_business_id = rs.getString("original_business_id");
				String business_name = rs.getString("business_name");
				String business_type = rs.getString("business_type");
				int frequency = rs.getInt("frequency");
				float total_amount = rs.getFloat("total_amount");
				double lat = rs.getDouble("lat") - LatShift;
				double lng = rs.getDouble("lng") - LngShift;
				String district = rs.getString("district");
				
				data.add(new Business(business_id, original_business_id, business_name, business_type, 
						frequency, total_amount, lat, lng, district));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(connection != null) connection.close();
			} catch (SQLException e) {
				// TODO: handle exception.
				e.printStackTrace();
			}
			
		}
		
		return data;
	}
	
	public static void saveData(String tableName, List<Business> data){
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = SqlConnection.getConnection();
			String sql = "DROP TABLE IF EXISTS " + tableName + " ;" ;
			String sql2 = 
					"CREATE TABLE " + tableName + " " +
	                "(business_id INTEGER not NULL, " +
	                " original_business_id VARCHAR(32), " + 
	                " business_name VARCHAR(128), " + 
	                "business_type VARCHAR(64), " +
	                " frequency INTEGER, " + 
	                "total_amount FLOAT, " +
	                "lat DOUBLE, " + 
	                "lng DOUBLE, " +
	                "district VARCHAR(32), "+ 
	                "cluster INTEGER, " +
	                "PRIMARY KEY ( business_id ));"; 
			
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();
			ps = connection.prepareStatement(sql2);
			ps.executeUpdate();
			sql = "insert into " + tableName + " (business_id, original_business_id, "
					+ "business_name, business_type, frequency, total_amount, lat, "
					+ "lng, district, cluster) " + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql);
			for (Business business : data) {
				ps.setInt(1, business.getBusiness_id());
				ps.setString(2, business.getOriginal_business_id());
				ps.setString(3, business.getBusiness_name());
				ps.setString(4, business.getBusiness_type());
				ps.setInt(5, business.getFrequency());
				ps.setFloat(6, business.getTotal_amount());
				ps.setDouble(7, business.getLat());
				ps.setDouble(8, business.getLng());
				ps.setString(9, business.getDistrict());
				ps.setInt(10, business.getLabel());
				
				ps.addBatch();
			}
			
			ps.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			
		}
	}
}
