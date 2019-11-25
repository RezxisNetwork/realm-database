package net.rezxis.mchosting.databse;

import net.rezxis.mchosting.databse.crates.CrateTypes;

public class DBCrate {

    private long id;
    private CrateTypes type;

    public DBCrate(long id, CrateTypes type) {
        this.id = id;
        this.type = type;
    }

    public long getId() { return id; }

    public CrateTypes getType() { return type; }
}
