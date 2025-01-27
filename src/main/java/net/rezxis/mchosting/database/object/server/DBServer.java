package net.rezxis.mchosting.database.object.server;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.ServersTable;

@Getter
@Setter
public class DBServer {
	
	private int id;
	private String displayName;
	private UUID owner;
	private int port;
	private String ip;
	private int players;
	private ServerStatus status;
	private String world;
	private int host;
	private String motd;
	private boolean cmd;
	private boolean visible;
	private String icon;
	//private DBShop shop;
	private int vote;
	private GameType type;
	private String voteCmd;
	private String resource;
	private String direct;
	private Version version;
	
	public DBServer(int id, String displayName, UUID owner, int port, String ip,
			int players, ServerStatus status, String world, int host, String motd,
			boolean cmd, boolean visible, String icon/*, DBShop shop*/,int vote,GameType type,String voteCmd,String resource,String direct
			,Version version) {
		this.id = id;
		this.displayName = displayName;
		this.owner = owner;
		this.port = port;
		this.ip = ip;
		this.players = players;
		this.status = status;
		this.world = world;
		this.host = host;
		this.motd = motd;
		this.cmd = cmd;
		this.visible = visible;
		this.icon = icon;
		//this.shop = shop;
		this.vote = vote;
		this.type = type;
		this.voteCmd = voteCmd;
		this.resource = resource;
		this.direct = direct;
		this.version = version;
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
