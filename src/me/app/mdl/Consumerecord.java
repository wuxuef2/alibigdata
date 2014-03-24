package me.app.mdl;

import java.util.Date;

import me.app.base.Consts;
import me.app.base.Consts.ActionType;

public class Consumerecord {
	private Long uid;
	private Consts.ActionType type;
	private Date consumeDatetimeD;
	
	public Consumerecord(Long uid, ActionType type, Date consumeDatetimeD) {
		super();
		this.uid = uid;
		this.type = type;
		this.consumeDatetimeD = consumeDatetimeD;
	}
	
	public Long getUid() {
		return uid;
	}
	
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	public Consts.ActionType getType() {
		return type;
	}
	
	public void setType(Consts.ActionType type) {
		this.type = type;
	}
	
	public Date getConsumeDatetimeD() {
		return consumeDatetimeD;
	}
	public void setConsumeDatetimeD(Date consumeDatetimeD) {
		this.consumeDatetimeD = consumeDatetimeD;
	}
}
