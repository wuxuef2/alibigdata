package me.app.opr;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.app.base.Consts;
import me.app.mdl.Behavior;

public abstract class Statistics {
	protected static final String INPUT_PATH = "D://kuaipan//document//alibidata//t_alibaba_data.csv";
	Comparator<Behavior> comparator = new Comparator<Behavior>(){
	   public int compare(Behavior behavior1, Behavior behavior2) {
		   return behavior1.getVisitDatetime().compareTo(behavior2.getVisitDatetime());
	   }
	 };
	
	protected Date string2Date(String dateString) {
		int month = 0;
		int day = 0;
		
		int i;
		for (i = 0; i < dateString.length(); i++) {
			if (Character.isDigit(dateString.charAt(i))) {
				month = month * 10 + dateString.charAt(i) - '0';
			} else {
				break;
			}
		}
		for (; i < dateString.length(); i++) {
			if (Character.isDigit(dateString.charAt(i))) {
				day = day * 10 + dateString.charAt(i) - '0';
			} else {
				break;
			}
		}
		
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, month);
		myDate.set(Calendar.DAY_OF_MONTH, day);
		
		return myDate.getTime();
	}
	
	protected double getWeight(Consts.ActionType type) {
		double weight = 0;
		
		switch (type) {
		case CLICK:
			weight = 777.0 / 10239.0;
			break;
		case BUY:
			weight = 1.0;
			break;
		case FAVOURITE:
			weight = 86.0 / 819.0;
			break;
		case ADD2CART:
			weight = 13.0 / 125.0;
		default:
			break;
		}
		
		return weight;
	}
	
	public <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return (o1.getValue()).compareTo( o2.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}
}
