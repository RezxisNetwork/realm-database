package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.player.DBPIP;

public class PIPTable extends MySQLStorage {

	public static PIPTable instnace;
    public static Gson gson = new Gson();

    public PIPTable() {
        super("pip");
        instnace = this;
        prefix = "rezxis_";
        tablename = "pip";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("ip", "INT,");
        map.put("player", "INT");
        createTable(map,this.foreignKey("ip", "rezxis_ips", "id"),this.foreignKey("player", "rezxis_players", "id"));
    }
    
    public void insert(DBPIP ip) {
    	execute(new Insert(insertIntoTable() + " (ip,player) VALUES (?,?)",
                ip.getIp(),
                ip.getPlayer()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		ip.setId(keys.get(0));
            }
        });
    }
    
    public void delete(DBPIP ip) {
		execute("DELETE FROM rezxis_pip WHERE id=?",ip.getId());
	}
    
    public DBPIP getFromPlayer(int player) {
    	return (DBPIP) executeQuery(new Query(selectFromTable("*","player = ?"),player) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBPIP(resultSet.getInt("id"),
                    			resultSet.getInt("ip"),
                    			player));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<DBPIP> getAllIPPlayer(int player) {
    	return (ArrayList<DBPIP>) executeQuery(new Query(selectFromTable("*","player = ?"),player) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBPIP> list = new ArrayList<DBPIP>();
                    while (resultSet.next())
                    {
                    	list.add(new DBPIP(resultSet.getInt("id"),
                    			resultSet.getInt("ip"),
                    			player));
                    }
                    setReturnValue(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<DBPIP> getAllfromIP(int ip) {
    	return (ArrayList<DBPIP>) executeQuery(new Query(selectFromTable("*","ip = ?"),ip) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<DBPIP> list = new ArrayList<DBPIP>();
                    while (resultSet.next())
                    {
                    	list.add(new DBPIP(resultSet.getInt("id"),
                    			resultSet.getInt("ip"),
                    			resultSet.getInt("player")));
                    }
                    setReturnValue(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public DBPIP getFromIPPlayer(int ip, int player) {
    	return (DBPIP) executeQuery(new Query(selectFromTable("*","ip = ? and player = ?"),ip,player) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBPIP(resultSet.getInt("id"),
                    			ip,
                    			player));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public DBPIP getFromIP(int ip) {
    	return (DBPIP) executeQuery(new Query(selectFromTable("*","ip = ?"),ip) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBPIP(resultSet.getInt("id"),
                    			ip,
                    			resultSet.getInt("player")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public DBPIP getFromID(int id) {
    	return (DBPIP) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if(resultSet.next())
                    {
                    	setReturnValue(new DBPIP(id,
                    			resultSet.getInt("ip"),
                    			resultSet.getInt("player")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void update(DBPIP ip) {
        execute("UPDATE " + getTable() + " SET ip = ?, player = ? WHERE id = ?",
        		ip.getIp(),
        		ip.getPlayer(),
        		ip.getId());
    }
}
