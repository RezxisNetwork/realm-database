package net.rezxis.mchosting.database.object.server;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class DBPlugin {

	private int id;
	private String name;
	private String jarName;
	private String version;
	private ArrayList<String> depends;
	
	public DBPlugin(int id, String name, String jarName, String version, ArrayList<String> depends) {
		this.id = id;
		this.name = name;
		this.jarName = jarName;
		this.version = version;
		this.depends = depends;
	}
}