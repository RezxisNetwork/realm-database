package net.rezxis.mchosting.database.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.internal.DBFile;
import net.rezxis.mchosting.database.object.internal.DBFile.Type;

public class FilesTable extends MySQLStorage{

	public static FilesTable instance;
	
	public FilesTable() {
		super("files");
		instance = this;
		prefix = "rezxis_";
		tablename = "files";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("uuid", "text,");
		map.put("secret", "text,");
		map.put("uploaded", "boolean,");
		map.put("time", "datetime,");
		map.put("type", "text");
		createTable(map);
	}
	
	public DBFile get2(String uuid, Type type) {
		return (DBFile) executeQuery(new Query(selectFromTable("*","uuid=? and type=?"),uuid,type.name()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(
                        		new DBFile(resultSet.getString("uuid"),
                        		resultSet.getString("secret"),
                        		resultSet.getBoolean("uploaded"),
                        		resultSet.getDate("time"),
                        		Type.valueOf(resultSet.getString("type")))
                        		);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public DBFile get(String uuid, String secret) {
		return (DBFile) executeQuery(new Query(selectFromTable("*","uuid = ?, secret = ?"),uuid,secret) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(
                        		new DBFile(resultSet.getString("uuid"),
                        		resultSet.getString("secret"),
                        		resultSet.getBoolean("uploaded"),
                        		resultSet.getDate("time"),
                        		Type.valueOf(resultSet.getString("type")))
                        		);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public void sync(DBFile file) {
		executeQuery(new Query(selectFromTable("*","uuid = ?, secret = ?"),file.getUUID(), file.getSecret()) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        file.setTime(resultSet.getDate("time"));
                        file.setUploaded(resultSet.getBoolean("uploaded"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	public void insert(DBFile file) {
		execute(new Insert(insertIntoTable() + " (uuid,secret,uploaded,time,type) VALUES (?,?,?,?,?)",
                file.getUUID(),
                file.getSecret(),
                file.isUploaded(),
                file.getTime(),
                file.getType().name()
        		) {
            @Override
            public void onInsert(List<Integer> integers) {
            }
        });
	}
}
