package site.selection;

import java.util.List;

import enity.Business;

public class Cnt_Growth {
	
	public double growth(jdbcDao a,int cluster,String business_type)
	{
		List<Business> data =a.readIndustry_Growth(cluster,business_type);
		double sum_weight=0;
		double numerator=0.0;
		if(data.size()>0)
		{
			for(Business ind:data)
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
}
