package site.selection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;

import database.SqlConnection;
import enity.Business;
import enity.Industry;;

public class jdbcDao {
	private static Connection connection;
	
	private static final double LatShift = 0.0;
	private static final double LngShift = 0.0;
	
	public List<Business> readData(int cluster, String b_type)
	{	
		List<Business> data = new ArrayList<Business>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = SqlConnection.getConnection();
			String sql = "select * from business_cluster where business_type = '" + b_type + "'"
			+"and cluster ="+cluster;
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
	
	public List<Industry> readIndustry_Growth(int cluster, String Industry)
	{	
		List<Industry> data = new ArrayList<Industry>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = SqlConnection.getConnection();
			String sql = "select * from industry_growth1 where business_type = '" + Industry + "'"
			+"and cluster ="+cluster;
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				float total_amount = rs.getFloat("total_amount");
				String growth = rs.getString("incre14");
				
				data.add(new Industry(total_amount, growth));
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
	
	public List<Industry> add_year(int cluster,int year, String Industry)
	{	
		List<Industry> data = new ArrayList<Industry>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = SqlConnection.getConnection();
			String sql = "select * from newopen where original_business_id in"+ 
			"(SELECT original_business_id from business_cluster where cluster="+cluster+" and business_type='"+Industry+"')"
			+"and add_year="+year;
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				float limit = rs.getFloat("limit");
				float daily_free = rs.getFloat("daily_fre");
				float total_amount=limit*daily_free;
				data.add(new Industry(total_amount));
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
	
	public List<Business> get_cluster()
	{	
		List<Business> data = new ArrayList<Business>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = SqlConnection.getConnection();
			String sql = "select * from business_cluster where cluster!=0";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				double lat = rs.getDouble("lat");
				double lng = rs.getDouble("lng");
				int clu=rs.getInt("cluster");
				data.add(new Business(lat,lng,clu));
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
	
	public List<Business> get_closed()
	{	
		List<Business> data = new ArrayList<Business>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = SqlConnection.getConnection();
			String sql = "select * from closed";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				double lat = rs.getDouble("lat")-LatShift;
				double lng = rs.getDouble("lng")-LngShift;
				double total_amount=rs.getDouble("limit")*rs.getDouble("daily_free");
				String businees_type=rs.getString("business_type");
				int year=rs.getInt("dis_year");
				System.out.println(rs.getFloat("limit")+" "+rs.getFloat("daily_free"));
				System.out.println(rs.getFloat("limit")*rs.getFloat("daily_free"));
				data.add(new Business(lat,lng,total_amount,year,businees_type));
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
	
	public void saveFinalClosed(String tableName, List<Business> data){
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = SqlConnection.getConnection();
			String sql = "DROP TABLE IF EXISTS " + tableName + " ;" ;
			String sql2 = 
					"CREATE TABLE " + tableName + " " +
	                "(business_id INTEGER not NULL, " +
	                "total_amount DOUBLE, " +
	                "lat DOUBLE, " + 
	                "lng DOUBLE, " +
	                "cluster INTEGER, " +
	                "dis_year INTEGER, "+
	                "business_type VARCHAR(64),"+
	                "PRIMARY KEY ( business_id ));"; 
			
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();
			ps = connection.prepareStatement(sql2);
			ps.executeUpdate();
			sql = "insert into " + tableName + " (business_id,"
					+ "total_amount, lat, "
					+ "lng, cluster,dis_year,business_type) " + "values (?, ?, ?, ?, ?, ?,?)";
			connection.setAutoCommit(false);
			int i=1;
			ps = connection.prepareStatement(sql);
			for (Business business : data) {
				ps.setInt(1, i++);
				ps.setDouble(2, business.getWeight());
				ps.setDouble(3, business.getLat());
				ps.setDouble(4, business.getLng());
				ps.setInt(5, business.getCluster());
				ps.setInt(6, business.getYear());
				ps.setString(7, business.getBusiness_type());
				
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
	
	public List<Industry> dis_year(int cluster,int year, String Industry)
	{	
		List<Industry> data = new ArrayList<Industry>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = SqlConnection.getConnection();
			String sql = "select * from with_cluster_close where cluster="+cluster
			+" and dis_year="+year +" and business_type='" + Industry + "'";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				double total_amount=rs.getDouble("total_amount");
				data.add(new Industry(total_amount));
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
	


}
