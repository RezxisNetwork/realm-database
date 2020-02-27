package net.rezxis.mchosting.database.object.server;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.ThirdPartyTable;

@Getter@Setter
public class DBThirdParty {

	private int id;
	private String key;
	private UUID owner;
	private boolean locked;
	private Date expire;
	private boolean online;
	private String host;
	private int port;
	private int players;
	private int max;
	private String name;
	private String motd;
	private boolean visible;
	private int score;
	private String icon;
	
	public DBThirdParty(int id, String key, UUID owner, boolean online, boolean locked,
			Date expire, String host, int port, int players, int max, String motd,
			String name, boolean visible, int score, String icon) {
		this.id = id;
		this.key = key;
		this.owner = owner;
		this.online = online;
		this.locked = locked;
		this.expire = expire;
		this.host = host;
		this.port = port;
		this.players = players;
		this.max = max;
		this.motd = motd;
		this.name = name;
		this.visible = visible;
		this.score = score;
		this.icon = icon;
	}
	
	public void update() {
		ThirdPartyTable.getInstance().update(this);
	}
	
	public void sync() {
		ThirdPartyTable.getInstance().load(this);
	}
}