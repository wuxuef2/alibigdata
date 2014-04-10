package me.app.opr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.app.base.Consts;
import me.app.eval.Evaluator;
import me.app.mdl.BrandExtend;
import me.app.mdl.Row;
import me.app.mdl.Topic;
import me.app.mdl.User;

public class Forecast {
	private UserStatistics userStatistics = new UserStatistics();	
	private BrandStatistics brandStatistics = new BrandStatistics();
	protected Date deadline;
	protected int monthNum = 3;
	
	public Forecast() {
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, 6);
		myDate.set(Calendar.DAY_OF_MONTH, 15);
		deadline = myDate.getTime();
	}
	
	public double forecast() {		
		double f1 = 0.0;
		return f1;
	}

	public ArrayList<Long> getLike(Topic topic, Date deadline) {		
		ArrayList<Long> like = new ArrayList<Long>();
		for (int i = 0; i < topic.getBehaviors().size(); i++) {
			if (!like.contains(topic.getBehaviors().get(i).getBrandID())) {
				if (topic.getBehaviors().get(i).getType() != Consts.ActionType.CLICK) {
					like.add(topic.getBehaviors().get(i).getBrandID());
				} else {
					int times = userStatistics.getTopicActionTimes(
							topic.getBehaviors(),
							topic.getBehaviors().get(i).getBrandID(),
							Consts.ActionType.CLICK,
							Consts.TopicType.BRAND);
					if (times >= 4 || (times >= 1 && userStatistics.getBehaviorLastHappenTime(
									topic.getBehaviors(),
									topic.getBehaviors().get(i).getBrandID(),
									Consts.ActionType.CLICK,
									Consts.TopicType.BRAND).getTime() >= deadline.getTime())) {
						like.add(topic.getBehaviors().get(i).getBrandID());
					}
				}
			}
		}

		return like;
	}
	
	public void myForecast(List<User> users, Date tmpDate, Date strictDate){		
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, 5);
		myDate.set(Calendar.DAY_OF_MONTH, 1);
		Date forgetTime = myDate.getTime();
		
		HashMap<Long, Integer> personNumber = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequence = null;
		HashMap<Long, Double> hotBrandsByScore = null;
		hotBrandsByScore = brandStatistics.getHot(0);
		HashMap<Long, Double> usersScore = userStatistics.getHot(0);
		
		frequence = brandStatistics.getActionFrequenceEveryMonthEveryPerson(
				Consts.ActionType.BUY, personNumber);
		HashMap<Long, Double>timeSpan = brandStatistics.getActionTimeSpan(Consts.ActionType.BUY);
		for (int i = 0; i < users.size(); i++) {
			if (!usersScore.containsKey(users.get(i).getId()) || usersScore.get(users.get(i).getId()) < 2) {
				users.get(i).setWillBuy(new HashSet<Long>());
				continue;
			}
			User user = users.get(i);
			ArrayList<Long> like = getLike(user, tmpDate);
			for (int j = like.size() - 1; j >= 0; j--) {
				BrandExtend brand = brandStatistics.getBrand(like.get(j));
				int userBuyTimes = brandStatistics.getTopicActionTimes(
						user.getBehaviors(), like.get(j),
						Consts.ActionType.BUY, Consts.TopicType.BRAND);
				int userClickTimes = brandStatistics.getTopicActionTimes(
						user.getBehaviors(), like.get(j),
						Consts.ActionType.CLICK, Consts.TopicType.BRAND);
				
				Date firstVistDate = userStatistics.getBehaviorFirstHappenTime(
						user.getBehaviors(), 
						like.get(j),
						Consts.ActionType.CLICK, 
						Consts.TopicType.BRAND);
				Date tmpVistDate = userStatistics.getBehaviorFirstHappenTime(
						user.getBehaviors(), 
						like.get(j),
						Consts.ActionType.BUY, 
						Consts.TopicType.BRAND);
				if (tmpVistDate.before(firstVistDate)) firstVistDate = tmpVistDate;
				Date lastVistDate = userStatistics.getBehaviorLastHappenTime(
						user.getBehaviors(), 
						like.get(j),
						Consts.ActionType.CLICK, 
						Consts.TopicType.BRAND);
				tmpVistDate = userStatistics.getBehaviorLastHappenTime(
						user.getBehaviors(), 
						like.get(j),
						Consts.ActionType.BUY, 
						Consts.TopicType.BRAND);
				if (tmpVistDate.after(firstVistDate)) lastVistDate = tmpVistDate;
				
				// 只会被人在短时间内关注的非热门商品
				if (lastVistDate.getTime() - firstVistDate.getTime() < 7 * 24 * 60 * 60 * 1000
						&& lastVistDate.before(tmpDate) 
						&& brand.getBuyPersons() <= 4) {
					like.remove(j);
					continue;
				}
				
				// 神来之笔
				if ((brand.getBuyTimes() < 1 || brand.getBuyPersons() < 2)
						&& brand.getClickPersons() > 3) {
					like.remove(j);
					continue;
				}
				
				// 该用户在很早之前的购买行为，并且在之后未关注过该商品
				if (userBuyTimes >= 1
						&& userStatistics.getBehaviorLastHappenTime(
								user.getBehaviors(), like.get(j),
								Consts.ActionType.BUY, 
								Consts.TopicType.BRAND)
								.getTime() <= tmpDate.getTime()
						&& userClickTimes >= 1
						&& userStatistics.getBehaviorLastHappenTime(
								user.getBehaviors(),
								like.get(j), 
								Consts.ActionType.CLICK,
								Consts.TopicType.BRAND)
								.getTime() <= tmpDate.getTime()) {
					like.remove(j);
					continue;
				}
								
				// 该商品很多人点，但很少人买
				if (brand.getClickPersons() > 30 && brand.getBuyTimes() < 5) {
					like.remove(j);
					continue;
				}
				
				// 冷门商品
				if (!hotBrandsByScore.containsKey(like.get(j))
						|| hotBrandsByScore.get(like.get(j)) < 1.5) {
					like.remove(j);
					continue;
				}
				
				// 耐用品，并且已经购买过的
				if (brand.getMostBuyTimes() == 1 && brand.getBuyPersons() > 35
						&& userBuyTimes == 1) {
					like.remove(j);
					continue;
				}
				
				// 一定为非周期的物品
				if ((brand.getMostBuyTimes() == 0 && lastVistDate.before(tmpDate))
						|| (brand.getMostBuyTimes() == 1 && brand.getLastBuyTimes().before(tmpDate))) {
					like.remove(j);
					continue;
				}
				/*
				if (userBuyTimes + brandStatistics.getTopicActionTimes(
						user.getBehaviors(), like.get(j),
						Consts.ActionType.FAVOURITE, Consts.TopicType.BRAND) 
						+ brandStatistics.getTopicActionTimes(
								user.getBehaviors(), like.get(j),
								Consts.ActionType.ADD2CART, Consts.TopicType.BRAND) == 0 && lastVistDate.getTime() >= tmpDate.getTime()
						&& lastVistDate.getTime() <= strictDate.getTime()) {
					like.remove(j);
					continue;
				}*/
			}
			users.get(i).setWillBuy(new HashSet<Long>(like));
		}
	}
	
	public void curForecast() {		
		brandStatistics.setForecastMode(deadline);
		userStatistics.setForecastMode(deadline);
		List<User> users = userStatistics.getUsers();
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, 6);
		myDate.set(Calendar.DAY_OF_MONTH, 7);
		Date tmpDate = myDate.getTime();

		Calendar thisDate = Calendar.getInstance();
		thisDate.set(Calendar.MONTH, 6);
		thisDate.set(Calendar.DAY_OF_MONTH, 7);
		Date strictDate = thisDate.getTime();
		myForecast(users, tmpDate, strictDate);		
		
		Evaluator evaluator = new Evaluator();
		evaluator.eval(users);		
	}
	
	public void tmpForecast() {
		FileWriter fw = null;
		String path = "D://4-10.txt";
		List<User> users = userStatistics.getUsers();
		int all = 0;
		
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, 7);
		myDate.set(Calendar.DAY_OF_MONTH, 4);
		Date tmpDate = myDate.getTime();
		
		Calendar thisDate = Calendar.getInstance();
		thisDate.set(Calendar.MONTH, 7);
		thisDate.set(Calendar.DAY_OF_MONTH, 6);
		Date strictDate = thisDate.getTime();
		myForecast(users, tmpDate, strictDate);
		
		
		try {
			fw = new FileWriter(path);
			for (int i = 0; i < users.size(); i++) {								
				Set<Long> like = users.get(i).getWillBuy();
				if (like.size() > 0) {
					Iterator<Long> iterator = like.iterator();
					String brandsList = new String();
					brandsList += iterator.next();
					all++;
					while (iterator.hasNext()) {
						all++;
						brandsList += "," + iterator.next();
					}
					brandsList += "\n";
					fw.write(users.get(i).getId() + "\t" + brandsList);
				} 
				
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
		
		System.out.println("items: " + all);
	}
}
