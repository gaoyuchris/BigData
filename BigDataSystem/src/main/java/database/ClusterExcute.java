package database;

import java.util.List;
import java.util.Map;

import cluster.DBSCANCluster;
import cluster.TableQuery;
import enity.Business;

public class ClusterExcute {
	
	public static double EPS = 500;
	public static int MinPts = 90;
	public static int frequency0 = 0;
	public static float total_amount0 = 0;
	public static double lat0 = 39.2;
	public static double lat1 = 41.6;
	public static double lng0 = 115.25;
	public static double lng1 = 117.4;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		excuteCluster(EPS, MinPts, frequency0, total_amount0, lat0, lat1, lng0, lng1);
	}
	
	public static void excuteCluster(double eps, int minpts, int frequency0, 
			float total_amount0, double lat0, double lat1, double lng0, double lng1){
		
		
		List<Business> data;
		data = TableQuery.readData("business", frequency0, total_amount0, lat0, lat1, lng0, lng1);
		System.out.println("data has beed read, data.size: "  + data.size());
		
		System.out.println("eps: " + EPS + ", minpts: " + MinPts);
		DBSCANCluster dbscan = new DBSCANCluster(minpts, eps, data);
	
		
		Map<Integer, List<Business>> result = dbscan.computeDBSCAN();
		
		int leftNum = dbscan.calculateLeftNum();
		System.out.println("clusters.size: " + result.size());
		System.out.println(leftNum + " of " + data.size() +" businesses left, ratio: " +  (leftNum*1.0/data.size()));
		TableQuery.saveData("business_cluster", data);
		
	}

}
