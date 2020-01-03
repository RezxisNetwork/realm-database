package net.rezxis.mchosting.database.object.internal;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.BackupsTable;

@Getter
@Setter
public class DBBackup {

	private ArrayList<String> plugins;
	private Date creation;
	private String owner;
	private int id;
	private int host;
	private String name;
	
	public DBBackup(int id, int host, String owner, String name, Date creation, ArrayList<String> plugins) {
		this.creation = creation;
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.host = host;
		this.plugins = plugins;
	}
	
	public void update() {
		BackupsTable.instnace.update(this);
	}
}
