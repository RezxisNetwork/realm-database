package net.rezxis.mchosting.database.object.anni;

import java.util.Date;

public class StatusSignInfo {

	public String serverName;
	public boolean joinable;
	public int maxPlayers;
	public int onlinePlayers;
	public boolean online;
	public String ip;
	public String icon="";
	public String line1;
	public String line2;
	public String line3;
	public String line4;
	public Date lastUpdated;

	public StatusSignInfo(String name, int max, int onlinePlayers, boolean joinable, boolean online, 
			String ip, String icon, String line1, String line2 , 
			String line3, String line4, Date lastUpdated) {
		this.serverName=name;
		this.maxPlayers=max;
		this.onlinePlayers=onlinePlayers;
		this.joinable=joinable;
		this.online=online;
		this.ip=ip;
		this.icon=icon;
		this.line1=line1;
		this.line2=line2;
		this.line3=line3;
		this.line4=line4;
		this.lastUpdated=lastUpdated;
	}
}