package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.internal.DBBackup;

public class BackupsTable extends MySQLStorage {

	public static BackupsTable instnace;
    public static Gson gson = new Gson();

    public BackupsTable() {
        super("backups");
        instnace = this;
        prefix = "rezxis_";
        tablename = "backups";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("owner", "text,");
        map.put("name", "text,");
        map.put("creation", "datetime,");
        map.put("shop", "text");
        createTable(map);
    }
    
    public void insert(DBBackup back) {
    	execute(new Insert(insertIntoTable() + " (owner,name,creation,shop) VALUES (?,?,?,?)",
                back.getOwner(),
                back.getName(),
                back.getCreation(),
                back.getShop()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		back.setId(keys.get(0));
            }
        });
    }
    
    public void delete(DBBackup bp) {
		execute("DELETE FROM rezxis_backups WHERE id=?",bp.getId());
	}
    
    public void update(DBBackup obj) {
        execute("UPDATE " + getTable() + " SET owner = ?, name = ?, creation = ?,shop = ? WHERE id = ?",
        		obj.getOwner(),
        		obj.getName(),
        		obj.getCreation(),
        		obj.getShop(),
        		obj.getId());
    }
    
    public DBBackup getBackupFromID(int id) {
    	return (DBBackup) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
            @SuppressWarnings("unchecked")
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBBackup(resultSet.getInt("id"),
                    			resultSet.getString("owner"),
                    			resultSet.getString("name"),
                    			resultSet.getTimestamp("creation"),
                    			resultSet.getString("shop")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<DBBackup> getBackups(String owner) {
    	return (ArrayList<DBBackup>) executeQuery(new Query(selectFromTable("*","owner = ?"),owner) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBBackup> arr = new ArrayList<DBBackup>();
                    setReturnValue(arr);
                    while (resultSet.next())
                    {
                    	arr.add(new DBBackup(resultSet.getInt("id"),
                    			owner,
                    			resultSet.getString("name"),
                    			resultSet.getTimestamp("creation"),
                    			resultSet.getString("shop")));
                    }
                    setReturnValue(arr);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<DBBackup> getAll() {
    	return (ArrayList<DBBackup>) executeQuery(new Query(selectFromTable("*")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBBackup> arr = new ArrayList<DBBackup>();
                    setReturnValue(arr);
                    while (resultSet.next())
                    {
                    	arr.add(new DBBackup(resultSet.getInt("id"),
                    			resultSet.getString("owner"),
                    			resultSet.getString("name"),
                    			resultSet.getTimestamp("creation"),
                    			resultSet.getString("shop")));
                    }
                    setReturnValue(arr);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
