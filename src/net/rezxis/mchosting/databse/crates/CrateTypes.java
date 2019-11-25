package net.rezxis.mchosting.databse.crates;

import net.rezxis.mchosting.databse.DBCrate;
import org.bukkit.Material;

public enum CrateTypes {

    UNKNOWN("unknown", Material.BARRIER, "Unknown"),
    NORMAL("normal", Material.CHEST, "&fノーマル"),
    RARE("rare", Material.STORAGE_MINECART, "&9レア"),
    VOTE("vote", Material.POWERED_MINECART, "&e投票");

    private final String name;
    private final Material displayItem;
    private final String lang;

    private CrateTypes(String name, Material displayItem, String lang) {
        this.name = name;
        this.displayItem = displayItem;
        this.lang = lang;
    }

    public static CrateTypes getByCrate(DBCrate crate) {
        for(CrateTypes ct : values()) {
            if(ct.name.equals(crate.getType())) {
                return ct;
            }
        }
        return CrateTypes.UNKNOWN;
    }

    public String getTypeString() { return name; }
    public String getName() { return lang; }
    public Material getDisplayItem() { return displayItem; }
}
