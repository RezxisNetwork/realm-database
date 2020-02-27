package net.rezxis.mchosting.database.object.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBPIP {

	private int id;
	private int ip;
	private int player;
	
	public DBPIP(int id, int ip, int player) {
		this.id = id;
		this.ip = ip;
		this.player = player;
	}
}
