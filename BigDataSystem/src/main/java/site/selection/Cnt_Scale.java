package site.selection;

import java.util.List;

import enity.Industry;

public class Cnt_Scale {
	
	public double scale2015(List<Industry> data)
	{
		
		double sum_weight=0;
		if(data.size()>0)
		{
			for(Industry ind:data)
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
	
	public  double scale2014_new(List<Industry> data)
	{
		
		double sum_weight=0;
		if(data.size()>0)
		{
			for(Industry ind:data)
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
		List<Industry> data2015 =a.readIndustry_Growth(cluster,business_type);
		double scale2015_now=0.0;
		if(data2015.size()>0)
		{
			scale2015_now=scale2015(data2015);
		}	
		
		List<Industry> data2014_new =a.add_year(cluster, sec_year, business_type);
		double scale2014_new=0.0;
		if(data2014_new.size()>0)
		{
			scale2014_new=scale2014_new(data2014_new);
		}
		
		List<Industry> data2014_dis =a.dis_year(cluster, sec_year, business_type);
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
	
//	public static void main(String[] args) {
//		int cluster=19;
//		String industry[]={"宾馆类","餐饮类","超市类","大型家电专卖","批发类","日用百货类","娱乐类","珠宝、工艺类","专业服务类"};
//
//		jdbcDao a=new jdbcDao();
//		for(int i=1;i<28;i++)
//		{
//			for(int j=0;j<9;j++)
//			{
//				List<Industry> data2015 =a.readIndustry_Growth(1,industry[3]);
//				double scale2015_now=0.0;
////				System.out.println(data2015.size());
//				if(data2015.size()>0)
//				{
//					scale2015_now=scale2015(data2015);
//				}	
//				
//				List<Industry> data2014_new =a.add_year(1, 2014, industry[3]);
//				double scale2014_new=0.0;
////				System.out.println(data2014_new.size());
//				if(data2014_new.size()>0)
//				{
//					scale2014_new=scale2014_new(data2014_new);
//				}
//				
//				List<Industry> data2014_dis =a.dis_year(1, 2014, industry[3]);
//				double scale2014_dis=0.0;
////				System.out.println(data2014_dis.size());
//				if(data2014_dis.size()>0)
//				{
//					scale2014_dis=scale2014_new(data2014_dis);
//				}
//				System.out.println(scale2015_now);
//				double scale2014_now=scale2015_now-scale2014_new+scale2014_dis;
//				System.out.println(scale2014_now);
//				System.out.print(scale2015_now/scale2014_now+",");
//				
//			}
//			System.out.println();
//		}
//	}

}
