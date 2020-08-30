package net.rezxis.mchosting.database.object;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.RezxisHostTable;

public class HostName {

	@Getter @Setter private int id;
	@Getter @Setter private String host;
	@Getter @Setter private String dest;
	
	public HostName(int id, String k, String v) {
		this.id = id;
		this.host = k;
		this.dest = v;
	}
	
	public void sync() {
		RezxisHostTable.instnace.sync(this);
	}
	
	public void update() {
		RezxisHostTable.instnace.update(this);
	}
}