package net.rezxis.mchosting.database.object.server;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.Tables;

@Getter@Setter
public class DBBackupShopItemLink {
	
	private int id;
	private int backup;
	private String name;
	private String itemType;
	private String cmd;
	private long price;
	private long earned;
	
	public DBBackupShopItemLink(int id, int backup, String name, String itemType, String cmd, long price, long earned) {
		this.id = id;
		this.backup = backup;
		this.name = name;
		this.itemType = itemType;
		this.price = price;
		this.cmd = cmd;
		this.earned = earned;
	}
	
	public void update() {
		Tables.getBsiTable().update(this);
	}
	
	public void insert() {
		Tables.getBsiTable().insert(this);
	}
	
	public void delete() {
		Tables.getBsiTable().delete(this);
	}
}
