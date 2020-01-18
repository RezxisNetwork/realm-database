package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.player.DBUUID;
import net.rezxis.mchosting.database.object.server.DBThirdParty;

public class UuidTable extends MySQLStorage {
    public static UuidTable instnace;
    public static Gson gson = new Gson();

    public UuidTable() {
        super("uuid");
        instnace = this;
        prefix = "rezxis_";
        tablename = "uuid";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("name", "text,");
        map.put("uuid", "text");
        createTable(map);
    }
    
    public void update(DBUUID db) {
		execute("UPDATE " + getTable() + " SET name = ?, uuid = ? WHERE id = ?",
				db.getName(),db.getUuid().toString(),db.getId()
				);
	}
    
    public void insert(DBUUID db) {
    	execute(new Insert(insertIntoTable() + " (name,uuid) VALUES (?,?)",
                db.getName(),
                db.getUuid().toString()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty()) {
            		db.setId(keys.get(0));
            	}
            }
        });
    }
    
    public DBUUID get(UUID uuid) {
    	return (DBUUID) executeQuery(new Query(selectFromTable("*","uuid = ?"),uuid.toString()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBUUID(resultSet.getInt("id"),resultSet.getString("name"),uuid));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public DBUUID get(String name) {
    	return (DBUUID) executeQuery(new Query(selectFromTable("*","name = ?"),name) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBUUID(resultSet.getInt("id"),name, UUID.fromString(resultSet.getString("uuid"))));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}