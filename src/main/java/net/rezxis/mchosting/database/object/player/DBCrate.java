package net.rezxis.mchosting.database.object.player;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.crates.CrateTypes;
import net.rezxis.mchosting.database.tables.CrateTable;

@Getter
@Setter
public class DBCrate {

    private long id;
    private CrateTypes type;

    public DBCrate(long id, CrateTypes type) {
        this.id = id;
        this.type = type;
    }
    
    public void remove() {
    	CrateTable.instnace.removeCrate(this);
    }
}
