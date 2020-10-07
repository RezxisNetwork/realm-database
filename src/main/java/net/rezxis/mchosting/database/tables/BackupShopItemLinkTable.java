package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.server.DBBackupShopItemLink;

public class BackupShopItemLinkTable extends MySQLStorage {

	public static BackupShopItemLinkTable instnace;
    public static Gson gson = new Gson();

    public BackupShopItemLinkTable() {
        super("backupshop");
        instnace = this;
        prefix = "rezxis_";
        tablename = "link_backup_shopitem";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("backup", "int,");
        map.put("name", "text,");
        map.put("itemType", "text,");
        map.put("cmd", "text,");
        map.put("price", "int,");
        map.put("earned", "int");
        createTable(map);
    }
    
    public void insert(DBBackupShopItemLink item) {
    	execute(new Insert(insertIntoTable() + " (backup,name,itemType,cmd,price,earned) VALUES (?,?,?,?,?,?)",
                item.getBackup(),item.getName(),item.getItemType(),
                item.getCmd(),item.getPrice(),item.getEarned()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		item.setId(keys.get(0));
            }
        });
    }
    
    public void delete(DBBackupShopItemLink item) {
		execute("DELETE FROM " + getTable() + " WHERE id = ?",item.getId());
	}
    
    public void update(DBBackupShopItemLink obj) {
        execute("UPDATE " + getTable() + " SET backup = ?, name = ?, itemType = ?, cmd = ?, price = ?, earned = ? WHERE id = ?",
        		obj.getBackup(),obj.getName(),obj.getItemType(),obj.getCmd(),
        		obj.getPrice(),obj.getEarned(),obj.getId());
    }
    
    public DBBackupShopItemLink convert(ResultSet rs) throws SQLException {
    	return new DBBackupShopItemLink(rs.getInt("id"),rs.getInt("backup"),rs.getString("name"),rs.getString("itemType")
    			,rs.getString("cmd"),rs.getLong("price"),rs.getLong("earned"));
    }
    
    public DBBackupShopItemLink getShopItemFromID(int id) {
    	return (DBBackupShopItemLink) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
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
	public ArrayList<DBBackupShopItemLink> getShopItems(int backup) {
    	return (ArrayList<DBBackupShopItemLink>) executeQuery(new Query(selectFromTable("*","backup = ?"),backup) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBBackupShopItemLink> arr = new ArrayList<>();
                    setReturnValue(arr);
                    while (resultSet.next())
                    {
                    	arr.add(convert(resultSet));
                    }
                    setReturnValue(arr);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
