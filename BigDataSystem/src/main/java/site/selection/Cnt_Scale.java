package site.selection;

import java.util.List;

import enity.Business;

public class Cnt_Scale {
	
	public double scale2015(List<Business> data)
	{
		
		double sum_weight=0;
		if(data.size()>0)
		{
			for(Business ind:data)
			{
				double weight=ind.getTotal_amount()/14.0;
				sum_weight+=weight;
			}
			return sum_weight;	
		}
		else
		{
			return 0.0;
		}
		
	}
	
	public  double scale2014_new(List<Business> data)
	{
		
		double sum_weight=0;
		if(data.size()>0)
		{
			for(Business ind:data)
			{
				double weight=ind.getTotal_amount();
				sum_weight+=weight;
			}
			return sum_weight;	
		}
		else
		{
			return 0.0;
		}
		
	}
	
	public double scale(jdbcDao a,int cluster,String business_type,int sec_year)
	{
		List<Business> data2015 =a.readIndustry_Growth(cluster,business_type);
		double scale2015_now=0.0;
		if(data2015.size()>0)
		{
			scale2015_now=scale2015(data2015);
		}	
		
		List<Business> data2014_new =a.add_year(cluster, sec_year, business_type);
		double scale2014_new=0.0;
		if(data2014_new.size()>0)
		{
			scale2014_new=scale2014_new(data2014_new);
		}
		
		List<Business> data2014_dis =a.dis_year(cluster, sec_year, business_type);
		double scale2014_dis=0.0;
		if(data2014_dis.size()>0)
		{
			scale2014_dis=scale2014_new(data2014_dis);
		}
		double scale2014_now=scale2015_now-scale2014_new+scale2014_dis;
		if(scale2014_now==0)
			return -10.0;
		else
			return scale2015_now/scale2014_now;
		
	}
	
}
