package net.rezxis.mchosting.databse;

import java.util.UUID;

public class DBPlayer {

	private int id;
	private UUID uuid;
	private Rank rank;
	private long rc;
	
	public DBPlayer(int id, UUID uuid, Rank rank, long rc) {
		this.id = id;
		this.uuid = uuid;
		this.rank = rank;
		this.rc = rc;
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
		
	}
	
	public void sync() {
		
	}
	
	public enum Rank {
		
		NORMAL(""),SPONSOR("§6[SPONSOR]"),DEVELOPER("§5[DEVELOPER]"),STAFF("§2[STAFF]"),SPECIAL("§d[SPECIAL]"),
		PREMIUM("§9[PREMIUM]");
		
		String prefix;
		Rank(String prefix) {
			this.prefix = prefix;
		}
		
		public String getPrefix() {
			return this.prefix;
		}
	}
}
