package net.rezxis.mchosting.database;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.crates.CrateTypes;

@Getter
@Setter
public class DBCrate {

    private long id;
    private CrateTypes type;

    public DBCrate(long id, CrateTypes type) {
        this.id = id;
        this.type = type;
    }
}
