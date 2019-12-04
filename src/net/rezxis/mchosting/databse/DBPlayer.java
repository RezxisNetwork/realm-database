package net.rezxis.mchosting.databse;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import net.rezxis.mchosting.databse.tables.PlayersTable;

public class DBPlayer {

	private int id;
	private UUID uuid;
	private Rank rank;
	private long rc;
	private boolean offlineBoot;
	private Date rankExp;
	
	public DBPlayer(int id, UUID uuid, Rank rank, long rc, boolean offline, Date exp) {
		this.id = id;
		this.uuid = uuid;
		this.rank = rank;
		this.rc = rc;
		this.offlineBoot = offline;
		this.rankExp = exp;
	}
	
	public Date getRankExpire() {
		return this.rankExp;
	}
	
	public void setRankExpire(Date date) {
		this.rankExp = date;
	}
	
	public boolean getOfflineBoot() {
		return this.offlineBoot;
	}
	
	public void setOfflineBoot(boolean val) {
		this.offlineBoot = val;
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
	
	public boolean isExpiredRank() {
		if (this.rank == Rank.NORMAL)
			return false;
		if (ignore.contains(this.rank))
			return false;
		return this.rankExp.before(new Date());
	}
	
	private static ArrayList<Rank> ignore;
	
	static {
		ignore = new ArrayList<Rank>();
		ignore.add(Rank.MEDIA);
		ignore.add(Rank.DEVELOPER);
		ignore.add(Rank.STAFF);
		ignore.add(Rank.SPECIAL);
		ignore.add(Rank.OWNER);
	}
	
	public enum Rank {
		
		NORMAL("","1G",20),MEDIA("§a[MEDIA]","2G",25,true),
		DEVELOPER("§5[DEVELOPER]","3G",40,true),STAFF("§2[STAFF]","2G",25,true)
		,SPECIAL("§d[SPECIAL]","3G",30,true),OWNER("§6[OWNER]","4G",40,true)
		,GOLD("§6[GOLD]","2G",25,true),DIAMOND("§3[DIAMOND]","3G",30,true),EMERALD("§a[EMERALD]","4G",40,true);
		
		String prefix;
		String mem;
		int players;
		boolean boot;
		
		Rank(String prefix, String mem, int max) {
			this.prefix = prefix;
			this.mem = mem;
			this.players = max;
			this.boot = false;
		}
		
		Rank(String prefix, String mem, int max, boolean boot) {
			this.prefix = prefix;
			this.mem = mem;
			this.players = max;
			this.boot = boot;
		}
		
		public String getPrefix() {
			return this.prefix;
		}
		
		public boolean getOfflineBoot() {
			return this.boot;
		}
		
		public String getMem() {
			return this.mem;
		}
		
		public int getMaxPlayers() {
			return this.players;
		}
	}
}
