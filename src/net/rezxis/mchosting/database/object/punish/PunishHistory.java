package net.rezxis.mchosting.database.object.punish;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter@Setter@AllArgsConstructor
public class PunishHistory {

	private int id;
	private String target;
	private String reason;
	private TargetType targetType;
	private PunishType type;
	private String from;
	private Date start;
	private Date end;//if null permanent
	
	public boolean isExpired() {
		if (end == null) return false;
		return end.before(new Date());
	}
}