package net.rezxis.mchosting.databse.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.databse.DBPlugin;
import net.rezxis.mchosting.databse.MySQLStorage;

public class PluginsTable extends MySQLStorage {

	public static PluginsTable instance;
	public static Gson gson = new Gson();
	
	public PluginsTable() {
		super("plugins");
		instance = this;
		prefix = "rezxis_";
		tablename = "plugins";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
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
