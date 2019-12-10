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
	private boolean cmd;
	private boolean visible;
	private String icon;
	private DBShop shop;
	private int vote;
	
	public DBServer(int id, String displayName, UUID owner, int port, ArrayList<String> plugins,
			int players, ServerStatus status, String world, int host, String motd,
			boolean cmd, boolean visible, String icon, DBShop shop,int vote) {
		this.id = id;
		this.displayName = displayName;
		this.owner = owner;
		this.port = port;
		this.plugins = plugins;
		this.status = status;
		this.world = world;
		this.host = host;
		this.motd = motd;
		this.cmd = cmd;
		this.visible = visible;
		this.icon = icon;
		this.shop = shop;
		this.vote = vote;
	}
	
	public DBShop getShop() {
		return this.shop;
	}
	
	public int getVote() {
		return this.vote;
	}
	
	public void setVote(int i) {
		this.vote = i;
	}
	
	public void addVote(int i) {
		this.vote+= i;
	}
	
	public void setShop(DBShop shop) {
		this.shop = shop;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getIcon() {
		return this.icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public boolean getCmd() {
		return this.cmd;
	}
	
	public void setCmd(boolean cmd) {
		this.cmd = cmd;
	}
	
	public boolean getVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean bool) {
		this.visible = bool;
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
