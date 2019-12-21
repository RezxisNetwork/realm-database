package net.rezxis.mchosting.database.object.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBEvent {
	
	private int id;
	private String owner;
	private int target;
	private Type type;
	private String log;
	
	public DBEvent(int id, String owner, int target, Type type, String log) {
		this.id = id;
		this.owner = owner;
		this.target = target;
		this.type = type;
		this.log = log;
	}
	
	public enum Type {
		STOP,KILL,START,RESTART,EXCEPTION;
	}
}
