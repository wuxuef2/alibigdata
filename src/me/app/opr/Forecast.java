package me.app.opr;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.app.mdl.Brand;
import me.app.mdl.Topic;
import me.app.mdl.User;

public class Forecast {
	private BrandStatistics brandStatistics = new BrandStatistics();
	private UserStatistics userStatistics = new UserStatistics();
	private List<Brand> brands = brandStatistics.getBrands();
	private List<User> users = userStatistics.getUsers();
	protected Date deadline;
	protected int monthNum = 3;
	
	protected Forecast() {
		Calendar myDate = Calendar.getInstance();
		myDate.set(Calendar.MONTH, 7);
		myDate.set(Calendar.DAY_OF_MONTH, 15);
		deadline = myDate.getTime();
	}
	
	public double forecast() {		
		double f1 = 0.0;
		return f1;
	}
	
	public void selectData() {
		//
	}
	
	public void selectTopicData(Topic topic) {
		
	}
}
