package me.app.opr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;




import java.util.concurrent.atomic.AtomicInteger;

import me.app.base.Consts;
import me.app.mdl.Brand;
import me.app.mdl.Behavior;
import me.app.mdl.Row;
import me.app.utl.FileUtil;

public class BrandStatistics extends Statistics{
	private List<Brand> brands = new ArrayList<Brand>();
	
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
			tmpBrand.setBehaviors(entry.getValue());
			brands.add(tmpBrand);
		}
	}	
	
	public List<Brand> getBrands() {
		return brands;
	}

	public Brand getBrand(long brandID) {
		for (int i = 0; i < brands.size(); i++) {
			if (brands.get(i).getId() == brandID)
				return brands.get(i);
		}
		
		return null;
	}
		
	public HashMap<Long, Double> getHot(double threshold) {		
		HashMap<Long, Double> hotBrands = new HashMap<Long, Double>();
		for (int i = 0; i < brands.size(); i++) {
			double score = getScore(brands.get(i));
			Long brandID = brands.get(i).getId();			
			if (score > threshold)
				hotBrands.put(brandID, score);
		}
		
		hotBrands = (HashMap<Long, Double>) sortByValue(hotBrands);		
		return hotBrands;
	}
	
	public HashMap<Long, Double> getActionTimeSpan(Consts.ActionType type) {
		HashMap<Long, Double> timespans = new HashMap<Long, Double>();
		for (int i = 0; i < brands.size(); i++) {
			double timespan = getActionTimeSpan(brands.get(i), type, Consts.TopicType.BRAND);
			Long brandID = brands.get(i).getId();
			timespans.put(brandID, timespan);
		}
		timespans = (HashMap<Long, Double>) sortByValue(timespans);
		return timespans;
	}
	
	public HashMap<Long, Double> getActionFrequenceEveryMonthEveryPerson(Consts.ActionType type, HashMap<Long, Integer> personsNumber) {
		HashMap<Long, Double> hotBrands = new HashMap<Long, Double>();
		for (int i = 0; i < brands.size(); i++) {
			AtomicInteger userNumbers = new AtomicInteger(0);
			double frequence = getTopicFrequence(brands.get(i), type, userNumbers, Consts.TopicType.BRAND);	
			Long brandID = brands.get(i).getId();		
			
			if (frequence != 0.0) { 	
				hotBrands.put(brandID, frequence);
				personsNumber.put(brandID, userNumbers.get());
			}
		}
		
		hotBrands = (HashMap<Long, Double>) sortByValue(hotBrands);	
		return hotBrands;
	}
		
	public void isBuyAfterOtherAction(Consts.ActionType type) {
		int counter = 0;
		int buyCounter = 0;
		for (int i = 0; i < brands.size(); i++) {
			Brand brand = brands.get(i);
			Collections.sort(brand.getBehaviors(), comparator);
			int j = 0;
			while (j < brand.getBehaviors().size()) {
				long uid = 0;
				for (; j < brand.getBehaviors().size(); j++) {
					if (brand.getBehaviors().get(j).getType() == type) {
						uid = brand.getBehaviors().get(j).getUid();
						counter++;
						break;
					}
				}
				
				if (uid != 0) {
					for (; j < brand.getBehaviors().size(); j++) {
						if (brand.getBehaviors().get(j).getType() == Consts.ActionType.BUY &&
								brand.getBehaviors().get(j).getUid() == uid) {
							System.out.println(brand.getId() + " be bought after " + type);
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
