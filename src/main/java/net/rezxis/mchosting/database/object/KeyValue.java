package net.rezxis.mchosting.database.object;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.RezxisKVTable;

public class KeyValue {

	@Getter @Setter private int id;
	@Getter @Setter private String key;
	@Getter @Setter private String value;
	
	public KeyValue(int id, String k, String v) {
		this.id = id;
		this.key = k;
		this.value = v;
	}
	
	public void sync() {
		RezxisKVTable.instnace.sync(this);
	}
	
	public void update() {
		RezxisKVTable.instnace.update(this);
	}
}
