package net.rezxis.mchosting.database.crates;

import net.rezxis.mchosting.database.ColorUtil;

public enum CrateTypes {

    UNKNOWN("unknown", "BARRIER", "Unknown"),
    NORMAL("normal", "CHEST", ColorUtil.COLOR_CHAR+"fノーマル"),
    RARE("rare", "STORAGE_MINECART", ColorUtil.COLOR_CHAR+"9レア"),
    VOTE("vote", "POWERED_MINECART", ColorUtil.COLOR_CHAR+"e投票");

    private final String name;
    private final String displayItem;
    private final String lang;

    private CrateTypes(String name, String displayItem, String lang) {
        this.name = name;
        this.displayItem = displayItem;
        this.lang = lang;
    }
    /*public static CrateTypes getByCrate(DBCrate crate) {
        for(CrateTypes ct : values()) {
            if(ct.name.equals(crate.getType())) {
                return ct;
            }
        }
        return CrateTypes.UNKNOWN;
    }*/

    public String getTypeString() { return name; }
    public String getName() { return lang; }
    public String getDisplayItem() { return displayItem; }
}
