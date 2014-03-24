package me.app.mdl;

import java.util.*;

public class Brand {
	private Long brandID;
	private List<Consumerecord> comsumerecords;
	
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
	
	public List<Consumerecord> getComsumerecords() {
		return comsumerecords;
	}
	
	public void setComsumerecords(List<Consumerecord> comsumerecords) {
		this.comsumerecords = comsumerecords;
	}
	
}
