package net.rezxis.mchosting.databse.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.databse.DBShop;
import net.rezxis.mchosting.databse.MySQLStorage;
import net.rezxis.mchosting.databse.ServerStatus;

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
		map.put("shop", "text");
		createTable(map);
	}
	
	public void insert(DBServer server) {
        execute(new Insert(insertIntoTable() + " (displayName,owner,plugins,players,port,status,world,host,motd,cmd,visible,icon,shop) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                server.getDisplayName(),
                server.getOwner().toString(),
                gson.toJson(server.getPlugins()),
                server.getPlayers(),
                server.getPort(),
                server.getStatus().name(),
                server.getWorld(),
                server.getHost(),
                server.getMotd(),
                server.getCmd(),
                server.getVisible(),
                server.getIcon(),
                gson.toJson(server.getShop())) {
            @Override
            public void onInsert(List<Integer> integers) {
                if (!integers.isEmpty())
                    server.setID(integers.get(0));
            }
        });
    }
	
	public void delete(DBServer server) {
		execute("DELETE FROM rezxis_servers WHERE id=?",server.getID());
	}
	
	public boolean load(final DBServer server) {
        return (Boolean) executeQuery(new Query(selectFromTable("*") + " WHERE owner = ?", server.getOwner().toString()) {
            @SuppressWarnings("unchecked")
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                        server.setID(resultSet.getInt("id"));
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
                        setReturnValue(true);
                    }else setReturnValue(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	public DBServer getByPort(int port) {
		return (DBServer) executeQuery(new Query(selectFromTable("*","port = ?"),port) {
			@SuppressWarnings("unchecked")
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new DBServer(resultSet.getInt("id"),
                        		resultSet.getString("displayName"),
                        		UUID.fromString(resultSet.getString("owner")),port,
                        		gson.fromJson(resultSet.getString("plugins"),ArrayList.class),
                        		resultSet.getInt("players"),
                        		ServerStatus.valueOf(resultSet.getString("status")),
                        		resultSet.getString("world"),
                        		resultSet.getInt("host"),
                        		resultSet.getString("motd"),
                        		resultSet.getBoolean("cmd"),
                        		resultSet.getBoolean("visible"),
                        		resultSet.getString("icon"),
                        		gson.fromJson(resultSet.getString("shop"), DBShop.class)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBServer getByID(int id) {
		return (DBServer) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
			@SuppressWarnings("unchecked")
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new DBServer(resultSet.getInt("id"),
                        		resultSet.getString("displayName"),
                        		UUID.fromString(resultSet.getString("owner")),resultSet.getInt("port"),
                        		gson.fromJson(resultSet.getString("plugins"),ArrayList.class),
                        		resultSet.getInt("players"),
                        		ServerStatus.valueOf(resultSet.getString("status")),
                        		resultSet.getString("world"),
                        		resultSet.getInt("host"),
                        		resultSet.getString("motd"),
                        		resultSet.getBoolean("cmd"),
                        		resultSet.getBoolean("visible"),
                        		resultSet.getString("icon"),
                        		gson.fromJson(resultSet.getString("shop"), DBShop.class)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBServer get(final UUID uuid) {
        return (DBServer) executeQuery(new Query(selectFromTable("*","owner = ?"),uuid.toString()) {
        	@SuppressWarnings("unchecked")
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new DBServer(resultSet.getInt("id"),
                        		resultSet.getString("displayName"),
                        		uuid,resultSet.getInt("port"),
                        		gson.fromJson(resultSet.getString("plugins"),ArrayList.class),
                        		resultSet.getInt("players"),
                        		ServerStatus.valueOf(resultSet.getString("status")),
                        		resultSet.getString("world"),
                        		resultSet.getInt("host"),
                        		resultSet.getString("motd"),
                        		resultSet.getBoolean("cmd"),
                        		resultSet.getBoolean("visible"),
                        		resultSet.getString("icon"),
                        		gson.fromJson(resultSet.getString("shop"), DBShop.class)));
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
			@SuppressWarnings("unchecked")
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new DBServer(resultSet.getInt("id"),
                        		name,
                        		UUID.fromString(resultSet.getString("owner")),resultSet.getInt("port"),
                        		gson.fromJson(resultSet.getString("plugins"),ArrayList.class),
                        		resultSet.getInt("players"),
                        		ServerStatus.valueOf(resultSet.getString("status")),
                        		resultSet.getString("world"),
                        		resultSet.getInt("host"),
                        		resultSet.getString("motd"),
                        		resultSet.getBoolean("cmd"),
                        		resultSet.getBoolean("visible"),
                        		resultSet.getString("icon"),
                        		gson.fromJson(resultSet.getString("shop"), DBShop.class)));
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
                    	servers.add(new DBServer(resultSet.getInt("id"),
                    			resultSet.getString("displayName"),
                    			UUID.fromString(resultSet.getString("owner")),
                    			resultSet.getInt("port"),
                    			gson.fromJson(resultSet.getString("plugins"), ArrayList.class),
                    			resultSet.getInt("players"),
                    			ServerStatus.valueOf(resultSet.getString("status")),
                    			resultSet.getString("world"),
                    			resultSet.getInt("host"),
                    			resultSet.getString("motd"),
                    			resultSet.getBoolean("cmd"),
                    			resultSet.getBoolean("visible"),
                    			resultSet.getString("icon"),
                    			gson.fromJson(resultSet.getString("shop"), DBShop.class)));
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
                    	servers.add(
                    			new DBServer(resultSet.getInt("id"),
                    			resultSet.getString("displayName"),
                    			UUID.fromString(resultSet.getString("owner")),
                    			resultSet.getInt("port"),
                    			gson.fromJson(resultSet.getString("plugins"), ArrayList.class),
                    			resultSet.getInt("players"),
                    			ServerStatus.RUNNING,
                    			resultSet.getString("world"),
                    			resultSet.getInt("host"),
                    			resultSet.getString("motd"),
                    			resultSet.getBoolean("cmd"),
                    			resultSet.getBoolean("visible"),
                    			resultSet.getString("icon"),
                    			gson.fromJson(resultSet.getString("shop"), DBShop.class)));
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
                    	servers.add(
                    			new DBServer(resultSet.getInt("id"),
                    			resultSet.getString("displayName"),
                    			UUID.fromString(resultSet.getString("owner")),
                    			resultSet.getInt("port"),
                    			gson.fromJson(resultSet.getString("plugins"), ArrayList.class),
                    			resultSet.getInt("players"),
                    			ServerStatus.RUNNING,
                    			resultSet.getString("world"),
                    			resultSet.getInt("host"),
                    			resultSet.getString("motd"),
                    			resultSet.getBoolean("cmd"),
                    			resultSet.getBoolean("visible"),
                    			resultSet.getString("icon"),
                    			gson.fromJson(resultSet.getString("shop"), DBShop.class)));
                    }
                    setReturnValue(servers);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public void update(DBServer server) {
        execute("UPDATE " + getTable() + " SET displayName = ?,players = ?,port = ?,owner = ?,plugins = ?,status = ?,world = ?,host = ?,motd = ?,cmd = ?,visible = ?,icon = ?,shop = ? WHERE id = ?",
        		server.getDisplayName(),server.getPlayers(), server.getPort(), server.getOwner().toString(),
        		gson.toJson(server.getPlugins()),server.getStatus().name(), server.getWorld(), server.getHost(), 
        		server.getMotd(), server.getCmd(), server.getVisible(), server.getIcon(), gson.toJson(server.getShop()), server.getID());
    }
}