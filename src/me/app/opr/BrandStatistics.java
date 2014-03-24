package me.app.opr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import me.app.base.Consts;
import me.app.mdl.Brand;
import me.app.mdl.Consumerecord;
import me.app.mdl.Row;
import me.app.utl.FileUtil;

public class BrandStatistics extends Statistics{
private List<Brand> brands = new ArrayList<Brand>();
	
	public void createUsers() {
		ArrayList<Row> rows = FileUtil.readFile(INPUT_PATH);
		HashMap<Long, List<Consumerecord>> ConsumerecordSets = new HashMap<Long, List<Consumerecord>>();
		
		for (int i = 0; i < rows.size(); i++) {
			Long uid = Long.parseLong(rows.get(i).getUid());
			Long brandID = Long.parseLong(rows.get(i).getBid());
			Date visitDatetime = string2Date(rows.get(i).getDate());
			Integer code = rows.get(i).getType();
			Consts.ActionType type = Consts.ActionType.fromCode(code);
			Consumerecord consumerecord = new Consumerecord(uid, type, visitDatetime);
			
			if (ConsumerecordSets.containsKey(brandID)) {
				ConsumerecordSets.get(brandID).add(consumerecord);
			} else {
				List<Consumerecord> behaviorList = new ArrayList<Consumerecord>();
				behaviorList.add(consumerecord);
				ConsumerecordSets.put(uid, behaviorList);
			}
		}
		
		Iterator<Entry<Long, List<Consumerecord>>> it = ConsumerecordSets.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, List<Consumerecord>> entry = (Entry<Long, List<Consumerecord>>)it.next();
			Brand tmpBrand = new Brand((Long)entry.getKey());
			tmpBrand.setComsumerecords(entry.getValue());
			brands.add(tmpBrand);
		}
	}
	
	public Brand getBrand(long brandID) {
		for (int i = 0; i < brands.size(); i++) {
			if (brands.get(i).getBrandID() == brandID)
				return brands.get(i);
		}
		
		return null;
	}
	
	public int getActionTimes(Brand brand, Consts.ActionType actionType) {
		List<Consumerecord> consumerecords = brand.getComsumerecords();
		int Times = 0;
		for (int i = 0; i < consumerecords.size(); i++) {
			if (consumerecords.get(i).getType() == actionType) {
				Times++;
			}
		}
		return Times;
	}
	
	public HashMap<Brand, Double> getHot() {		
		HashMap<Brand, Double> HotBrands = new HashMap<Brand, Double>();
		for (int i = 0; i < brands.size(); i++) {
			
		}
		
		return HotBrands;
	}
	
	public double otherAction2BUY(Brand brand, Consts.ActionType type) {
		int otherActionTimes = getActionTimes(brand, type);
		int buyTimes = getActionTimes(brand, Consts.ActionType.BUY);
		
		return (double)buyTimes / (double)otherActionTimes;
	}
	
	public void isBuyAfterADD2CART() {
		
	}
}
