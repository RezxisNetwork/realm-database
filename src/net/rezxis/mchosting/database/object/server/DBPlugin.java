package net.rezxis.mchosting.database.object.server;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class DBPlugin {

	private String name;
	private String jarName;
	private String version;
	private ArrayList<String> depends;
	
	public DBPlugin(String name, String jarName, String version, ArrayList<String> depends) {
		this.name = name;
		this.jarName = jarName;
		this.version = version;
		this.depends = depends;
	}
}