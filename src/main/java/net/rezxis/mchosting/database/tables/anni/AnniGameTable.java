package net.rezxis.mchosting.database.tables.anni;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.anni.StatusSignInfo;

public class AnniGameTable extends MySQLStorage {

	/*
	 *
	 varchar(20) serverName;
	 int maxPlayers;
	 int onlinePlayers;
	 boolean joinable;
	 boolean online;//?
	 varchar(40) ip:port;
	 text icon(json);
	 text line1;
	 text line2;
	 text line3;
	 text line4;
	 DATETIME lastUpdated;
	 */

	public AnniGameTable() {
		super("servers");
        prefix = "shotbow_";
        tablename = "servers";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("serverName","VARCHAR(20) PRIMARY KEY NOT NULL,");
        map.put("maxPlayers","int,");
        map.put("onlinePlayers","int,");
        map.put("joinable","boolean,");
        map.put("online","boolean,");
        map.put("ip","varchar(40),");
        map.put("icon","text,");
        map.put("line1","text,");
        map.put("line2","text,");
        map.put("line3","text,");
        map.put("line4","text,");
        map.put("lastUpdated","DATETIME");

        createTable(map);
    }


		@SuppressWarnings("unchecked")
		public HashMap<String, StatusSignInfo> get() {
			return (HashMap<String, StatusSignInfo>) executeQuery(new Query(selectFromTable("*")) {
	            @Override
	            protected void onResult(ResultSet resultSet) {
	                try {
	                	HashMap<String, StatusSignInfo> map=new HashMap<String, StatusSignInfo>();
	                    setReturnValue(null);
	                    	while (resultSet.next()) {
	                    		map.put(resultSet.getString("serverName"),
	                    				new StatusSignInfo(
	                    						resultSet.getString("serverName"),
	                    						resultSet.getInt("maxPlayers"),
	                    						resultSet.getInt("onlinePlayers"),
	                    						resultSet.getBoolean("joinable"),
	                    						resultSet.getBoolean("online"),
	                    						resultSet.getString("ip"),
	                    						resultSet.getString("icon"),
	                    						resultSet.getString("line1"),
	                    						resultSet.getString("line2"),
	                    						resultSet.getString("line3"),
	                    						resultSet.getString("line4"),
	                    						resultSet.getTimestamp("lastUpdated"))
	                    				);
	                    	}
	                    	setReturnValue(map);

	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
		}




}
