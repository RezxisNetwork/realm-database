package net.rezxis.mchosting.database.object.player;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.UuidTable;

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
	
	public void update() {
		UuidTable.instnace.update(this);
	}
}
