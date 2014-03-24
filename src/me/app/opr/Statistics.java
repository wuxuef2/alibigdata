package me.app.opr;

import java.util.Calendar;
import java.util.Date;

import me.app.base.Consts;

public abstract class Statistics {
	protected static final String INPUT_PATH = "D://kuaipan//document//alibidata//t_alibaba_data.csv";
	
	protected Date string2Date(String dateString) {
		int monthIndex = dateString.indexOf("月");
		int dayIndex = dateString.indexOf("日");
		
		int month = Integer.parseInt(dateString.substring(0, monthIndex));
		int day = Integer.parseInt(dateString.substring(monthIndex + 1, dayIndex));
		
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, month);
		myDate.set(Calendar.DAY_OF_MONTH, day);
		
		return myDate.getTime();
	}
	
	protected double getWeight(Consts.ActionType type) {
		double weight = 0;
		
		switch (type) {
		case CLICK:
			weight = 0.5;
			break;
		case BUY:
			weight = 2;
			break;
		case FAVOURITE:
			weight = 1;
			break;
		case ADD2CART:
			weight = 1.5;
		default:
			break;
		}
		
		return weight;
	}
}
