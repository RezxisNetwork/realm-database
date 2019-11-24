package net.rezxis.mchosting.databse;

import java.util.Date;

import net.rezxis.mchosting.databse.tables.FilesTable;

public class DBFile {

	private String name;
	private String uuid;
	private String secret;
	private boolean uploaded;
	private Date time;
	private Type type;
	
	public DBFile(String name, String uuid, String secret, boolean uploaded, Date time, Type type) {
		this.name = name;
		this.uuid = uuid;
		this.secret = secret;
		this.uploaded = uploaded;
		this.time = time;
		this.type = type;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getUUID() {
		return this.uuid;
	}
	
	public String getSecret() {
		return this.secret;
	}
	
	public boolean getUploaded() {
		return this.uploaded;
	}
	
	public void setUploaded(boolean b) {
		this.uploaded = b;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public Date getTime() {
		return this.time;
	}
	
	public void sync() {
		FilesTable.instance.sync(this);
	}
	
	public enum Type {
		WORLD,CONFIG,UNKNOWN;
	}
}
