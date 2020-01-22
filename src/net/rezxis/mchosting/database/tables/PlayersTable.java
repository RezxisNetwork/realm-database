package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.player.DBPlayer.Rank;

public class PlayersTable extends MySQLStorage {

	public static PlayersTable instance;
	public static Gson gson = new Gson();
	
	public PlayersTable() {
		super("players");
		instance = this;
		prefix = "rezxis_";
		tablename = "players";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
		map.put("uuid", "text,");
		map.put("rank", "text,");
		map.put("coin", "INT,");
		map.put("ofb", "boolean,");
		map.put("rexp", "datetime,");
		map.put("nvote", "datetime,");
		map.put("online", "boolean,");
		map.put("ban","boolean,");
		map.put("reason","text,");
		map.put("vpn", "boolean,");
		map.put("support", "boolean,");
		map.put("suexp", "datetime,");
		map.put("prefix", "text,");
		map.put("vault", "INT");
		createTable(map);
	}
	
	public boolean load(DBPlayer player) {
        return (Boolean) executeQuery(new Query(selectFromTable("*") + " WHERE uuid = ?",player.getUUID().toString()) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                    	player.setId(resultSet.getInt("id"));
                    	player.setRank(Rank.valueOf(resultSet.getString("rank")));
                    	player.setCoin(resultSet.getInt("coin"));
                    	player.setOfflineBoot(resultSet.getBoolean("ofb"));
                    	player.setRankExpire(resultSet.getDate("rexp"));
                    	player.setNextVote(resultSet.getDate("nvote"));
                    	player.setOnline(resultSet.getBoolean("online"));
                    	player.setBan(resultSet.getBoolean("ban"));
                    	player.setReason(resultSet.getString("reason"));
                    	player.setVpnBypass(resultSet.getBoolean("vpn"));
                    	player.setSupporter(resultSet.getBoolean("support"));
                    	player.setSupporterExpire(resultSet.getDate("suexp"));
                    	player.setPrefix(resultSet.getString("prefix"));
                    	player.setVault(resultSet.getInt("vault"));
                        setReturnValue(true);
                    }else setReturnValue(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public void insert(DBPlayer player) {
        execute(new Insert(insertIntoTable() + " (uuid,rank,coin,ofb,rexp,nvote,online,ban,reason,vpn,support,suexp,prefix,vault) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                player.getUUID().toString(),
                player.getRank().name(),
                player.getCoin(),
                player.isOfflineBoot(),
                player.getRankExpire(),
                player.getNextVote(),
                player.isOnline(),
                player.isBan(),
                player.getReason(),
                player.isVpnBypass(),
                player.isSupporter(),
                player.getSupporterExpire(),
                player.getPrefix(),
                player.getVault()
        		) {
            @Override
            public void onInsert(List<Integer> integers) {
                if (!integers.isEmpty())
                    player.setId(integers.get(0));
            }
        });
    }
	
	public DBPlayer getFromID(int id) {
        return (DBPlayer) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    //setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new DBPlayer(resultSet.getInt("id"),
                        		UUID.fromString(resultSet.getString("uuid")),
                        		DBPlayer.Rank.valueOf(resultSet.getString("rank")),
                        		resultSet.getInt("coin"),
                        		resultSet.getBoolean("ofb"),
                        		resultSet.getDate("rexp"),
                        		resultSet.getDate("nvote"),
                        		resultSet.getBoolean("online"),
                        		resultSet.getBoolean("ban"),
                        		resultSet.getString("reason"),
                        		resultSet.getBoolean("vpn"),
                        		resultSet.getBoolean("support"),
                        		resultSet.getDate("suexp"),
                        		resultSet.getString("prefix"),
                        		resultSet.getInt("vault")
                        		));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public DBPlayer get(final UUID uuid) {
        return (DBPlayer) executeQuery(new Query(selectFromTable("*","uuid = ?"),uuid.toString()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    //setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new DBPlayer(resultSet.getInt("id"),
                        		uuid,
                        		DBPlayer.Rank.valueOf(resultSet.getString("rank")),
                        		resultSet.getInt("coin"),
                        		resultSet.getBoolean("ofb"),
                        		resultSet.getDate("rexp"),
                        		resultSet.getDate("nvote"),
                        		resultSet.getBoolean("online"),
                        		resultSet.getBoolean("ban"),
                        		resultSet.getString("reason"),
                        		resultSet.getBoolean("vpn"),
                        		resultSet.getBoolean("support"),
                        		resultSet.getDate("suexp"),
                        		resultSet.getString("prefix"),
                        		resultSet.getInt("vault")
                        		));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public ArrayList<DBPlayer> ofbPlayers() {
		@SuppressWarnings("unchecked")
		ArrayList<DBPlayer> list = (ArrayList<DBPlayer>) executeQuery(new Query(selectFromTable("*","ofb = true")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBPlayer> arr = new ArrayList<DBPlayer>();
                    setReturnValue(arr);
                    while (resultSet.next())
                    {
                    	arr.add(new DBPlayer(resultSet.getInt("id"),
                        		UUID.fromString(resultSet.getString("uuid")),
                        		DBPlayer.Rank.valueOf(resultSet.getString("rank")),
                        		resultSet.getInt("coin"),
                        		true,
                        		resultSet.getDate("rexp"),
                        		resultSet.getDate("nvote"),
                        		resultSet.getBoolean("online"),
                        		resultSet.getBoolean("ban"),
                        		resultSet.getString("reason"),
                        		resultSet.getBoolean("vpn"),
                        		resultSet.getBoolean("support"),
                        		resultSet.getDate("suexp"),
                        		resultSet.getString("prefix"),
                        		resultSet.getInt("vault")
                        		));
                    }
                    setReturnValue(arr);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
		return list;
	}
	
	public int getOnlinePlayers() {
		return(Integer) executeQuery(new Query(selectFromTable("*","online = ?"),true) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	int i = 0;
                	while (resultSet.next()) {i++;}
                	setReturnValue(i);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public void addCoin(UUID uuid,int coin) {
		execute("UPDATE " + getTable() + " SET coin = coin + ? WHERE uuid = ?",coin,uuid.toString());
	}
	
	public void update(DBPlayer player) {
        execute("UPDATE " + getTable() + " SET uuid = ?, rank = ?, coin = ?,ofb = ?,rexp = ?,nvote = ?,online = ?,ban = ?, reason = ?, vpn = ?, support = ?, suexp = ?, prefix = ?,vault = ? WHERE id = ?",
        		player.getUUID().toString(),
        		player.getRank().name(),
        		player.getCoin(),
        		player.isOfflineBoot(),
        		player.getRankExpire(),
        		player.getNextVote(),
        		player.isOnline(),
        		player.isBan(),
        		player.getReason(),
        		player.isVpnBypass(),
        		player.isSupporter(),
        		player.getSupporterExpire(),
        		player.getPrefix(),
        		player.getVault(),
        		player.getId());
    }
}
