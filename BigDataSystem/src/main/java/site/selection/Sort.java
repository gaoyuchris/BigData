package site.selection;

import java.util.Comparator;
import enity.Business;


class sortbylat implements Comparator{
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		Business p1=(Business)o1;
		Business p2=(Business)o2;
		if (p1.getLat() > p2.getLat()) 
			return 1;
		else
			return -1;
	}
}

class sortbylng implements Comparator{
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		Business p1=(Business)o1;
		Business p2=(Business)o2;
		if (p1.getLng() > p2.getLng()) 
			return 1;
		else
			return -1;
	}
}