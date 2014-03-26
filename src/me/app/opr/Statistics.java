package me.app.opr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import me.app.base.Consts;
import me.app.mdl.Behavior;
import me.app.mdl.Topic;

public abstract class Statistics {
	protected static final String INPUT_PATH = "D://kuaipan//document//alibidata//t_alibaba_data.csv";
	protected int monthNum = 3;
	protected Date deadline;
	
	protected Statistics() {
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, 7);
		myDate.set(Calendar.DAY_OF_MONTH, 15);
		deadline = myDate.getTime();
	}
	
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
		for (i += 2; i < dateString.length(); i++) {
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
	
	public int getActionTimes(Topic topic, Consts.ActionType actionType) {
		List<Behavior> consumerecords = topic.getBehaviors();
		int Times = 0;
		for (int i = 0; i < consumerecords.size(); i++) {
			if (consumerecords.get(i).getType() == actionType) {
				Times++;
			}
		}
		return Times;
	}
	
	public double getScore(Topic topic) {
		double score = 0;
		for (int j = 0; j < topic.getBehaviors().size(); j++) {
			score += getWeight(topic.getBehaviors().get(j).getType());
		}
		return score;
	}
	

	public double getTopicFrequence(Topic topic, Consts.ActionType type, AtomicInteger number, Consts.TopicType topicType) {
		HashMap<Long, Long> bugCounter = new HashMap<Long, Long>();
		for (int j = 0; j < topic.getBehaviors().size(); j++) {
			
			Long myId;
			if (topicType == Consts.TopicType.BRAND) {
				myId = topic.getBehaviors().get(j).getUid();
			} else {
				myId = topic.getBehaviors().get(j).getBrandID();
			}
			
			if (topic.getBehaviors().get(j).getType() == type) {
				long num = 0;
				if (bugCounter.containsKey(myId)) {
					num = bugCounter.get(myId);
				}
				num++;
				bugCounter.put(myId, num);
			}
		}
		
		double frequence = 0.0;	
		number.set(0);
		Set<Long> set = bugCounter.keySet();
		Iterator<Long> it = set.iterator();
		while (it.hasNext()){
			number.set(number.get() + 1);;
			Long myId = (Long)it.next();
			frequence += bugCounter.get(myId);
	    }
		if (number.get() != 0) { 
			frequence = frequence / number.get() / monthNum;	
		}
		
		return frequence;
	}
	
	public double getActionTimeSpan(Topic topic, Consts.ActionType type, Consts.TopicType topicType) {
		HashMap<Long, List<Date>> actionCounter = new HashMap<Long, List<Date>>();
		for (int i = 0; i < topic.getBehaviors().size(); i++) {
			Long myId;
			if (topicType == Consts.TopicType.BRAND) {
				myId = topic.getBehaviors().get(i).getUid();
			} else {
				myId = topic.getBehaviors().get(i).getBrandID();
			}
			
			if (topic.getBehaviors().get(i).getType() == type) {
				if (actionCounter.containsKey(myId)) {
					actionCounter.get(myId).add(topic.getBehaviors().get(i).getVisitDatetime());
				} else {
					List<Date> dateList = new ArrayList<Date>();
					dateList.add(topic.getBehaviors().get(i).getVisitDatetime());
					actionCounter.put(myId, dateList);
				}
			}
		}
		
		double timeSpan = 0.0;
		Set<Long> set = actionCounter.keySet();
		Iterator<Long> it = set.iterator();
		int counter = 0;
		while (it.hasNext()) {
			counter++;
			double timeSpanSum = 0;
			int timeSpanNumber = 0;
			Long myId = (Long)it.next();
			List<Date> list = actionCounter.get(myId);
			Collections.sort(list);
			for (int i = 1; i < list.size(); i++) {
				timeSpanNumber++;
				timeSpanSum += (list.get(i).getTime() - list.get(i - 1).getTime()) / (24 * 60 * 60 * 1000);
			}
			
			if (timeSpanNumber != 0) {
				timeSpan += timeSpanSum / timeSpanNumber;				
			}
		}
		
		if (counter != 0) { 
			timeSpan /= counter;
		}
		return timeSpan;
	}
	

	public double otherAction2BUY(Topic topic, Consts.ActionType type) {
		int otherActionTimes = getActionTimes(topic, type);
		int buyTimes = getActionTimes(topic, Consts.ActionType.BUY);
		
		return (double)buyTimes / (double)otherActionTimes;
	}
			
}
