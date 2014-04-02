import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import me.app.base.Consts;
import me.app.opr.BrandStatistics;
import me.app.opr.Forecast;
import me.app.opr.UserStatistics;


public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		Forecast forecast = new Forecast();
		forecast.curForecast();
	}
	
	public static void brandStatic(String path) {
		BrandStatistics brandStatistics = new BrandStatistics();
		HashMap<Long, Double> hotBrandsByScore = null;
		
		HashMap<Long, Integer> personNumber = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequence = null;
		HashMap<Long, Double> timeSpan = null;
		
		HashMap<Long, Integer> personNumberOfClick = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequenceOfClick = null;
		HashMap<Long, Double> timeSpanOfClick = null;
		
		HashMap<Long, Integer> personNumberOfFavourite = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequenceOfFavourite = null;
		HashMap<Long, Double> timeSpanOfFavourite = null;
		
		HashMap<Long, Integer> personNumberOfAdd2cart = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequenceOfAdd2cart = null;
		HashMap<Long, Double> timeSpanOfAdd2cart = null;
		
		hotBrandsByScore = brandStatistics.getHot(0);
		frequence = brandStatistics.getActionFrequenceEveryMonthEveryPerson(Consts.ActionType.BUY, personNumber);
		timeSpan = brandStatistics.getActionTimeSpan(Consts.ActionType.BUY);
		
		frequenceOfClick = brandStatistics.getActionFrequenceEveryMonthEveryPerson(Consts.ActionType.CLICK, personNumberOfClick);
		timeSpanOfClick = brandStatistics.getActionTimeSpan(Consts.ActionType.CLICK);
		
		frequenceOfFavourite = brandStatistics.getActionFrequenceEveryMonthEveryPerson(Consts.ActionType.FAVOURITE, personNumberOfFavourite);
		timeSpanOfFavourite = brandStatistics.getActionTimeSpan(Consts.ActionType.FAVOURITE);
		
		frequenceOfAdd2cart = brandStatistics.getActionFrequenceEveryMonthEveryPerson(Consts.ActionType.ADD2CART, personNumberOfAdd2cart);
		timeSpanOfAdd2cart = brandStatistics.getActionTimeSpan(Consts.ActionType.ADD2CART);
		
		Set<Long> set = hotBrandsByScore.keySet();
		Iterator<Long> iterator = set.iterator();
		FileWriter fw = null;

		try {
			fw = new FileWriter(path);
			fw.write("id" + "," + "score" + "," +
					"fqn/buy" + "," + "span/buy" + "," + "num/buy" + "," +
					"fqn/click" + "," + "span/click" + "," + "num/click" + "," + 
					"fqn/favourite" + "," + "span/favourite" + "," + "num/favourite" + "," + 
					"fqn/add2cart" + "," + "span/add2cart" + "," + "num/add2cart" + "," + 
					"clickTimes" + "," + "buyTimes" + "," + "favouriteTimes" + "," + "add2cartTimes" +
					"\n");
			Long id;
			double score;
			double fqn, span, num;
			double fqnOfClick, spanOfClick, numOfClick;
			double fqnOfFavourite, spanOfFavourite, numOfFavourite;
			double fqnOfAdd2cart, spanOfAdd2cart, numOfAdd2cart;
			int clickTimes, buyTimes, add2cartTimes, favouriteTimes;
			
			while (iterator.hasNext()) {
				id = iterator.next();
				if (hotBrandsByScore.containsKey(id)) {
					score = hotBrandsByScore.get(id);
				} else {
					score = 0;
				}	
				
				if (frequence.containsKey(id)) {
					fqn = frequence.get(id);
				} else {
					fqn = 0;
				}				
				if (timeSpan.containsKey(id)) {
					span = timeSpan.get(id);
				} else {
					span = 0;
				}				
				if (personNumber.containsKey(id)) {
					num = personNumber.get(id);
				} else {
					num = 0;
				}
				
				if (frequenceOfClick.containsKey(id)) {
					fqnOfClick = frequenceOfClick.get(id);
				} else {
					fqnOfClick = 0;
				}				
				if (timeSpanOfClick.containsKey(id)) {
					spanOfClick = timeSpanOfClick.get(id);
				} else {
					spanOfClick = 0;
				}				
				if (personNumberOfClick.containsKey(id)) {
					numOfClick = personNumberOfClick.get(id);
				} else {
					numOfClick = 0;
				}
				
				if (frequenceOfFavourite.containsKey(id)) {
					fqnOfFavourite = frequenceOfFavourite.get(id);
				} else {
					fqnOfFavourite = 0;
				}				
				if (timeSpanOfFavourite.containsKey(id)) {
					spanOfFavourite = timeSpanOfFavourite.get(id);
				} else {
					spanOfFavourite = 0;
				}				
				if (personNumberOfFavourite.containsKey(id)) {
					numOfFavourite = personNumberOfFavourite.get(id);
				} else {
					numOfFavourite = 0;
				}
				
				if (frequenceOfAdd2cart.containsKey(id)) {
					fqnOfAdd2cart = frequenceOfAdd2cart.get(id);
				} else {
					fqnOfAdd2cart = 0;
				}				
				if (timeSpanOfAdd2cart.containsKey(id)) {
					spanOfAdd2cart = timeSpanOfAdd2cart.get(id);
				} else {
					spanOfAdd2cart = 0;
				}				
				if (personNumberOfAdd2cart.containsKey(id)) {
					numOfAdd2cart = personNumberOfAdd2cart.get(id);
				} else {
					numOfAdd2cart = 0;
				}
				
				clickTimes = brandStatistics.getActionTimes(brandStatistics.getBrand(id), Consts.ActionType.CLICK);
				buyTimes = brandStatistics.getActionTimes(brandStatistics.getBrand(id), Consts.ActionType.BUY);
				favouriteTimes = brandStatistics.getActionTimes(brandStatistics.getBrand(id), Consts.ActionType.FAVOURITE);
				add2cartTimes = brandStatistics.getActionTimes(brandStatistics.getBrand(id), Consts.ActionType.ADD2CART);
				
				fw.write(id + "," + score + "," +
						fqn + "," + span + "," + num + "," +
						fqnOfClick + "," + spanOfClick + "," + numOfClick + "," + 
						fqnOfFavourite + "," + spanOfFavourite + "," + numOfFavourite + "," + 
						fqnOfAdd2cart + "," + spanOfAdd2cart + "," + numOfAdd2cart + "," + 
						clickTimes + "," + buyTimes + "," + favouriteTimes + "," + add2cartTimes +
						"\n");
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void userStatic(String path) {
		UserStatistics userStatistics = new UserStatistics();
		HashMap<Long, Double> hotBrandsByScore = null;
		
		HashMap<Long, Integer> personNumber = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequence = null;
		HashMap<Long, Double> timeSpan = null;
		
		HashMap<Long, Integer> personNumberOfClick = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequenceOfClick = null;
		HashMap<Long, Double> timeSpanOfClick = null;
		
		HashMap<Long, Integer> personNumberOfFavourite = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequenceOfFavourite = null;
		HashMap<Long, Double> timeSpanOfFavourite = null;
		
		HashMap<Long, Integer> personNumberOfAdd2cart = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequenceOfAdd2cart = null;
		HashMap<Long, Double> timeSpanOfAdd2cart = null;
		
		hotBrandsByScore = userStatistics.getHot(0);
		frequence = userStatistics.getActionFrequenceEveryMonthEveryBrand(Consts.ActionType.BUY, personNumber);
		timeSpan = userStatistics.getActionTimeSpan(Consts.ActionType.BUY);
		
		frequenceOfClick = userStatistics.getActionFrequenceEveryMonthEveryBrand(Consts.ActionType.CLICK, personNumberOfClick);
		timeSpanOfClick = userStatistics.getActionTimeSpan(Consts.ActionType.CLICK);
		
		frequenceOfFavourite = userStatistics.getActionFrequenceEveryMonthEveryBrand(Consts.ActionType.FAVOURITE, personNumberOfFavourite);
		timeSpanOfFavourite = userStatistics.getActionTimeSpan(Consts.ActionType.FAVOURITE);
		
		frequenceOfAdd2cart = userStatistics.getActionFrequenceEveryMonthEveryBrand(Consts.ActionType.ADD2CART, personNumberOfAdd2cart);
		timeSpanOfAdd2cart = userStatistics.getActionTimeSpan(Consts.ActionType.ADD2CART);
		
		Set<Long> set = hotBrandsByScore.keySet();
		Iterator<Long> iterator = set.iterator();
		FileWriter fw = null;

		try {
			fw = new FileWriter(path);
			fw.write("id" + "," + "score" + "," +
					// 				brand statistics 				|| user statistics
					// frequence: 	每月每人平均操作次数 				|| 每月每个商品操作次数的平均
					// span: 		每人对商品的操作的时间间隔的平均 	|| 每商品被每人操作时间间隔的平均
					// num: 		一共有多少人做这个操作 			|| 一共操作了多少个商品
					"fqn/buy" + "," + "span/buy" + "," + "num/buy" + "," +
					"fqn/click" + "," + "span/click" + "," + "num/click" + "," + 
					"fqn/favourite" + "," + "span/favourite" + "," + "num/favourite" + "," + 
					"fqn/add2cart" + "," + "span/add2cart" + "," + "num/add2cart" + "," + 
					"clickTimes" + "," + "buyTimes" + "," + "favouriteTimes" + "," + "add2cartTimes" +
					"\n");
			Long id;
			double score;
			double fqn, span, num;
			double fqnOfClick, spanOfClick, numOfClick;
			double fqnOfFavourite, spanOfFavourite, numOfFavourite;
			double fqnOfAdd2cart, spanOfAdd2cart, numOfAdd2cart;
			int clickTimes, buyTimes, add2cartTimes, favouriteTimes;
			
			while (iterator.hasNext()) {
				id = iterator.next();
				if (hotBrandsByScore.containsKey(id)) {
					score = hotBrandsByScore.get(id);
				} else {
					score = 0;
				}	
				
				if (frequence.containsKey(id)) {
					fqn = frequence.get(id);
				} else {
					fqn = 0;
				}				
				if (timeSpan.containsKey(id)) {
					span = timeSpan.get(id);
				} else {
					span = 0;
				}				
				if (personNumber.containsKey(id)) {
					num = personNumber.get(id);
				} else {
					num = 0;
				}
				
				if (frequenceOfClick.containsKey(id)) {
					fqnOfClick = frequenceOfClick.get(id);
				} else {
					fqnOfClick = 0;
				}				
				if (timeSpanOfClick.containsKey(id)) {
					spanOfClick = timeSpanOfClick.get(id);
				} else {
					spanOfClick = 0;
				}				
				if (personNumberOfClick.containsKey(id)) {
					numOfClick = personNumberOfClick.get(id);
				} else {
					numOfClick = 0;
				}
				
				if (frequenceOfFavourite.containsKey(id)) {
					fqnOfFavourite = frequenceOfFavourite.get(id);
				} else {
					fqnOfFavourite = 0;
				}				
				if (timeSpanOfFavourite.containsKey(id)) {
					spanOfFavourite = timeSpanOfFavourite.get(id);
				} else {
					spanOfFavourite = 0;
				}				
				if (personNumberOfFavourite.containsKey(id)) {
					numOfFavourite = personNumberOfFavourite.get(id);
				} else {
					numOfFavourite = 0;
				}
				
				if (frequenceOfAdd2cart.containsKey(id)) {
					fqnOfAdd2cart = frequenceOfAdd2cart.get(id);
				} else {
					fqnOfAdd2cart = 0;
				}				
				if (timeSpanOfAdd2cart.containsKey(id)) {
					spanOfAdd2cart = timeSpanOfAdd2cart.get(id);
				} else {
					spanOfAdd2cart = 0;
				}				
				if (personNumberOfAdd2cart.containsKey(id)) {
					numOfAdd2cart = personNumberOfAdd2cart.get(id);
				} else {
					numOfAdd2cart = 0;
				}
				
				clickTimes = userStatistics.getActionTimes(userStatistics.getUser(id), Consts.ActionType.CLICK);
				buyTimes = userStatistics.getActionTimes(userStatistics.getUser(id), Consts.ActionType.BUY);
				favouriteTimes = userStatistics.getActionTimes(userStatistics.getUser(id), Consts.ActionType.FAVOURITE);
				add2cartTimes = userStatistics.getActionTimes(userStatistics.getUser(id), Consts.ActionType.ADD2CART);
				
				fw.write(id + "," + score + "," +
						fqn + "," + span + "," + num + "," +
						fqnOfClick + "," + spanOfClick + "," + numOfClick + "," + 
						fqnOfFavourite + "," + spanOfFavourite + "," + numOfFavourite + "," + 
						fqnOfAdd2cart + "," + spanOfAdd2cart + "," + numOfAdd2cart + "," + 
						clickTimes + "," + buyTimes + "," + favouriteTimes + "," + add2cartTimes +
						"\n");
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
