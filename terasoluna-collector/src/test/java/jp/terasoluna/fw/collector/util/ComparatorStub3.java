package jp.terasoluna.fw.collector.util;

import java.util.Comparator;

import org.springframework.util.Assert;

public class ComparatorStub3 implements Comparator<String> {

	public int compare(String o1, String o2) {
		
        Assert.isTrue(o1 instanceof Comparable,
                "The first object provided is not Comparable");
        Assert.isTrue(o2 instanceof Comparable,
                "The second object provided is not Comparable");
		
		if(o1 != null && o2 != null){
			// •¶Žš—ñ‚ÌŒã‚ë2•¶Žš‚ð”äŠr‚·‚é
			String substr1 = o1.substring(o1.length()-2);
			String substr2 = o2.substring(o2.length()-2);
			
			return substr1.compareTo(substr2);
			
		}
		
		return 0;
	}

}
