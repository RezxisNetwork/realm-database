package net.rezxis.mchosting.databse.tables;

import com.google.gson.Gson;
import net.rezxis.mchosting.databse.DBCrate;
import net.rezxis.mchosting.databse.MySQLStorage;
import net.rezxis.mchosting.databse.crates.CrateTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class CrateTable extends MySQLStorage {
    public static CrateTable instnace;
    public static Gson gson = new Gson();

    public CrateTable() {
        super("crates");
        instnace = this;
        prefix = "rezxis_";
        tablename = "crates";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("owner", "text,");
        map.put("type", "text");
        createTable(map);
    }

    @SuppressWarnings("unchecked")
    public List<DBCrate> getCrates(UUID player, int limit) {
        return (List<DBCrate>) executeQuery(new Query(selectFromTable("`crate_type`,`id`", "`owner`="+player.toString()+" LIMIT "+limit)) {
            @Override
            protected void onResult(ResultSet resultSet) {
                try{
                    List<DBCrate> crates = new ArrayList<>();
                    while (resultSet.next()) {
                        crates.add(
                          new DBCrate(resultSet.getLong("id"), CrateTypes.valueOf(resultSet.getString("type")))
                        );
                    }
                    setReturnValue(crates);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void giveCrate(UUID player, CrateTypes type) {
        execute(new Insert(insertIntoTable() + " (`owner`,`crate_type`) VALUES (?,?)",
                player.toString(),
                type.name()) {
            @Override
            public void onInsert(List<Integer> keys) {
            }
        });
    }

    public void giveManyCrates(UUID player, CrateTypes type, int amount) {
        for(int i = 0; i < amount;i++) {
            giveCrate(player, type);
        }
    }

    public void removeCrate(DBCrate crate) {
        execute("DELETE FROM `crates` WHERE `id`=?", crate.getId());
    }
}
