package site.selection;

import java.util.List;

import enity.Industry;

public class Cnt_Growth {
	
	public double growth(jdbcDao a,int cluster,String business_type)
	{
		List<Industry> data =a.readIndustry_Growth(cluster,business_type);
		double sum_weight=0;
		double numerator=0.0;
		if(data.size()>0)
		{
			for(Industry ind:data)
			{
				String temp=ind.getGrowth();
				double growth=0.0;
				if(!temp.equals(".")&&!temp.equals("NULL"))
				{
					growth=Double.parseDouble(ind.getGrowth());
				}
				if(growth>100|growth<-100)
				{
					growth=0.0;
				}
				double weight=ind.getTotal_amount()/14.0;
				numerator+=weight*growth;
				sum_weight+=weight;
			}
			return numerator/sum_weight;		
		}
		else
		{
			return -10.0;
		}
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
//				List<Industry> data =a.readIndustry_Growth(i,industry[j]);
//				double sum_weight=0;
//				double numerator=0.0;
//				if(data.size()>0)
//				{
//					for(Industry ind:data)
//					{
//						String temp=ind.getGrowth();
//						double growth=0.0;
//						if(!temp.equals(".")&&!temp.equals("NULL"))
//						{
//							growth=Double.parseDouble(ind.getGrowth());
//						}
//						double weight=ind.getTotal_amount()/14.0;
//						if(growth>100.0||growth<-100.0)
//						{
//							growth=0.0;
//						}
//						numerator+=weight*growth;
//						sum_weight+=weight;
//					}
////					System.out.println(numerator);
////					System.out.println(sum_weight);
//					System.out.print((numerator/sum_weight+1)+",");		
//				}
//				else
//				{
//					System.out.print("null,");
//				}
//				
//			}
//			System.out.println();
//
//		}
//	}	
}
