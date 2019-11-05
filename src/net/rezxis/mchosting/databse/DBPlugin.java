package net.rezxis.mchosting.databse;

import java.util.ArrayList;

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
	
	public String getName() {
		return this.name;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public String getJarName() {
		return this.jarName;
	}
	
	public ArrayList<String> getDepends() {
		return this.depends;
	}
}