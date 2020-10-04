package net.rezxis.mchosting.database.object.server;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.Tables;

@Getter@Setter
public class DBServerPluginLink {

	private int id;
	private int server;
	private String name;
	private int plugin;
	private boolean enabled;
	private boolean lastEnabled;
	
	public DBServerPluginLink(int id, int server, String name, int plugin, boolean enabled, boolean lastEnabled) {
		this.id = id;
		this.server = server;
		this.name = name;
		this.plugin = plugin;
		this.enabled = enabled;
		this.lastEnabled = lastEnabled;
	}
	
	public DBServer getDBServer() {
		return Tables.getSTable().getByID(server);
	}
	
	public DBPlugin getDBPlugin() {
		return Tables.getPlTable().getPluginById(plugin);
	}
	
	public void update() {
		Tables.getSplTable().update(this);
	}
}
