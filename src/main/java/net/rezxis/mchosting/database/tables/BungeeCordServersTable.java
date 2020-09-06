package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.server.BungeeCordServer;

public class BungeeCordServersTable extends MySQLStorage {

	public static  BungeeCordServersTable instnace;
    public static Gson gson = new Gson();

    public  BungeeCordServersTable() {
        super("bungeeServers");
        instnace = this;
        prefix = "rezxis_";
        tablename = "bungeeservers";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("name", "text,");
        map.put("motd", "test,");
        map.put("address", "text,");
        map.put("port", ",int,");
        map.put("restricted", "boolean");
        createTable(map);
    }
    
    public void insert(BungeeCordServer bcs) {
    	execute(new Insert(insertIntoTable() + " (name,motd,address,port,restricted) VALUES (?,?,?,?,?)",
                bcs.getName(),
                bcs.getMotd(),
                bcs.getAddress(),
                bcs.getPort(),
                bcs.isRestricted()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		bcs.setId(keys.get(0));
            }
        });
    }
    
    public void delete(BungeeCordServer bcs) {
		execute("DELETE FROM rezxis_bungeeservers WHERE id=?",bcs.getId());
	}
    
    public void update(BungeeCordServer bcs) {
        execute("UPDATE " + getTable() + " SET name = ?,motd = ?, address = ?, port = ?, restricted = ? WHERE id = ?",
        		bcs.getName(),
        		bcs.getMotd(),
        		bcs.getAddress(),
        		bcs.getPort(),
        		bcs.isRestricted(),
        		bcs.getId());
    }
    
    public boolean isExists(String name) {
    	return (boolean) executeQuery(new Query(selectFromTable("*","name = ?"),name) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                	setReturnValue(false);
                    if (resultSet.next())
                    {
                    	setReturnValue(true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public BungeeCordServer getServerFromID(int id) {
    	return (BungeeCordServer) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
			@Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(parse(resultSet));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
	@SuppressWarnings("unchecked")
	public ArrayList<BungeeCordServer> getServersByName(String name) {
    	return (ArrayList<BungeeCordServer>) executeQuery(new Query(selectFromTable("*","name = ?"), name) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<BungeeCordServer> arr = new ArrayList<BungeeCordServer>();
                    setReturnValue(arr);
                    while (resultSet.next())
                    {
                    	arr.add(parse(resultSet));
                    }
                    setReturnValue(arr);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<BungeeCordServer> getAll() {
    	return (ArrayList<BungeeCordServer>) executeQuery(new Query(selectFromTable("*")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<BungeeCordServer> arr = new ArrayList<>();
                    setReturnValue(arr);
                    while (resultSet.next())
                    {
                    	arr.add(parse(resultSet));
                    }
                    setReturnValue(arr);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private BungeeCordServer parse(ResultSet resultSet) throws SQLException {
		return new BungeeCordServer(resultSet.getInt("id"),
				resultSet.getString("name"),
				resultSet.getString("motd"),
				resultSet.getString("address"),
				resultSet.getInt("port"),
				resultSet.getBoolean("restricted"));
    }
}
