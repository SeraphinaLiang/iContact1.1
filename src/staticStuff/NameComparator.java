package staticStuff;

import java.util.Comparator;
public class NameComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		String s1=(String)o1;
		String s2=(String)o2;
		
		String name1=utility.getPingYin(s1);
		String name2=utility.getPingYin(s2);
		
		return name1.compareToIgnoreCase(name2);
	}

	
}
	
