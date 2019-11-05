package net.rezxis.mchosting.databse;

import java.util.ArrayList;
import java.util.UUID;

import net.rezxis.mchosting.databse.tables.ServersTable;

public class DBServer {
	
	private ArrayList<String> plugins;
	private int id;
	private String displayName;
	private UUID owner;
	private int port;
	private int players;
	private ServerStatus status;
	private String world;
	private int host;
	private String motd;
	
	public DBServer(int id, String displayName, UUID owner, int port, ArrayList<String> plugins,
			int players, ServerStatus status, String world, int host, String motd) {
		this.id = id;
		this.displayName = displayName;
		this.owner = owner;
		this.port = port;
		this.plugins = plugins;
		this.status = status;
		this.world = world;
		this.host = host;
		this.motd = motd;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getMotd() {
		return this.motd;
	}
	
	public void setMotd(String motd) {
		this.motd = motd;
	}
	
	public ServerStatus getStatus() {
		return this.status;
	}
	
	public String getWorld() {
		return this.world;
	}
	
	public void setWorld(String world) {
		this.world = world;
	}
	
	public void setStatus(ServerStatus status) {
		this.status = status;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getHost() {
		return this.host;
	}
	
	public void setHost(int host) {
		this.host = host;
	}
	
	public void setDisplayName(String name) {
		this.displayName = name;
	}
	
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void update() {
		ServersTable.instance.update(this);
	}
	
	public ArrayList<String> getPlugins() {
		return this.plugins;
	}
	
	public void setPlugins(ArrayList<String> plugins) {
		this.plugins = plugins;
	}
	
	public void setPlayers(int players) {
		this.players = players;
	}
	
	public int getPlayers() {
		return this.players;
	}
	
	public void sync() {
		ServersTable.instance.load(this);
	}
}
