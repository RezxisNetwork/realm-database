package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.KeyValue;

public class RezxisKVTable extends MySQLStorage {

	public static RezxisKVTable instnace;

    public RezxisKVTable() {
        super("kvstorage");
        instnace = this;
        prefix = "rezxis_";
        tablename = "kvstorage";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("keyx", "text,");
        map.put("value", "text");
        createTable(map);
    }
    
    public void insert(KeyValue kv) {
    	execute(new Insert(insertIntoTable() + " (id,keyx,value) VALUES (?,?,?)",
                kv.getId(),
                kv.getKey(),
                kv.getValue()) {
            @Override
            public void onInsert(List<Integer> keyxs) {
            	if (!keyxs.isEmpty())
            		kv.setId(keyxs.get(0));
            }
        });
    }
    
    public void delete(KeyValue kv) {
		execute("DELETE FROM "+this.getTable()+" WHERE id=?",kv.getId());
	}
    
    public void update(KeyValue kv) {
        execute("UPDATE " + getTable() + " SET keyx = ?, value = ? WHERE id = ?",
        		kv.getKey(),
        		kv.getValue(),
        		kv.getId());
    }
    
    public void sync(KeyValue kv) {
    	executeQuery(new Query(selectFromTable("keyx,value","id = ?"),kv.getId()) {
            @Override
            protected void onResult(ResultSet resultSet) {
            	setReturnValue(null);
                try {
                    if(resultSet.next()) {
                        kv.setKey(resultSet.getString("keyx"));
                        kv.setValue(resultSet.getString("value"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public KeyValue get(String keyx) {
    	return (KeyValue) executeQuery(new Query(selectFromTable("id,value","keyx = ?"),keyx) {
            @Override
            protected void onResult(ResultSet resultSet) {
            	setReturnValue(null);
                try {
                    if(resultSet.next())
                        setReturnValue(new KeyValue(resultSet.getInt("id"), keyx, resultSet.getString("value")));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public KeyValue get(int id) {
    	return (KeyValue) executeQuery(new Query(selectFromTable("keyx,value","id = ?"),id) {
            @Override
            protected void onResult(ResultSet resultSet) {
            	setReturnValue(null);
                try {
                    if(resultSet.next())
                        setReturnValue(new KeyValue(id, resultSet.getString("keyx"), resultSet.getString("value")));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
