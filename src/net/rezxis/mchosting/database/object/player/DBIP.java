package net.rezxis.mchosting.database.object.player;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.IPTable;

@Getter
@Setter
public class DBIP {

	private int id;
	private String ip;
	private boolean banned;
	private String reason;
	private Date lastUpdate;
	
	public DBIP(int id, String ip, boolean banned, String reason, Date lastUpdate) {
		this.id = id;
		this.ip = ip;
		this.banned = banned;
		this.reason = reason;
		this.lastUpdate = lastUpdate;
	}
	
	public void update() {
		IPTable.instnace.update(this);
	}
}
