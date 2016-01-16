package site.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import enity.Business;
import enity.Location;

public class PutIntoGrid {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		int []intervals = {3,5,7};
		jdbcDao jdbc=new jdbcDao();
		String industry[]={"������","������","������","���ͼҵ�ר��","������","���ðٻ���","������","�鱦��������","רҵ������"};
		String business_type=industry[5];
		int cluster=24;
		for(int i=0;i<intervals.length;i++)
		{
			List<Business> data=jdbc.readData(cluster,business_type);
			if(data.size()>0)
			{
				Collections.sort(data,new sortbylat());
				double firstlat=data.get(0).getLat();
				double latspan=(data.get(data.size()-1).getLat()-data.get(0).getLat())/intervals[i];

				Collections.sort(data,new sortbylng());
				double lngspan=(data.get(data.size()-1).getLng()-data.get(0).getLng())/intervals[i];
				double firstlng=data.get(0).getLng();

				HashMap<Location,List<Business>> grid=new HashMap<Location,List<Business>>();
				Iterator<Business> iter=data.iterator();
				while(iter.hasNext())
				{
					Business temp=iter.next();
					int x=(int)((temp.getLat()-firstlat)/latspan);
					if(x==intervals[i]){
						x-=1;
					}
					
					int y=(int)((temp.getLng()-firstlng)/lngspan);
					if(y==intervals[i]){
						y-=1;
					}
					
					Location loc=new Location(x,y,firstlat+x*latspan,firstlng+y*lngspan,latspan,lngspan);
					if(grid.containsKey(loc))
					{
						grid.get(loc).add(temp);
					}
					else
					{
						List<Business> firstbs=new ArrayList<Business>();
						firstbs.add(temp);
						grid.put(loc, firstbs);
					}
						
				}
				
				int sum=0;
				double [][]show_grid=new double[intervals[i]][intervals[i]];
				ArrayList<Location> loc=new ArrayList<Location>();
				for(int x=0;x<intervals[i];x++)
					for(int y=0;y<intervals[i];y++)
					{
						show_grid[x][y]=0.0;
					}
				Iterator<Entry<Location, List<Business>>> grid_iter = grid.entrySet().iterator();
				while(grid_iter.hasNext())
				{
					@SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry) grid_iter.next();
					Location tiny_grid=(Location)entry.getKey();
					ArrayList<Business> val = (ArrayList<Business>) entry.getValue();
					double total_amount=0.0;
					for(Business temp:val)
					{
						total_amount+=temp.getTotal_amount();
					}
					total_amount=total_amount/(val.size()+1);
					tiny_grid.setTotal_amount(total_amount);
					loc.add(tiny_grid);
					sum++;
					show_grid[tiny_grid.getX()][tiny_grid.getY()]=total_amount;
				}
				for(Location lo:loc)
				{
					System.out.println(cluster+","+business_type+","+intervals[i]*intervals[i]+","
							+lo.getX()+","+lo.getY()+","+lo.getLat()+","
							+latspan+","+lo.getLng()+","+lngspan+","+lo.getTotal_amount());
				}
				for(int x=0;x<intervals[i];x++)
					for(int y=0;y<intervals[i];y++)
					{
						if(show_grid[x][y]==0.0)
						{
							System.out.println(cluster+","+business_type+","+intervals[i]*intervals[i]
									+","+x+","+y+","+(firstlat+latspan*x)+","+latspan+","
									+(firstlng+lngspan*y)+","+lngspan+","+0.0);
						}
					}
				
			}
			
		}
				
	}
			
}




