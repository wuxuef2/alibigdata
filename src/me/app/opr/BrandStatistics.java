package me.app.opr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;




import java.util.concurrent.atomic.AtomicInteger;

import me.app.base.Consts;
import me.app.mdl.Brand;
import me.app.mdl.Behavior;
import me.app.mdl.Row;
import me.app.utl.FileUtil;

public class BrandStatistics extends Statistics{
	private List<Brand> brands = new ArrayList<Brand>();
	private int monthNum = 4;
	public BrandStatistics() {
		super();
		createBrands();
	}
	
	private void createBrands() {
		ArrayList<Row> rows = FileUtil.readFile(INPUT_PATH);
		HashMap<Long, List<Behavior>> ConsumerecordSets = new HashMap<Long, List<Behavior>>();
		
		for (int i = 0; i < rows.size(); i++) {
			Long uid = Long.parseLong(rows.get(i).getUid());
			Long brandID = Long.parseLong(rows.get(i).getBid());
			Date visitDatetime = string2Date(rows.get(i).getDate());
			Integer code = rows.get(i).getType();
			Consts.ActionType type = Consts.ActionType.fromCode(code);
			Behavior consumerecord = new Behavior(brandID, uid, type, visitDatetime);
			
			if (ConsumerecordSets.containsKey(brandID)) {
				ConsumerecordSets.get(brandID).add(consumerecord);
			} else {
				List<Behavior> behaviorList = new ArrayList<Behavior>();
				behaviorList.add(consumerecord);
				ConsumerecordSets.put(brandID, behaviorList);
			}
		}
		
		Iterator<Entry<Long, List<Behavior>>> it = ConsumerecordSets.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, List<Behavior>> entry = (Entry<Long, List<Behavior>>)it.next();
			Brand tmpBrand = new Brand((Long)entry.getKey());
			tmpBrand.setComsumerecords(entry.getValue());
			brands.add(tmpBrand);
		}
	}	
	
	public List<Brand> getBrands() {
		return brands;
	}

	public Brand getBrand(long brandID) {
		for (int i = 0; i < brands.size(); i++) {
			if (brands.get(i).getBrandID() == brandID)
				return brands.get(i);
		}
		
		return null;
	}
	
	public int getActionTimes(Brand brand, Consts.ActionType actionType) {
		List<Behavior> consumerecords = brand.getComsumerecords();
		int Times = 0;
		for (int i = 0; i < consumerecords.size(); i++) {
			if (consumerecords.get(i).getType() == actionType) {
				Times++;
			}
		}
		return Times;
	}
	
	public double getScore(Brand brand) {
		double score = 0;
		for (int j = 0; j < brand.getComsumerecords().size(); j++) {
			score += getWeight(brand.getComsumerecords().get(j).getType());
		}
		return score;
	}
	
	public HashMap<Long, Double> getHot(double threshold) {		
		HashMap<Long, Double> hotBrands = new HashMap<Long, Double>();
		for (int i = 0; i < brands.size(); i++) {
			double score = getScore(brands.get(i));
			Long brandID = brands.get(i).getBrandID();			
			if (score > threshold)
				hotBrands.put(brandID, score);
		}
		
		hotBrands = (HashMap<Long, Double>) sortByValue(hotBrands);		
		return hotBrands;
	}
	
	public double getBrandFrequence(Brand curBrand, Consts.ActionType type, AtomicInteger userNumber) {
		double frequence = 0.0;		
		
		HashMap<Long, Long> bugCounter = new HashMap<Long, Long>();
		for (int j = 0; j < curBrand.getComsumerecords().size(); j++) {
			if (curBrand.getComsumerecords().get(j).getType() == type) {
				long num = 0;
				if (bugCounter.containsKey(curBrand.getComsumerecords().get(j).getUid())) {
					num = bugCounter.get(curBrand.getComsumerecords().get(j).getUid());
				}
				num++;
				bugCounter.put(curBrand.getComsumerecords().get(j).getUid(), num);
			}
		}
		
		Set<Long> set = bugCounter.keySet();
		Iterator<Long> it = set.iterator();
		while(it.hasNext()){
			userNumber.set(userNumber.get() + 1);;
			Long uid = (Long)it.next();
			frequence += bugCounter.get(uid);
	    }
		if (userNumber.get() != 0) { 
			frequence = frequence / userNumber.get() / monthNum;	
		}
		
		return frequence;
	}
	
	public HashMap<Long, Double> getActionFrequenceEveryMonthEveryPerson(Consts.ActionType type, HashMap<Long, Integer> personsNumber) {
		HashMap<Long, Double> hotBrands = new HashMap<Long, Double>();
		for (int i = 0; i < brands.size(); i++) {
			AtomicInteger userNumbers = new AtomicInteger(0);
			double frequence = getBrandFrequence(brands.get(i), type, userNumbers);	
			Long brandID = brands.get(i).getBrandID();		
			
			if (frequence != 0.0) { 	
				hotBrands.put(brandID, frequence);
				personsNumber.put(brandID, userNumbers.get());
			}
		}
		
		hotBrands = (HashMap<Long, Double>) sortByValue(hotBrands);	
		return hotBrands;
	}
	
	public double otherAction2BUY(Brand brand, Consts.ActionType type) {
		int otherActionTimes = getActionTimes(brand, type);
		int buyTimes = getActionTimes(brand, Consts.ActionType.BUY);
		
		return (double)buyTimes / (double)otherActionTimes;
	}
	
	public void isBuyAfterOtherAction(Consts.ActionType type) {
		int counter = 0;
		int buyCounter = 0;
		for (int i = 0; i < brands.size(); i++) {
			Brand brand = brands.get(i);
			Collections.sort(brand.getComsumerecords(), comparator);
			int j = 0;
			while (j < brand.getComsumerecords().size()) {
				long uid = 0;
				for (; j < brand.getComsumerecords().size(); j++) {
					if (brand.getComsumerecords().get(j).getType() == type) {
						uid = brand.getComsumerecords().get(j).getUid();
						counter++;
						break;
					}
				}
				
				if (uid != 0) {
					for (; j < brand.getComsumerecords().size(); j++) {
						if (brand.getComsumerecords().get(j).getType() == Consts.ActionType.BUY &&
								brand.getComsumerecords().get(j).getUid() == uid) {
							System.out.println(brand.getBrandID() + " be bought after " + type);
							buyCounter++;
							break;
						}
					}
				}
			}
		}
		System.out.println(buyCounter + " / " + counter);
	}
}
