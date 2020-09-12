package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.DBServer.GameType;
import net.rezxis.mchosting.database.object.server.DBShop;
import net.rezxis.mchosting.database.object.server.ServerStatus;

public class ServersTable extends MySQLStorage {

	public static ServersTable instance;
	public static Gson gson = new Gson();
	
	public ServersTable() {
		super("server");
		instance = this;
		prefix = "rezxis_";
		tablename = "servers";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
		map.put("displayName", "text,");
		map.put("owner", "text,");
		map.put("plugins", "text,");
		map.put("players", "int,");
		map.put("port", "int,");
		map.put("status", "text,");
		map.put("world", "text,");
		map.put("host", "int,");
		map.put("motd", "text,");
		map.put("cmd","boolean,");
		map.put("visible", "boolean,");
		map.put("icon", "text,");
		map.put("shop", "text,");
		map.put("vote", "int,");
		map.put("type", "text,");
		map.put("voteCmd", "text,");
		map.put("resource", "text,");
		map.put("ip", "text,");
		map.put("direct", "text");
		createTable(map);
	}
	
	public void insert(DBServer server) {
        execute(new Insert(insertIntoTable() + " (displayName,owner,plugins,players,port,status,world,host,motd,cmd,visible,icon,shop,vote,type,voteCmd,resource,ip,direct) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                server.getDisplayName(),
                server.getOwner().toString(),
                gson.toJson(server.getPlugins()),
                server.getPlayers(),
                server.getPort(),
                server.getStatus().name(),
                server.getWorld(),
                server.getHost(),
                server.getMotd(),
                server.isCmd(),
                server.isVisible(),
                server.getIcon(),
                gson.toJson(server.getShop()),
                server.getVote(),
                server.getType().name(),
                server.getVoteCmd(),
                server.getResource(),
                server.getIp(),
                server.getDirect()) {
            @Override
            public void onInsert(List<Integer> integers) {
                if (!integers.isEmpty())
                    server.setId(integers.get(0));
            }
        });
    }
	
	public void delete(DBServer server) {
		execute("DELETE FROM rezxis_servers WHERE id=?",server.getId());
	}
	
	public boolean load(final DBServer server) {
        return (Boolean) executeQuery(new Query(selectFromTable("*") + " WHERE owner = ?", server.getOwner().toString()) {
            @SuppressWarnings("unchecked")
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                        server.setId(resultSet.getInt("id"));
                        server.setDisplayName(resultSet.getString("displayName"));
                        server.setPort(resultSet.getInt("port"));
                        server.setPlayers(resultSet.getInt("players"));
                        server.setPlugins(gson.fromJson(resultSet.getString("plugins"), ArrayList.class));
                        server.setStatus(ServerStatus.valueOf(resultSet.getString("status")));
                        server.setWorld(resultSet.getString("world"));
                        server.setHost(resultSet.getInt("host"));
                        server.setMotd(resultSet.getString("motd"));
                        server.setCmd(resultSet.getBoolean("cmd"));
                        server.setVisible(resultSet.getBoolean("visible"));
                        server.setIcon(resultSet.getString("icon"));
                        server.setShop(gson.fromJson(resultSet.getString("shop"), DBShop.class));
                        server.setVote(resultSet.getInt("vote"));
                        server.setType(GameType.valueOf(resultSet.getString("type")));
                        server.setVoteCmd(resultSet.getString("voteCmd"));
                        server.setResource(resultSet.getString("resource"));
                        server.setIp(resultSet.getString("ip"));
                        server.setDirect(resultSet.getString("direct"));
                        setReturnValue(true);
                    } else setReturnValue(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public boolean existsWithName(String name) {
        return (Boolean) executeQuery(new Query(selectFromTable("*") + " WHERE displayName = ?", name) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                        setReturnValue(true);
                    } else {
                    	setReturnValue(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	@SuppressWarnings("unchecked")
	private static DBServer convert(ResultSet resultSet) {
		try {
			return new DBServer(resultSet.getInt("id"),
					resultSet.getString("displayName"),
					UUID.fromString(resultSet.getString("owner")),
					resultSet.getInt("port"),
					resultSet.getString("ip"),
					gson.fromJson(resultSet.getString("plugins"),ArrayList.class),
					resultSet.getInt("players"),
					ServerStatus.valueOf(resultSet.getString("status")),
					resultSet.getString("world"),
					resultSet.getInt("host"),
					resultSet.getString("motd"),
					resultSet.getBoolean("cmd"),
					resultSet.getBoolean("visible"),
					resultSet.getString("icon"),
					gson.fromJson(resultSet.getString("shop"), DBShop.class),
					resultSet.getInt("vote"),
					GameType.valueOf(resultSet.getString("type")),
					resultSet.getString("voteCmd"),
					resultSet.getString("resource"),
					resultSet.getString("direct"));
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public DBServer getByPort(int port) {
		return (DBServer) executeQuery(new Query(selectFromTable("*","port = ?"),port) {
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
	
	public DBServer getByID(int id) {
		return (DBServer) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
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
	
	public DBServer get(final UUID uuid) {
        return (DBServer) executeQuery(new Query(selectFromTable("*","owner = ?"),uuid.toString()) {
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
	
	public int getOnlinePlayers() {
		return(Integer) executeQuery(new Query(selectFromTable("SUM(players)","status = ?"),ServerStatus.RUNNING.name()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	setReturnValue(0);
                    if(resultSet.next())
                    {
                    	setReturnValue(resultSet.getInt("SUM(players)"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBServer getServerByName(String name) {
		return (DBServer) executeQuery(new Query(selectFromTable("*","displayname= ?"), name) {
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
	
	public DBServer getServerByDirect(String name) {
		return (DBServer) executeQuery(new Query(selectFromTable("*","direct= ?"), name) {
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
	
	@SuppressWarnings("unchecked")
	public ArrayList<DBServer> getAll() {
		return (ArrayList<DBServer>) executeQuery(new Query(selectFromTable("*")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    ArrayList<DBServer> servers = new ArrayList<>();
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
	public ArrayList<DBServer> getOnlineServersVisible() {
		return (ArrayList<DBServer>) executeQuery(new Query(selectFromTable("*","status = ? AND visible = true"), ServerStatus.RUNNING.name()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    ArrayList<DBServer> servers = new ArrayList<>();
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
	public ArrayList<DBServer> getOnlineServers() {
		return (ArrayList<DBServer>) executeQuery(new Query(selectFromTable("*","status = ?"), ServerStatus.RUNNING.name()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    ArrayList<DBServer> servers = new ArrayList<>();
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
	
	public void update(DBServer server) {
        execute("UPDATE " + getTable() + " SET displayName = ?,players = ?,port = ?,ip = ?,owner = ?,plugins = ?,status = ?,world = ?,host = ?,motd = ?,cmd = ?,visible = ?,icon = ?,shop = ?,vote = ?,type = ?, voteCmd = ?, resource = ?, direct = ? WHERE id = ?",
        		server.getDisplayName(),server.getPlayers(), server.getPort(), server.getIp(), server.getOwner().toString(),
        		gson.toJson(server.getPlugins()),server.getStatus().name(), server.getWorld(), server.getHost(), 
        		server.getMotd(), server.isCmd(), server.isVisible(), server.getIcon(), gson.toJson(server.getShop()),
        		server.getVote(),server.getType().name(),server.getVoteCmd(), server.getResource(),
        		server.getDirect(), server.getId());
    }
}