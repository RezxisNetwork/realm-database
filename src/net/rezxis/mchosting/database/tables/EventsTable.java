package net.rezxis.mchosting.database.tables;

import java.util.LinkedHashMap;

import com.google.gson.Gson;

import net.rezxis.mchosting.database.DBEvent;
import net.rezxis.mchosting.database.MySQLStorage;

public class EventsTable extends MySQLStorage {

	public static EventsTable instnace;
    public static Gson gson = new Gson();

    public EventsTable() {
        super("events");
        instnace = this;
        prefix = "rezxis_";
        tablename = "events";
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", "INT PRIMARY KEY NOT NULL AUTO_INCREMENT,");
        map.put("owner", "text,");
        map.put("target", "INT,");
        map.put("time", "datetime,");
        map.put("type", "text,");
        map.put("log", "text");
        createTable(map);
    }
}
