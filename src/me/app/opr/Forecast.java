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
import java.util.List;
import java.util.Set;

import me.app.base.Consts;
import me.app.eval.Evaluator;
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

	public ArrayList<Long> getLike(Topic topic) {
		ArrayList<Long> like = new ArrayList<Long>();
		for (int i = 0; i < topic.getBehaviors().size(); i++) {
			if (topic.getBehaviors().get(i).getType() != Consts.ActionType.CLICK && !like.contains(topic.getBehaviors().get(i).getBrandID())) {
				like.add(topic.getBehaviors().get(i).getBrandID());
			}
		}
		
		return like;
	}	
	
	public void curForecast() {		
		brandStatistics.setForecastMode(deadline);
		userStatistics.setForecastMode(deadline);
		List<User> users = userStatistics.getUsers();
		
		HashMap<Long, Integer> personNumber = new HashMap<Long, Integer>();
		HashMap<Long, Double> frequence = null;
		HashMap<Long, Double> hotBrandsByScore = null;
		hotBrandsByScore = brandStatistics.getHot(0);
		HashMap<Long, Double> usersScore = userStatistics.getHot(0);
		
		frequence = brandStatistics.getActionFrequenceEveryMonthEveryPerson(
				Consts.ActionType.BUY, personNumber);
		HashMap<Long, Double>timeSpan = brandStatistics.getActionTimeSpan(Consts.ActionType.BUY);
		for (int i = 0; i < users.size(); i++) {
			if (!usersScore.containsKey(users.get(i).getId()) || usersScore.get(users.get(i).getId()) < 4) {
				users.get(i).setWillBuy(new HashSet<Long>());
				continue;
			}
			
			ArrayList<Long> like = getLike(users.get(i));
			for (int j = like.size() - 1; j >= 0; j--) {
				if (frequence.containsKey(like.get(j))
						&& frequence.get(like.get(j)) < 0.26) {
					like.remove(j);
					continue;
				}
				if (timeSpan.containsKey(like.get(j)) 
						&& timeSpan.get(like.get(j)) < 1 
						&& personNumber.containsKey(like.get(j))
						&& personNumber.get(like.get(j)) == 1) {
					like.remove(j);
					continue;
				}
				if (hotBrandsByScore.containsKey(like.get(j))
					&& hotBrandsByScore.get(like.get(j)) < 2) {
					like.remove(j);
					continue;
				}
			}
			users.get(i).setWillBuy(new HashSet<Long>(like));
		}
		
		Evaluator evaluator = new Evaluator();
		evaluator.eval(users);
		
	}
	
	public void tmpForecast() {
		FileWriter fw = null;
		String path = "D://3-27.txt";
		List<User> users = userStatistics.getUsers();
		HashMap<Long, Integer> personNumber = new HashMap<Long, Integer>();
		HashMap<Long, Double> hotBrandsByScore = brandStatistics.getHot(0);
		HashMap<Long, Double> frequence = brandStatistics.getActionFrequenceEveryMonthEveryPerson(
				Consts.ActionType.BUY, personNumber);
		HashMap<Long, Double>timeSpan = brandStatistics.getActionTimeSpan(Consts.ActionType.BUY);
		HashMap<Long, Double> usersScore = userStatistics.getHot(0);
		
		try {
			fw = new FileWriter(path);
			for (int i = 0; i < users.size(); i++) {
				if (!usersScore.containsKey(users.get(i).getId()) || usersScore.get(users.get(i).getId()) < 3) {
					users.get(i).setWillBuy(new HashSet<Long>());
					continue;
				}
				
				ArrayList<Long> like = getLike(users.get(i));
				for (int j = like.size() - 1; j >= 0; j--) {
					if (frequence.containsKey(like.get(j))
							&& frequence.get(like.get(j)) < 0.26) {
						like.remove(j);
						continue;
					}
					/*if (timeSpan.containsKey(like.get(j)) 
							&& timeSpan.get(like.get(j)) < 1 
							&& personNumber.containsKey(like.get(j))
							&& personNumber.get(like.get(j)) == 1) {
						like.remove(j);
						continue;
					}*/
					if (hotBrandsByScore.containsKey(like.get(j))
							&& hotBrandsByScore.get(like.get(j)) < 2) {
						like.remove(j);
						continue;
					}
				}
				if (like.size() > 0) {
					String brandsList = new String();
					brandsList += like.get(0);
					for (int j = 1; j < like.size(); j++) {
						brandsList += "," + like.get(j);
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
				
	}
}
