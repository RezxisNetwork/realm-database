package net.rezxis.mchosting.database.object.anni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class RezxisPlayerLoadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final RezxisPlayer rezxisPlayer;
    private AsyncPlayerPreLoginEvent loginEvent;
    private boolean isLogin;
    private final String name;
    private final UUID uuid;
    private String kickMessage = null;
    private HashMap<EventPriority,List<JoinTask>> tasks = new HashMap<>();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public RezxisPlayerLoadEvent(UUID uuid, String name, RezxisPlayer rezxisPlayer, AsyncPlayerPreLoginEvent loginEvent)
    {
        super(true);
        this.uuid = uuid;
        this.name = name;
        this.rezxisPlayer = rezxisPlayer;
        this.loginEvent = loginEvent;
    }

    public RezxisPlayer getRezxisPlayer() {
        return rezxisPlayer;
    }

    public String getName()
    {
        return name;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public String getKickMessage()
    {
        return kickMessage;
    }

    public void setKickMessage(String kickMessage)
    {
        if(loginEvent != null)
        {
            loginEvent.setKickMessage(kickMessage);
            loginEvent.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
        this.kickMessage = kickMessage;
    }

    public void addOnJoin(JoinTask task)
    {
        if(!tasks.containsKey(task.getPriority()))
            tasks.put(task.getPriority(),new ArrayList<JoinTask>());
        tasks.get(task.getPriority()).add(task);
    }

    public JoinTasks getTasks() {
        return new JoinTasks(tasks);
    }


    public class JoinTasks {
        private HashMap<EventPriority, List<JoinTask>> tasks;

        public JoinTasks(HashMap<EventPriority, List<JoinTask>> tasks) {
            this.tasks = tasks;
        }

        public void execute(EventPriority priority,Player player) {
            if (tasks.containsKey(priority))
                for (JoinTask task : tasks.remove(priority)) {
                    try {
                        task.execute(player);
                    } catch (Exception e)
                    {
                        Bukkit.getLogger().warning("A Join task for rezxis Player threw an exception");
                        e.printStackTrace();
                    }
                }
        }

        public void executeAll(Player player) {
            execute(EventPriority.LOWEST,player);
            execute(EventPriority.LOW,player);
            execute(EventPriority.NORMAL,player);
            execute(EventPriority.HIGH,player);
            execute(EventPriority.HIGHEST,player);
            execute(EventPriority.MONITOR,player);
        }
    }
}
