package site.selection;

import java.util.List;

import enity.Business;

public class PutIntoCluster {
	public static void main(String[] args) {
//		Business distance=new Business();
		jdbcDao a=new jdbcDao();
		List<Business> cluster=a.get_cluster();
		List<Business> closed=a.get_closed();
		System.out.println(closed.size());
		for(Business clo:closed)
		{
			int label=0;
			for(Business open:cluster)
			{
				double distance=Business.getDistance(clo, open);
				if(distance<800.0)
				{
					label=open.getCluster();
					break;
				}
			}
			clo.setCluster(label);
		}
		System.out.println(closed.size());
		a.saveFinalClosed("with_cluster_close", closed);
		
	}

}
