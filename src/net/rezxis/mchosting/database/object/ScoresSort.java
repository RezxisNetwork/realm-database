package net.rezxis.mchosting.database.object;

import java.util.Comparator;

import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.DBThirdParty;

public class ScoresSort implements Comparator<ServerWrapper> {

	@Override
	public int compare(ServerWrapper o1, ServerWrapper o2) {
		int i1 = 0;
		int i2 = 0;
		if (o1.isDBServer()) {
			i1 = ((DBServer)o1.getObject()).getVote();
		} else {
			i1 = ((DBThirdParty)o1.getObject()).getScore();
		}
		if (o2.isDBServer()) {
			i2 = ((DBServer)o2.getObject()).getVote();
		} else {
			i2 = ((DBThirdParty)o2.getObject()).getScore();
		}
		return i1 < i2 ? 1: -1;
	}
}