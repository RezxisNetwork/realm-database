package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Getter;
import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.punish.PunishHistory;
import net.rezxis.mchosting.database.object.punish.PunishType;
import net.rezxis.mchosting.database.object.punish.TargetType;

public class PunishsTable extends MySQLStorage {

	@Getter
	private static PunishsTable instance;
	
	public PunishsTable() {
		super("punishs");
		instance = this;
		prefix = "rezxis_";
		tablename = "plugins";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
		map.put("target", "text,");
		map.put("reason", "text,");
		map.put("ttype", "text,");
		map.put("ptype", "text,");
		map.put("from", "text,");
		map.put("start", "datetime,");
		map.put("end", "datetime NULL");
		createTable(map);
	}
	
	public void insert(PunishHistory ph) {
        execute(new Insert(insertIntoTable() + " (target,reason,ttype,ptype,from,start,end) VALUES (?,?,?,?,?,?,?)",
               ph.getTarget(),ph.getReason(),ph.getTargetType().name(),ph.getType().name(),ph.getFrom(),ph.getStart(),ph.getEnd()) {
            @Override
            public void onInsert(List<Integer> integers) {
                if (!integers.isEmpty())
                    ph.setId(integers.get(0));
            }
        });
    }
	
	@SuppressWarnings("unchecked")
	public ArrayList<PunishHistory> getHistory(int target, TargetType type) {
    	return (ArrayList<PunishHistory>) executeQuery(new Query(selectFromTable("*","target = ? AND ttype = ?"), target, type.name()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                	ArrayList<PunishHistory> list = new ArrayList<>();
                    while (resultSet.next())
                    {
                    	list.add(parse(resultSet));
                    }
                    setReturnValue(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	private PunishHistory parse(ResultSet set) throws SQLException {
		return new PunishHistory(set.getInt("id"),set.getString("target"),set.getString("reason"),TargetType.valueOf(set.getString("ttype"))
				,PunishType.valueOf(set.getString("ptype")),set.getString("from"),set.getDate("start"),set.getDate("end"));
	}
}
