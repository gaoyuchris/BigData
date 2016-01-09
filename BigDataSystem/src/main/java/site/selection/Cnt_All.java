package site.selection;

public class Cnt_All {
	public static final String industry[]={"宾馆类","餐饮类","超市类","大型家电专卖","批发类","日用百货类","娱乐类","珠宝、工艺类","专业服务类"};
	
	public static void main(String[] args) {
		Cnt_Growth cg=new Cnt_Growth();
		Cnt_Scale cs=new Cnt_Scale();
		jdbcDao a=new jdbcDao();
		for(int i=1;i<28;i++)
		{
			for(int j=0;j<industry.length;j++)
			{
				double gro=cg.growth(a, i, industry[j]);
				double sc=cs.scale(a, i, industry[j], 2014);
				if(gro==-10.0||sc==0.0)
				{
					System.out.print("null,");
					continue;
				}
				else if(sc==-10.0)
				{
					System.out.print("14null,");
					continue;
				}
				else
				{
					System.out.print(gro+1/sc+",");
				}		
			}
			System.out.println();
		}
		
		
	}

}
