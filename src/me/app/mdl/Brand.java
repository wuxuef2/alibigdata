package me.app.mdl;

import java.util.*;

public class Brand {
	private Long brandID;
	private List<Behavior> behaviors;
	
	public Brand(Long brandID) {
		super();
		this.brandID = brandID;
	}

	public Long getBrandID() {
		return brandID;
	}
	
	public void setBrandID(Long brandID) {
		this.brandID = brandID;
	}
	
	public List<Behavior> getComsumerecords() {
		return behaviors;
	}
	
	public void setComsumerecords(List<Behavior> comsumerecords) {
		this.behaviors = comsumerecords;
	}
	
}
