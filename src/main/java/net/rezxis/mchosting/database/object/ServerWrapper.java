package net.rezxis.mchosting.database.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import lombok.Getter;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.DBThirdParty;
import net.rezxis.mchosting.database.object.server.ServerStatus;
import net.rezxis.mchosting.database.tables.PlayersTable;
import net.rezxis.mchosting.database.tables.ServersTable;
import net.rezxis.mchosting.database.tables.ThirdPartyTable;

@Getter
public class ServerWrapper {

	private Object object;
	
	public ServerWrapper(Object object) {
		this.object = object;
	}
	
	public boolean isDBServer() {
		return object instanceof DBServer;
	}
	
	public DBServer getDBServer() {
		return (DBServer)object;
	}
	
	public DBThirdParty getDBThirdParty() {
		return (DBThirdParty)object;
	}
	
	public UUID getOwner() {
		if (isDBServer()) {
			return getDBServer().getOwner();
		} else {
			return getDBThirdParty().getOwner();
		}
	}
	
	public String getIcon() {
		if (isDBServer()) {
			return getDBServer().getIcon();
		} else {
			return getDBThirdParty().getIcon();
		}
	}
	
	public int getPlayers() {
		if (isDBServer()) {
			return getDBServer().getPlayers();
		} else {
			return getDBThirdParty().getPlayers();
		}
	}
	
	public String getDisplayName() {
		if (isDBServer()) {
			return getDBServer().getDisplayName();
		} else {
			return getDBThirdParty().getName();
		}
	}
	
	public String getMotd() {
		if (isDBServer()) {
			return getDBServer().getMotd();
		} else {
			return getDBThirdParty().getMotd();
		}
	}
	
	public int getVote() {
		if (isDBServer()) {
			return getDBServer().getVote();
		} else {
			return getDBThirdParty().getScore();
		}
	}
	
	public String getAddress() {
		if (isDBServer())  {
			return getDBServer().getIp();
		} else {
			return getDBThirdParty().getHost();
		}
	}
	
	public int getPort() {
		if (isDBServer()) {
			return getDBServer().getPort();
		} else {
			return getDBThirdParty().getPort();
		}
	}
	
	public static ServerWrapper getServerByName(String name) {
		DBThirdParty dtp = Tables.getTTable().getByName(name);
		if (dtp != null)
			return new ServerWrapper(dtp);
		DBServer ds = Tables.getSTable().getServerByName(name);
		if (ds != null)
			return new ServerWrapper(ds);
		return null;
	}
	
	public static ArrayList<ServerWrapper> getServers(boolean all, String sort) {
		ArrayList<ServerWrapper> list = new ArrayList<>();
		if (all) {
			for (DBServer dbs : ServersTable.instance.getOnlineServers()) {
				list.add(new ServerWrapper(dbs));
			}
			for (DBThirdParty dtp : ThirdPartyTable.getInstance().getOnlineServers()) {
				list.add(new ServerWrapper(dtp));
			}
		} else {
			for (DBServer dbs : ServersTable.instance.getOnlineServersVisible()) {
				list.add(new ServerWrapper(dbs));
			}
			for (DBThirdParty dtp : ThirdPartyTable.getInstance().getOnlineServersVisible()) {
				list.add(new ServerWrapper(dtp));
			}
		}
		if (sort != null) {
			if (sort.equalsIgnoreCase("players")) {
				Collections.sort(list, new PlayersSort());
			} else {
				Collections.sort(list, new ScoresSort());
			}
		}
		
		if (all) {
			ArrayList<DBPlayer> ofb = PlayersTable.instance.ofbPlayers();
			for (DBPlayer dpp : ofb) {
				if (!dpp.isExpiredRank()) {
					DBServer sss = ServersTable.instance.get(dpp.getUUID());
					if (sss != null) {
						if (sss.getStatus() != ServerStatus.RUNNING) {
							list.add(new ServerWrapper(sss));
						}
					}
				}
			}
		}
		return list;
	}
}
