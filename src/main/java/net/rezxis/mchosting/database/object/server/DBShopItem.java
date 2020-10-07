package net.rezxis.mchosting.database.object.server;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.Tables;

@Getter@Setter
public class DBShopItem {
	
	private int id;
	private int server;
	private String name;
	private String itemType;
	private String cmd;
	private long price;
	private long earned;
	
	public DBShopItem(int id, int server, String name, String itemType, String cmd, long price, long earned) {
		this.id = id;
		this.server = server;
		this.name = name;
		this.itemType = itemType;
		this.price = price;
		this.cmd = cmd;
		this.earned = earned;
	}
	
	public void update() {
		Tables.getSiTable().update(this);
	}
	
	public void insert() {
		Tables.getSiTable().insert(this);
	}
	
	public void delete() {
		Tables.getSiTable().delete(this);
	}
}