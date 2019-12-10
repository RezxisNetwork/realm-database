package net.rezxis.mchosting.databse;

import java.util.Date;

import net.rezxis.mchosting.databse.tables.BackupsTable;

public class DBBackup {

	private Date creation;
	private String owner;
	private int id;
	private int host;
	private String name;
	
	public DBBackup(int id, int host, String owner, String name, Date creation) {
		this.creation = creation;
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.host = host;
	}
	
	public int getHost() {
		return this.host;
	}
	
	public void setHost(int i) {
		this.host = i;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOwner(String own) {
		this.owner = own;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setCreationDate(Date date) {
		this.creation = date;
	}
	
	public void update() {
		BackupsTable.instnace.update(this);
	}
	
	public Date getCreationDate() {
		return this.creation;
	}
}
