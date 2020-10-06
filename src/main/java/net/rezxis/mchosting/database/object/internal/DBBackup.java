package net.rezxis.mchosting.database.object.internal;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.BackupsTable;

@Getter@Setter
public class DBBackup {

	private Date creation;
	private String owner;
	private int id;
	private String name;
	private String shop;
	
	public DBBackup(int id, String owner, String name, Date creation, String shop) {
		this.creation = creation;
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.shop = shop;
	}
	
	public void update() {
		BackupsTable.instnace.update(this);
	}
}
