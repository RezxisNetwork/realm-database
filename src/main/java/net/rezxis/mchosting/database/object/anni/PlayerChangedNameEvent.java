package net.rezxis.mchosting.database.object.anni;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangedNameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final RezxisPlayer rezxisPlayer;
    private final String oldName;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerChangedNameEvent(RezxisPlayer rezxisPlayer,String name)
    {
        this.oldName = name;
        this.rezxisPlayer = rezxisPlayer;
    }

    public RezxisPlayer getRezxisPlayer() {
        return rezxisPlayer;
    }

    public String getOldName()
    {
        return oldName;
    }
}

