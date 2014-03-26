import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import me.app.base.Consts;
import me.app.opr.BrandStatistics;


public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BrandStatistics brandStatistics = new BrandStatistics();
		HashMap<Long, Integer> personNumber = new HashMap<Long, Integer>();
		HashMap<Long, Double> hotBrands = brandStatistics.getActionTimeSpan(Consts.ActionType.BUY);
		Set<Long> set = hotBrands.keySet();
		Iterator<Long> it = set.iterator();
		int size = 0;
		while(it.hasNext()){
			size++;
			Long s = (Long)it.next();			
			System.out.println(s + ": " + hotBrands.get(s));
	    }
		System.out.println("size: " + size);
	}

}
