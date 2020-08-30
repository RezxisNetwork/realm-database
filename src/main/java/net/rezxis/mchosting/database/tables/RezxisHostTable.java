package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.HostName;

public class RezxisHostTable extends MySQLStorage {

	public static RezxisHostTable instnace;

    public RezxisHostTable() {
        super("hostname");
        instnace = this;
        prefix = "rezxis_";
        tablename = "hostname";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("host", "text,");
        map.put("dest", "text");
        createTable(map);
    }
    
    public void insert(HostName hn) {
    	execute(new Insert(insertIntoTable() + " (id,host.dest) VALUES (?,?,?)",
                hn.getId(),
                hn.getHost(),
                hn.getDest()) {
            @Override
            public void onInsert(List<Integer> hosts) {
            	if (!hosts.isEmpty())
            		hn.setId(hosts.get(0));
            }
        });
    }
    
    public void delete(HostName hn) {
		execute("DELETE FROM "+this.getTable()+" WHERE id=?",hn.getId());
	}
    
    public void update(HostName hn) {
        execute("UPDATE " + getTable() + " SET host = ?, dest = ? WHERE id = ?",
        		hn.getHost(),
        		hn.getDest(),
        		hn.getId());
    }
    
    public void sync(HostName hn) {
    	executeQuery(new Query(selectFromTable("host,dest","id = ?"),hn.getId()) {
            @Override
            protected void onResult(ResultSet resultSet) {
            	setReturnValue(null);
                try {
                    if(resultSet.next()) {
                        hn.setHost(resultSet.getString("host"));
                        hn.setDest(resultSet.getString("dest"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public HostName get(String host) {
    	return (HostName) executeQuery(new Query(selectFromTable("id,dest","host = ?"),host) {
            @Override
            protected void onResult(ResultSet resultSet) {
            	setReturnValue(null);
                try {
                    if(resultSet.next())
                        setReturnValue(new HostName(resultSet.getInt("id"), host, resultSet.getString("dest")));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public HostName get(int id) {
    	return (HostName) executeQuery(new Query(selectFromTable("host,dest","id = ?"),id) {
            @Override
            protected void onResult(ResultSet resultSet) {
            	setReturnValue(null);
                try {
                    if(resultSet.next())
                        setReturnValue(new HostName(id, resultSet.getString("host"), resultSet.getString("dest")));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
