package net.rezxis.mchosting.database.object.server;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class DBBackupPluginLink {

	private int id;
	private int backup;
	private int plugin;
	
	public DBBackupPluginLink(int id, int backup, int plugin) {
		this.id = id;
		this.backup = backup;
		this.plugin = plugin;
	}
}
