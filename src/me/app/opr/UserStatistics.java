package me.app.opr;

import me.app.mdl.*;
import me.app.utl.FileUtil;
import me.app.base.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * 统计用户等各类数据
 * User: wuxuef
 * Date: 3/24/14
 * Time: 05:00 PM
 */
public class UserStatistics extends Statistics {
	private List<User> users = new ArrayList<User>();
	
	public void createUsers() {
		ArrayList<Row> rows = FileUtil.readFile(INPUT_PATH);
		HashMap<Long, List<Behavior>> behaviorsSets = new HashMap<Long, List<Behavior>>();
		
		for (int i = 0; i < rows.size(); i++) {
			Long uid = Long.parseLong(rows.get(i).getUid());
			Long brandID = Long.parseLong(rows.get(i).getBid());
			Date visitDatetime = string2Date(rows.get(i).getDate());
			Integer code = rows.get(i).getType();
			Consts.ActionType type = Consts.ActionType.fromCode(code);
			Behavior tmpBehavior = new Behavior(brandID, uid, type, visitDatetime);
			
			if (behaviorsSets.containsKey(uid)) {
				behaviorsSets.get(uid).add(tmpBehavior);
			} else {
				List<Behavior> behaviorList = new ArrayList<Behavior>();
				behaviorList.add(tmpBehavior);
				behaviorsSets.put(uid, behaviorList);
			}
		}
		
		Iterator<Entry<Long, List<Behavior>>> it = behaviorsSets.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, List<Behavior>> entry = (Entry<Long, List<Behavior>>)it.next();
			User tmpUser = new User((Long)entry.getKey());
			tmpUser.setBehaviors(entry.getValue());
			users.add(tmpUser);
		}
	}
	
	public User getUser(long uid) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUid() == uid)
				return users.get(i);
		}
		
		return null;
	}
	
	public int getActionTimes(User user, Consts.ActionType actionType) {
		List<Behavior> behaviorList = user.getBehaviors();
		int Times = 0;
		for (int i = 0; i < behaviorList.size(); i++) {
			if (behaviorList.get(i).getType() == actionType) {
				Times++;
			}
		}
		return Times;
	}

}
