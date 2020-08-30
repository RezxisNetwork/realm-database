package net.rezxis.mchosting.database.object.anni;

import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;

public abstract class JoinTask {
    private EventPriority priority;
    public JoinTask(){
        this(EventPriority.NORMAL);
    }
    public JoinTask(EventPriority priority){
        this.priority = priority;
    }
    public abstract void execute(Player player);
    public EventPriority getPriority(){return priority;}
}