package net.rezxis.mchosting.databse;

import java.util.UUID;

import net.rezxis.mchosting.databse.tables.PlayersTable;

public class DBPlayer {

	private int id;
	private UUID uuid;
	private Rank rank;
	private long rc;
	private int[] boxes;
	//[Normal,Rare,Vote]
	
	
	public DBPlayer(int id, UUID uuid, Rank rank, long rc, int[] boxes) {
		this.id = id;
		this.uuid = uuid;
		this.rank = rank;
		this.rc = rc;
		this.boxes = boxes;
	}
	
	public int[] getBoxes() {
		return this.boxes;
	}
	
	public void setBoxes(int[] boxes) {
		this.boxes = boxes;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public Rank getRank() {
		return this.rank;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public long getCoin() {
		return this.rc;
	}
	
	public void addCoin(long num) {
		this.rc += num;
	}
	
	public void setCoin(long num) {
		this.rc = num;
	}
	
	public void update() {
		PlayersTable.instance.update(this);
	}
	
	public void sync() {
		PlayersTable.instance.load(this);
	}
	
	public enum Rank {
		
		NORMAL("","1G",20),MEDIA("§a[MEDIA]","2G",25),DEVELOPER("§5[DEVELOPER]","3G",40),STAFF("§2[STAFF]","2G",25)
		,SPECIAL("§d[SPECIAL]","3G",30),PREMIUM("§9[PREMIUM]","2G",25),OWNER("§6[OWNER]","3G",40),TEST("TEST","3G",40);
		
		String prefix;
		String mem;
		int players;
		Rank(String prefix, String mem, int max) {
			this.prefix = prefix;
			this.mem = mem;
			this.players = max;
		}
		
		public String getPrefix() {
			return this.prefix;
		}
		
		public String getMem() {
			return this.mem;
		}
		
		public int getMaxPlayers() {
			return this.players;
		}
	}
}
