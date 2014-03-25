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
		HashMap<Long, Double> hotBrands = brandStatistics.getActionFrequenceEveryMonthEveryPerson(Consts.ActionType.BUY, personNumber);
		Set<Long> set = hotBrands.keySet();
		Iterator<Long> it = set.iterator();
		while(it.hasNext()){
			Long s = (Long)it.next();			
			System.out.println(s + ": " + hotBrands.get(s) + " by " + personNumber.get(s) + " persons.");
	    }
	}

}
