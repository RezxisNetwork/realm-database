package net.rezxis.mchosting.database.object.player;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.Tables;

@Getter@Setter
public class DBVote {

	public DBVote(int id, UUID uuid, int streak, int total, Date lastVote, Date rank) {
		this.id = id;
		this.uuid = uuid;
		this.streak= streak;
		this.total = total;
		this.lastVote = lastVote;
		this.rank = rank;
	}
	
	private int id;
	private UUID uuid;
	private int streak;
	private int total;
	private Date lastVote;
	private Date rank;
	
	public boolean hasRank() {
		return new Date().before(rank);
	}
	
	public void update() {
		Tables.getVTable().update(this);
	}
}
