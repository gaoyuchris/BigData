package cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enity.Business;



/*
 * DBSCAN(D, eps, MinPts) {
   C = 0
   for each point P in dataset D {
      if P is visited
         continue next point
      mark P as visited
      NeighborPts = regionQuery(P, eps)
      if sizeof(NeighborPts) < MinPts
         mark P as NOISE
      else {
         C = next cluster
         expandCluster(P, NeighborPts, C, eps, MinPts)
      }
   }
}

expandCluster(P, NeighborPts, C, eps, MinPts) {
   add P to cluster C
   for each point P' in NeighborPts { 
      if P' is not visited {
         mark P' as visited
         NeighborPts' = regionQuery(P', eps)
         if sizeof(NeighborPts') >= MinPts
            NeighborPts = NeighborPts joined with NeighborPts'
      }
      if P' is not yet member of any cluster
         add P' to cluster C
   }
}

regionQuery(P, eps)
   return all points within P's eps-neighborhood (including P)
 */
public class DBSCANCluster {
	private int minpts;
	private double eps;
	private List<Business> data;
	private int labelnum;
	
	private int clusterID;
	private Map<Integer, List<Business>> clusters;
	private int[][] table;
	private int leftNum;
	
	private Map<Integer, Integer> trueLabelCount;
	public DBSCANCluster(int minpts, double eps, List<Business> data, int labelnum){
		this.minpts = minpts;
		this.eps = eps;
		this.data = data;
		this.labelnum = labelnum;
	}
	public DBSCANCluster(int minpts, double eps, List<Business> data){
		this.minpts = minpts;
		this.eps = eps;
		this.data = data;
	}
	
	
	public Map<Integer, List<Business>> computeDBSCAN(){
		
		init();
		for(Business p : data){
			
			if(p.getVisted() == Constant.VISITED) continue;
			
			p.setVisted(Constant.VISITED);
			List<Business> region = queryRegion(p);
			
			if(region.size() >= minpts){
				clusterID ++;
				p.setType(Constant.CORE);
				
				expandCluster(p, region);
			}else{
				p.setType(Constant.NOISE);  //暂时都认为是噪音点，边界点会在随后处理中体现
			}
		}
//		calculateTable();
//		calculateLeftNum();
		return clusters;
		
	}
	public double fscore(){
		double sum = 0;
		for(int i = 1; i <=labelnum; i++){
			double max = 0;
			for(int j = 1; j <= clusterID; j++){
				if(fscore(j, i) > max){
					max = fscore(j, i);
				}
			}
			sum = sum +  trueLabelCount.get(i) * max;
		}
		return sum/data.size();
	}
	
	public double fscore(int clusteri, int classifyj){
		return (2 * precision(clusteri, classifyj) * recall(clusteri, classifyj))/
				(precision(clusteri, classifyj) + recall(clusteri, classifyj));
	}
	
	private double precision(int clusteri, int classifyj){
		List<Business> cluster = clusters.get(clusteri);
		int clusteriNum = cluster.size();
		int clusterijNum = table[clusteri][classifyj];
		if (clusteriNum == 0){
			return 0.0;
		}
		return clusterijNum*1.0/clusteriNum;
	}
	
	private double recall(int clusteri, int classifyj){
		
		int clusterijNum = table[clusteri][classifyj];
		int classifyjNum = trueLabelCount.get(classifyj);
		
		return clusterijNum*1.0/classifyjNum;
	}
	public double  purity(){
		int sum = 0;
		for(int i = 1; i <= clusterID; i++){
			int label = 1;
			for(int j = 1; j < table[i].length; j++){
				if(table[i][j] > table[i][label]){
					label = j;
				}
			}
			
			sum = sum + table[i][label];
		}
		if(leftNum == 0) return 0.0;
		return sum*1.0/leftNum;
	}
	
	private void expandCluster(Business p, List<Business> region){
		if(clusters.containsKey(clusterID) == false){
			clusters.put(clusterID, new ArrayList<Business>());
			p.setLabel(clusterID);
		}
		List<Business> cluster = clusters.get(clusterID);
		cluster.add(p);
		
		for (int i = 0; i < region.size(); i++) {
			Business neighbor = region.get(i);
			if(neighbor.getVisted() == Constant.UNVISITED){
				neighbor.setVisted(Constant.VISITED);
				List<Business> neignborRegion = queryRegion(neighbor);
				if(neignborRegion.size() >= minpts){
					
					neighbor.setType(Constant.CORE);
					for (Business point : neignborRegion) {
						if(region.contains(point) == false){
							region.add(point);
						}
					}
				}else{
					neighbor.setType(Constant.BORDER);
				}
			}else{
				neighbor.setType(Constant.BORDER);
			}
			
			if(neighbor.getLabel() == 0){
				clusters.get(clusterID).add(neighbor);
				neighbor.setLabel(clusterID);
			}
			
		}
	}
	
	private List<Business> queryRegion(Business p){
		List<Business> region = new ArrayList<Business>();
		for (Business point : data) {
			if(Business.getDistance(p, point) <= eps){
				region.add(point);
			}
		}
		
		return region;
	}
	private void calculateTable(){
		table  = new int[clusterID+1][labelnum + 1];
		for(int i = 0; i < table.length; i++){
			for(int j = 0; j < table[i].length; j++)
				table[i][j] = 0;
		}
		for(int i = 1; i <= clusterID; i++){
			List<Business> cluster = clusters.get(i);
			for (Business point : cluster) {
				int truelabel = point.getTruelabel();
			
				table[i][truelabel] ++;
			}
		}
		
	}
	public int  calculateLeftNum(){
		for(int i = 1; i <= clusterID; i++){
			leftNum += clusters.get(i).size();
		}
		return leftNum;
	}
	private void init(){
		clusterID = 0;
		leftNum = 0;
		clusters = new HashMap<Integer, List<Business>>();
		
	}
	
	
	
}
