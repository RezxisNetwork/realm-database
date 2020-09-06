package net.rezxis.mchosting.database.object.server;

import lombok.Getter;
import lombok.Setter;

public class BungeeCordServer {

	@Getter@Setter private int id;
	@Getter@Setter private String name;
	@Getter@Setter private String motd;
	@Getter@Setter private String address;
	@Getter@Setter private int port;
	@Getter@Setter private boolean restricted;
	
	public BungeeCordServer(int id, String name, String motd, String address, int port, boolean restricted) {
		this.id = id;
		this.name = name;
		this.motd = motd;
		this.address = address;
		this.port = port;
		this.restricted = restricted;
	}
}
