package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.player.DBIP;

public class IPTable extends MySQLStorage {

	public static IPTable instnace;
    public static Gson gson = new Gson();

    public IPTable() {
        super("ips");
        instnace = this;
        prefix = "rezxis_";
        tablename = "ips";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("ip", "text,");
        map.put("banned", "boolean,");
        map.put("reason", "text,");
        map.put("lastUpdate", "datetime");
        createTable(map);
    }
    
    public void insert(DBIP ip) {
    	execute(new Insert(insertIntoTable() + " (ip,banned,reason,lastUpdate) VALUES (?,?,?,now())",
                ip.getIp(),
                ip.isBanned(),
                ip.getReason()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		ip.setId(keys.get(0));
            }
        });
    }
    
    public void delete(DBIP ip) {
		execute("DELETE FROM rezxis_ips WHERE id=?",ip.getId());
	}
    
    public DBIP get(String ip) {
    	return (DBIP) executeQuery(new Query(selectFromTable("*","ip = ?"),ip) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	setReturnValue(null);
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBIP(resultSet.getInt("id"),
                    			ip,
                    			resultSet.getBoolean("banned"),
                    			resultSet.getString("reason"),
                    			resultSet.getDate("lastUpdate")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public DBIP getFromID(int id) {
    	return (DBIP) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBIP(id,
                    			resultSet.getString("ip"),
                    			resultSet.getBoolean("banned"),
                    			resultSet.getString("reason"),
                    			resultSet.getDate("lastUpdate")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void update(DBIP ip) {
        execute("UPDATE " + getTable() + " SET ip = ?, banned = ?, reason = ?, lastUpdate = now() WHERE id = ?",
        		ip.getIp(),
        		ip.isBanned(),
        		ip.getReason(),
        		ip.getId());
    }
}