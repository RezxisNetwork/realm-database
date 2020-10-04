package net.rezxis.mchosting.database.object.server;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.Tables;

@Getter@Setter
public class DBServerPluginLink {

	private int id;
	private int server;
	private int plugin;
	
	public DBServerPluginLink(int id, int server, int plugin) {
		this.id = id;
		this.server = server;
		this.plugin = plugin;
	}
	
	public DBServer getServer() {
		return Tables.getSTable().getByID(server);
	}
	
	public DBPlugin getPlugin() {
		return Tables.getPlTable().getPluginById(plugin);
	}
}
