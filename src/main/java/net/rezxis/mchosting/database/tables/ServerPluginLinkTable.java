package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.server.DBServerPluginLink;

public class ServerPluginLinkTable extends MySQLStorage {

	public static ServerPluginLinkTable instnace;
    public static Gson gson = new Gson();

    public ServerPluginLinkTable() {
        super("splink");
        instnace = this;
        prefix = "rezxis_";
        tablename = "link_server_plugin";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("server", "int,");
        map.put("plugin", "int,");
        map.put("enabled", "boolean,");
        map.put("lastEnabled", "boolean");
        createTable(map);
    }
    
    public void insert(DBServerPluginLink link) {
    	execute(new Insert(insertIntoTable() + " (server,plugin,enabled,lastEnabled) VALUES (?,?,?,?)",
                link.getServer().getId(),
                link.getPlugin().getId()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		link.setId(keys.get(0));
            }
        });
    }
    
    public DBServerPluginLink getLink(int server, int plugin) {
    	return (DBServerPluginLink) executeQuery(new Query(selectFromTable("*","server = ?, plugin = ?"), server, plugin) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                        setReturnValue(convert(resultSet));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<DBServerPluginLink> getAllByServer(int server) {
    	return (ArrayList<DBServerPluginLink>) executeQuery(new Query(selectFromTable("*","server = ?"), server) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBServerPluginLink> list = new ArrayList<>();
                    if(resultSet.next())
                        list.add(convert(resultSet));
                    setReturnValue(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void update(DBServerPluginLink link) {
    	execute("UPDATE " + getTable() + " SET server = ?, plugin = ?, enabled = ?, lastEnabled = ? WHERE id = ?",
    			link.getServer(),
    			link.getPlugin(),
    			link.isEnabled(),
    			link.isLastEnabled(),
    			link.getId()
        		);
    }
    
    public static DBServerPluginLink convert(ResultSet rs) {
    	try {
			return new DBServerPluginLink(rs.getInt("id"), rs.getInt("server"), rs.getInt("plugin"), rs.getBoolean("enabled"), rs.getBoolean("lastEnabled"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
