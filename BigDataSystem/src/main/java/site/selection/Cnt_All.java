package site.selection;

public class Cnt_All {
	public static final String industry[]={"������","������","������","���ͼҵ�ר��","������","���ðٻ���","������","�鱦��������","רҵ������"};
	
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
