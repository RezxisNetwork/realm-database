package net.rezxis.mchosting.database.object.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.ColorUtil;
import net.rezxis.mchosting.database.tables.PlayersTable;

@Getter
@Setter
public class DBPlayer {

	private int id;
	private UUID UUID;
	private Rank rank;
	private long coin;
	private boolean OfflineBoot;
	private Date RankExpire;
	private Date nextVote;
	private boolean online;
	private boolean ban;
	private String reason;
	private boolean vpnBypass;
	private boolean isSupporter;
	private Date supporterExpire;
	private String prefix;
	private int vault;
	private String verifyCode;
	private long discordId;
	
	public DBPlayer(int id, UUID uuid, Rank rank, long rc, boolean offline, Date exp,
			Date nextVote, boolean online, boolean ban, String reason, boolean vpnBypass,
			boolean isSupporter, Date supporterExpire, String prefix, int vault, String verifyCode, long discordId) {
		this.id = id;
		this.UUID = uuid;
		this.rank = rank;
		this.coin = rc;
		this.OfflineBoot = offline;
		this.RankExpire = exp;
		this.nextVote = nextVote;
		this.online = online;
		this.ban = ban;
		this.reason = reason;
		this.vpnBypass = vpnBypass;
		this.isSupporter = isSupporter;
		this.supporterExpire = supporterExpire;
		this.prefix = prefix;
		this.vault = vault;
		this.verifyCode = verifyCode;
		this.discordId = discordId;
	}
	
	public void addCoin(long num) {
		this.coin += num;
	}
	
	public void update() {
		PlayersTable.instance.update(this);
	}
	
	public boolean isStaff() {
		if (this.rank == Rank.OWNER || this.rank == Rank.DEVELOPER || this.rank == Rank.STAFF)
			return true;
		return false;
	}
	
	public void sync() {
		PlayersTable.instance.load(this);
	}
	
	public boolean isExpiredRank() {
		if (this.rank == Rank.NORMAL)
			return false;
		if (ignore.contains(this.rank))
			return false;
		return this.RankExpire.before(new Date());
	}
	
	public boolean isExpiredSupporter() {
		if (!isSupporter)
			return true;
		if (new Date().after(supporterExpire))
			return true;
		return false;
	}
	
	private static ArrayList<Rank> ignore;
	
	static {
		ignore = new ArrayList<Rank>();
		ignore.add(Rank.DEVELOPER);
		ignore.add(Rank.STAFF);
		ignore.add(Rank.SPECIAL);
		ignore.add(Rank.OWNER);
	}
	
	public enum Rank {
		
		NORMAL("","2G",20,2,false,false),
		DEVELOPER(ColorUtil.COLOR_CHAR+"5[DEVELOPER]","4G",40,5,true,true),STAFF(ColorUtil.COLOR_CHAR+"2[STAFF]","4G",40,5,true,false)
		,SPECIAL(ColorUtil.COLOR_CHAR+"d[SPECIAL]","4G",40,5,true,false),OWNER(ColorUtil.COLOR_CHAR+"6[OWNER]","4G",40,5,true,true)
		,GOLD(ColorUtil.COLOR_CHAR+"6[GOLD]","2G",25,3,true,false),DIAMOND(ColorUtil.COLOR_CHAR+"3[DIAMOND]","3G",30,4,true,false)
		,EMERALD(ColorUtil.COLOR_CHAR+"a[EMERALD]","4G",40,5,true,false)
		,CUSTOM(ColorUtil.COLOR_CHAR+"d[CUSTOM]","4G",40,5,true,true);
		
		String prefix;
		String mem;
		int players;
		boolean boot;
		int backups;
		boolean plugin;
		boolean crateAll;
		
		Rank(String prefix, String mem, int max, int back, boolean boot, boolean plugin) {
			this.prefix = prefix;
			this.mem = mem;
			this.players = max;
			this.boot = boot;
			this.backups = back;
			this.plugin = plugin;
		}
		
		public boolean getPluginUpload() {
			return this.plugin;
		}
		
		public int getMaxBackups() {
			return this.backups;
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
