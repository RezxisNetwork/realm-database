package net.rezxis.mchosting.databse;

public class DBCrate {

    private long id;
    private String type;

    public DBCrate(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public long getId() { return id; }

    public String getType() { return type; }
}
