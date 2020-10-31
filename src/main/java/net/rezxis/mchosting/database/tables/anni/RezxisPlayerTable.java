package net.rezxis.mchosting.database.tables.anni;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.rezxis.mchosting.database.MySQLStorage;
import net.rezxis.mchosting.database.object.anni.PlayerChangedNameEvent;
import net.rezxis.mchosting.database.object.anni.RezxisPlayer;

/**
 * User: Austin
 * Date: 9/14/14
 * Time: 10:23 AM
 */
public class RezxisPlayerTable extends MySQLStorage {

	public static RezxisPlayerTable instance;
	
    public static String foreignKeyFrom(String columnName)
    {
        return "FOREIGN KEY("+columnName+") REFERENCES `rezxis_player`(id) ON DELETE CASCADE";
    }

    public RezxisPlayerTable()
    {
        super("players");
        instance = this;
        prefix = "rezxis_";
        tablename = "player";
        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("uuid", "VARCHAR(40) UNIQUE,");
        map.put("name", "VARCHAR(40),");
        map.put("xf_user", "INT(11),");
        map.put("last_updated","DATETIME");
        createTable(map);
    }

    public RezxisPlayerTable(String dbConfig)
    {
        super(dbConfig);
        prefix = "rezxis_";
        tablename = "player";
    }

    public boolean load(final RezxisPlayer rezxisPlayer)
    {
        return (Boolean) executeQuery(new Query(selectFromTable("id,name,xf_user") + " WHERE uuid = ?", rezxisPlayer.getUUID().toString().replace("-", "")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                        rezxisPlayer.setPlayerId(resultSet.getInt(1));
                        String name = resultSet.getString(2);
                        rezxisPlayer.setXfUser(resultSet.getInt(3));
                        Bukkit.getLogger().info(name +","+ rezxisPlayer.getName());
                        if(!rezxisPlayer.getName().equals(name))
                        {
                            Bukkit.getLogger().info(name + " is now wanting to be called " + rezxisPlayer.getName());
                            PlayerChangedNameEvent event = new PlayerChangedNameEvent(rezxisPlayer, name);
                            Bukkit.getLogger().info("called Event");
                            Bukkit.getPluginManager().callEvent(event);
                            updateName(rezxisPlayer);
                        }
                        setReturnValue(true);
                    }else setReturnValue(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateName(RezxisPlayer rezxisPlayer) {
        execute("UPDATE " + getTable() + " SET name = ?, last_updated = Now() WHERE id = ?", rezxisPlayer.getName(), rezxisPlayer.getPlayerId());
    }

    public void insert(final RezxisPlayer rezxisPlayer)
    {
        execute(new Insert(insertIntoTable() + " (uuid,name,last_updated) VALUES (?,?,Now())",
                rezxisPlayer.getUUID().toString().replace("-", ""),
                rezxisPlayer.getName()) {
            @Override
            public void onInsert(List<Integer> integers) {
                if (!integers.isEmpty())
                    rezxisPlayer.setPlayerId(integers.get(0));
            }
        });
    }

    public RezxisPlayer get(final int id) {
        return (RezxisPlayer) executeQuery(new Query(selectFromTable("name,uuid,xf_user","id = ?"),id) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new RezxisPlayer(id,resultSet.getString(1), getUUIDFromNonDashedString(resultSet.getString(2)), resultSet.getInt(3)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static UUID getUUIDv2()
    {
        UUID uuid = UUID.randomUUID();
        return new UUID((uuid.getMostSignificantBits()&(~0x000000000000F000))|(2<<12), uuid.getLeastSignificantBits());
    }

    public static UUID getUUIDFromNonDashedString(String uuid)
    {
        return UUID.fromString( uuid.substring( 0, 8 ) + "-" + uuid.substring( 8, 12 ) + "-" + uuid.substring( 12, 16 ) + "-" + uuid.substring( 16, 20 ) + "-" + uuid.substring( 20, 32 ) );
    }

    public RezxisPlayer get(final UUID id) {
        return (RezxisPlayer) executeQuery(new Query(selectFromTable("id,name,xf_user","uuid = ?"),id.toString().replace("-","")) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new RezxisPlayer(resultSet.getInt(1),resultSet.getString(2),id, resultSet.getInt(3)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public RezxisPlayer get(final String name) {
        return (RezxisPlayer) executeQuery(new Query(selectFromTable("id,uuid,xf_user","name = ?"),name) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new RezxisPlayer(resultSet.getInt(1),name,getUUIDFromNonDashedString(resultSet.getString(2)), resultSet.getInt(3)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveXenId(RezxisPlayer rezxisPlayer)
    {
        execute("UPDATE " + getTable() + " SET xf_user = ? WHERE id = ?",rezxisPlayer.getXfUser(),rezxisPlayer.getPlayerId());
    }

    public RezxisPlayer getByXen(final int id) {
        return (RezxisPlayer) executeQuery(new Query(selectFromTable("name,uuid,id","xf_user = ?"),id) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try {
                    setReturnValue(null);
                    if(resultSet.next())
                    {
                        setReturnValue(new RezxisPlayer(resultSet.getInt(3),resultSet.getString(1), getUUIDFromNonDashedString(resultSet.getString(2)), id));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
