package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.server.DBThirdParty;

public class ThirdPartyTable extends MySQLStorage {

	@Getter
	private static ThirdPartyTable instance;
	
	public ThirdPartyTable() {
		super("thirdparty");
		instance = this;
		prefix = "rezxis_";
		tablename = "thirdparty";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
		map.put("token", "text,");
		map.put("owner", "text,");
		map.put("locked", "boolean,");
		map.put("expire", "datetime,");
		map.put("online", "boolean,");
		map.put("host", "text,");
		map.put("port", "int,");
		map.put("players", "int,");
		map.put("max", "int,");
		map.put("name", "text,");
		map.put("motd", "text,");
		map.put("visible", "boolean,");
		map.put("score", "int,");
		map.put("icon", "text");
		createTable(map);
	}
	
	public boolean load(final DBThirdParty p) {
        return (Boolean) executeQuery(new Query(selectFromTable("*") + " WHERE owner = ?", p.getOwner().toString()) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                    	p.setId(resultSet.getInt("id"));
                    	p.setKey(resultSet.getString("token"));
                    	p.setOwner(UUID.fromString(resultSet.getString("owner")));
                    	p.setLocked(resultSet.getBoolean("locked"));
                    	p.setExpire(resultSet.getTimestamp("expire"));
                    	p.setOnline(resultSet.getBoolean("online"));
                    	p.setHost(resultSet.getString("host"));
                    	p.setPort(resultSet.getInt("port"));
                    	p.setPlayers(resultSet.getInt("players"));
                    	p.setMax(resultSet.getInt("max"));
                    	p.setName(resultSet.getString("name"));
                    	p.setMotd(resultSet.getString("motd"));
                    	p.setVisible(resultSet.getBoolean("visible"));
                    	p.setScore(resultSet.getInt("score"));
                    	p.setIcon(resultSet.getString("icon"));
                        setReturnValue(true);
                    } else setReturnValue(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public void insert(DBThirdParty p) {
        execute(new Insert(insertIntoTable() + " (token,owner,locked,expire,online,host,port,players,max,name,motd,visible,score,icon) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
        		p.getKey(),
        		p.getOwner().toString(),
        		p.isLocked(),
        		p.getExpire(),
        		p.isOnline(),
        		p.getHost(),
        		p.getPort(),
        		p.getPlayers(),
        		p.getMax(),
        		p.getName(),
        		p.getMotd(),
        		p.isVisible(),
        		p.getScore(),
        		p.getIcon()
                ) {
            @Override
            public void onInsert(List<Integer> integers) {
                if (!integers.isEmpty())
                    p.setId(integers.get(0));
            }
        });
    }
	
	public DBThirdParty getByUUID(UUID uuid) {
		return (DBThirdParty) executeQuery(new Query(selectFromTable("*","owner = ?"),uuid.toString()) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(convert(resultSet));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBThirdParty getByID(int id) {
		return (DBThirdParty) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(convert(resultSet));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBThirdParty getByName(String name) {
		return (DBThirdParty) executeQuery(new Query(selectFromTable("*","name = ?"),name) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next()) {
                        setReturnValue(convert(resultSet));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBThirdParty getByKey(String key) {
		return (DBThirdParty) executeQuery(new Query(selectFromTable("*","token = ?"),key) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next()) {
                        setReturnValue(convert(resultSet));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public void update(DBThirdParty p) {
		execute("UPDATE " + getTable() + " SET token = ?, owner = ?, locked = ?,expire = ?,online = ?,host = ?,port = ?,players = ?, max = ?, name = ?, motd = ?, visible = ?, score = ?, icon = ? WHERE id = ?",
				p.getKey(),p.getOwner().toString(),p.isLocked(),p.getExpire(),p.isOnline(),p.getHost(),p.getPort(),p.getPlayers(),p.getMax(),p.getName(),p.getMotd(),p.isVisible(),p.getScore(),p.getIcon(),p.getId()
				);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<DBThirdParty> getOnlineServersVisible() {
		return (ArrayList<DBThirdParty>) executeQuery(new Query(selectFromTable("*","online = 1 AND visible = ?"),true) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    ArrayList<DBThirdParty> servers = new ArrayList<>();
                    while (resultSet.next())
                    {
                    	servers.add(convert(resultSet));
                    }
                    setReturnValue(servers);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<DBThirdParty> getOnlineServers() {
		return (ArrayList<DBThirdParty>) executeQuery(new Query(selectFromTable("*","online = 1")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    ArrayList<DBThirdParty> servers = new ArrayList<>();
                    while (resultSet.next())
                    {
                    	servers.add(convert(resultSet));
                    }
                    setReturnValue(servers);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	private static DBThirdParty convert(ResultSet resultSet) {
		try {
			return new DBThirdParty(
					resultSet.getInt("id"),
					resultSet.getString("token"),
					UUID.fromString(resultSet.getString("owner")),
					resultSet.getBoolean("online"),
					resultSet.getBoolean("locked"),
					resultSet.getTimestamp("expire"),
					resultSet.getString("host"),
					resultSet.getInt("port"),
					resultSet.getInt("players"),
					resultSet.getInt("max"),
					resultSet.getString("motd"),
					resultSet.getString("name"),
					resultSet.getBoolean("visible"),
					resultSet.getInt("score"),
					resultSet.getString("icon")
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
