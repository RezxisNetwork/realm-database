package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.internal.DBBackup;
import net.rezxis.mchosting.database.object.server.DBShopItem;

public class ShopItemTable extends MySQLStorage {

	public static ShopItemTable instnace;
    public static Gson gson = new Gson();

    public ShopItemTable() {
        super("shopitems");
        instnace = this;
        prefix = "rezxis_";
        tablename = "shopitems";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("server", "int,");
        map.put("name", "text,");
        map.put("itemType", "text,");
        map.put("cmd", "text,");
        map.put("price", "int,");
        map.put("earned", "int");
        createTable(map);
    }
    
    public void insert(DBShopItem item) {
    	execute(new Insert(insertIntoTable() + " (server,name,itemType,cmd,price,earned) VALUES (?,?,?,?,?,?)",
                item.getServer(),item.getName(),item.getItemType(),
                item.getCmd(),item.getPrice(),item.getEarned()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		item.setId(keys.get(0));
            }
        });
    }
    
    public void delete(DBShopItem item) {
		execute("DELETE FROM " + getTable() + " WHERE id = ?",item.getId());
	}
    
    public void update(DBShopItem obj) {
        execute("UPDATE " + getTable() + " SET server = ?, name = ?, itemType = ?, cmd = ?, price = ?, earned = ? WHERE id = ?",
        		obj.getServer(),obj.getName(),obj.getItemType(),obj.getCmd(),
        		obj.getPrice(),obj.getEarned(),obj.getId());
    }
    
    public DBShopItem convert(ResultSet rs) throws SQLException {
    	return new DBShopItem(rs.getInt("id"),rs.getInt("server"),rs.getString("name"),rs.getString("itemType")
    			,rs.getString("cmd"),rs.getLong("price"),rs.getLong("earned"));
    }
    
    public DBShopItem getShopItemFromID(int id) {
    	return (DBShopItem) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
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
	public ArrayList<DBShopItem> getShopItems(int server) {
    	return (ArrayList<DBShopItem>) executeQuery(new Query(selectFromTable("*","server = ?"),server) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBShopItem> arr = new ArrayList<>();
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
