package net.rezxis.mchosting.database.object.internal;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.tables.FilesTable;

@Getter
@Setter
public class DBFile {

	private String UUID;
	private String secret;
	private boolean uploaded;
	private Date time;
	private Type type;
	
	public DBFile(String uuid, String secret, boolean uploaded, Date time, Type type) {
		this.UUID = uuid;
		this.secret = secret;
		this.uploaded = uploaded;
		this.time = time;
		this.type = type;
	}
	
	public void sync() {
		FilesTable.instance.sync(this);
	}
	
	public enum Type {
		WORLD,CONFIG,UNKNOWN,PLUGIN;
	}
}
