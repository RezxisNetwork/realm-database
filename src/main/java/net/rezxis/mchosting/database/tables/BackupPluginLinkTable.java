package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.internal.DBBackup;
import net.rezxis.mchosting.database.object.server.DBBackupPluginLink;

public class BackupPluginLinkTable extends MySQLStorage {

	public static BackupPluginLinkTable instnace;
    public static Gson gson = new Gson();

    public BackupPluginLinkTable() {
        super("bplink");
        instnace = this;
        prefix = "rezxis_";
        tablename = "link_backup_plugin";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("backup", "int,");
        map.put("plugin", "int");
        createTable(map);
    }
    
    public void insert(DBBackupPluginLink link) {
    	execute(new Insert(insertIntoTable() + " (backup,plugin) VALUES (?,?)",
                link.getBackup(),
                link.getPlugin()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		link.setId(keys.get(0));
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<DBBackupPluginLink> getAllByBackup(int backup) {
    	return (ArrayList<DBBackupPluginLink>) executeQuery(new Query(selectFromTable("*","backup = ?"), backup) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBBackupPluginLink> list = new ArrayList<>();
                    while (resultSet.next())
                        list.add(convert(resultSet));
                    setReturnValue(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void delete(DBBackupPluginLink link) {
		execute("DELETE FROM rezxis_link_backup_plugin WHERE id=?",link.getId());
	}
    
    public void update(DBBackupPluginLink link) {
    	execute("UPDATE " + getTable() + " SET backup = ?, plugin = ? WHERE id = ?",
    			link.getBackup(),
    			link.getPlugin(),
    			link.getId()
        		);
    }
    
    public static DBBackupPluginLink convert(ResultSet rs) {
    	try {
			return new DBBackupPluginLink(rs.getInt("id"), rs.getInt("server"), rs.getInt("plugin"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
