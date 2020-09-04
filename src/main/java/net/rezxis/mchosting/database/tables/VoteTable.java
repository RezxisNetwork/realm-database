package net.rezxis.mchosting.database.tables;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.player.DBVote;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class VoteTable extends MySQLStorage {
    public static VoteTable instnace;
    public static Gson gson = new Gson();

    public VoteTable() {
        super("votes");
        instnace = this;
        prefix = "rezxis_";
        tablename = "votes";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("uuid", "text,");
        map.put("streak", "int,");
        map.put("total", "int,");
        map.put("lastvote", "datetime,");
        map.put("rank", "datetime");
        createTable(map);
    }

    public void insert(DBVote vote) {
    	execute(new Insert(insertIntoTable() + " (uuid,streak,total,lastVote,rank) VALUES (?,?,?,?,?)",
                vote.getUuid().toString(),
                vote.getStreak(),
                vote.getTotal(),
                vote.getLastVote(),
                vote.getRank()) {
            @Override
            public void onInsert(List<Integer> keys) {
            	if (!keys.isEmpty())
            		vote.setId(keys.get(0));
            }
        });
    }
    
    public void update(DBVote obj) {
        execute("UPDATE " + getTable() + " SET uuid = ?, streak = ?, total = ?, lastvote = ?, rank = ? WHERE id = ?",
        		obj.getUuid().toString(),
        		obj.getStreak(),
        		obj.getTotal(),
        		obj.getLastVote(),
        		obj.getRank(),
        		obj.getId());
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<DBVote> getVoteWithLimit(int limit) {
    	return (ArrayList<DBVote>) executeQuery(new Query("SELECT * FROM `rezxis_votes` ORDER BY `rezxis_votes`.`total` DESC LIMIT ?",limit) {
			@Override
            protected void onResult(ResultSet resultSet) {
				ArrayList<DBVote> list = new ArrayList<>();
                try {
                    while (resultSet.next())
                    {
                    	list.add(parse(resultSet));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                setReturnValue(list);
            }
        });
    }
    
    public DBVote getVoteByID(int id) {
    	return (DBVote) executeQuery(new Query(selectFromTable("*","id = ?"),id) {
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
    
    public DBVote getVoteByUUID(UUID uuid) {
    	return (DBVote) executeQuery(new Query(selectFromTable("*","uuid = ?"),uuid.toString()) {
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
    
    private static DBVote parse(ResultSet rs) throws SQLException {
    	return new DBVote(rs.getInt("id")
    			,UUID.fromString(rs.getString("uuid"))
    			,rs.getInt("streak")
    			,rs.getInt("total")
    			,rs.getTimestamp("lastvote")
    			,rs.getTimestamp("rank"));
    }
}
