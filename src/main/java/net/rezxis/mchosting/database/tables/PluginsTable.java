package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.server.DBPlugin;

public class PluginsTable extends MySQLStorage {

	public static PluginsTable instance;
	public static Gson gson = new Gson();
	
	public PluginsTable() {
		super("plugins");
		instance = this;
		prefix = "rezxis_";
		tablename = "plugins";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
		map.put("name", "text,");
		map.put("jarname", "text,");
		map.put("version", "text,");
		map.put("depends", "text");
		createTable(map);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,DBPlugin> getPlugins() {
		return (HashMap<String,DBPlugin>) executeQuery(new Query(selectFromTable("*")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	HashMap<String,DBPlugin> plugins = new HashMap<>();
                    while (resultSet.next()) {
                    	plugins.put(resultSet.getString("name"),
                    			new DBPlugin(
                    					resultSet.getInt("id"),
                    					resultSet.getString("name"),
                    					resultSet.getString("jarname"),
                    					resultSet.getString("version"),
                    					gson.fromJson(resultSet.getString("depends"), ArrayList.class)));
                    }
                    setReturnValue(plugins);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getNames() {
		return (ArrayList<String>) executeQuery(new Query("SELECT DISTINCT name FROM `rezxis_plugins`") {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<String> plugins = new ArrayList<>();
                    while (resultSet.next()) {
                    	plugins.add(resultSet.getString("name"));
                    }
                    setReturnValue(plugins);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBPlugin getPluginById(int id) {
		return (DBPlugin) executeQuery(new Query(selectFromTable("*", "id = ?"), id) {
            @SuppressWarnings("unchecked")
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                	DBPlugin plugin = null;
                    if (resultSet.next()) {
                    	plugin = new DBPlugin(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("jarname"),resultSet.getString("version"),gson.fromJson(resultSet.getString("depends"), ArrayList.class));
                    }
                    setReturnValue(plugin);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<DBPlugin> get(String name) {
		return (ArrayList<DBPlugin> ) executeQuery(new Query(selectFromTable("*", "name = ?"), name) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBPlugin>  plugin = new ArrayList<DBPlugin>();
                    while (resultSet.next()) {
                    	plugin.add(new DBPlugin(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("jarname"),resultSet.getString("version"),gson.fromJson(resultSet.getString("depends"), ArrayList.class)));
                    }
                    setReturnValue(plugin);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public void insert(String name, String jarName, String version, ArrayList<String> depends) {
        execute(new Insert(insertIntoTable() + " (name,jarname,version,depends) VALUES (?,?,?,?)",
                name,
                jarName,
                version,
                gson.toJson(depends)) {
            @Override
            public void onInsert(List<Integer> integers) {
            }
        });
    }
}
