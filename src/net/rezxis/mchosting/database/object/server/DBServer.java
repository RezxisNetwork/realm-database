package net.rezxis.mchosting.database.object.server;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.ServersTable;

@Getter
@Setter
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
	private GameType type;
	
	public DBServer(int id, String displayName, UUID owner, int port, ArrayList<String> plugins,
			int players, ServerStatus status, String world, int host, String motd,
			boolean cmd, boolean visible, String icon, DBShop shop,int vote,GameType type) {
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
		this.type = type;
	}
	
	public void addVote(int i) {
		this.vote+= i;
	}
	
	public void sync() {
		ServersTable.instance.load(this);
	}
	
	public void update() {
		ServersTable.instance.update(this);
	}
	
	public enum GameType {
		NORMAL,CUSTOM;
	}
}
