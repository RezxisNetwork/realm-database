package net.rezxis.mchosting.database.object.player;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class DBUUID {

	private int id;
	private String name;
	private UUID uuid;
	
	public DBUUID(int id, String name, UUID uuid) {
		this.id = id;
		this.name = name;
		this.uuid = uuid;
	}
}
