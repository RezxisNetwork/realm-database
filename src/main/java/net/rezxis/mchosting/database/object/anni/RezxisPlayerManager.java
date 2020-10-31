package net.rezxis.mchosting.database.object.anni;

import com.google.common.base.Charsets;

import net.rezxis.mchosting.database.BukkitVars;
import net.rezxis.mchosting.database.tables.anni.RezxisPlayerTable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * User: Austin
 * Date: 9/14/14
 * Time: 10:28 AM
 */
public class RezxisPlayerManager implements Listener {

    private static RezxisPlayerManager i;
    private static HashMap<UUID,RezxisPlayerLoadEvent.JoinTasks> onJoin = new HashMap<>();

    private boolean onlineMode = false;

    public RezxisPlayerManager()
    {
        i = this;
    }

    public static RezxisPlayer createPlayer(final Player player)
    {
        new Exception(player.getName() + " does not have a rezxis player!!!").printStackTrace();
        return createPlayer(player.getUniqueId(), player.getName(), true, null);
    }

    public static RezxisPlayer createPlayer(final UUID uuid, final String name, boolean schedule, AsyncPlayerPreLoginEvent preLogin)
    {
        if(RezxisPlayer.getOnlineByUUID(uuid) != null)
        {
//            if(rezxisSql.instance.getConfiguration().debug)
//                new Exception("Tried to make a rezxisplayer for " + name + ", but there's already one loaded!").printStackTrace();
            return RezxisPlayer.getOnlineByUUID(uuid);
        }
        final RezxisPlayer rezxisPlayer = new RezxisPlayer(uuid, name);
        if (schedule)
        {
            Bukkit.getScheduler().runTaskAsynchronously(BukkitVars.getPlugin(),new Runnable()
            {
                @Override
                public void run()
                {
                    loadOrCreate(rezxisPlayer);
                    RezxisPlayerLoadEvent event = fireLoadEvent(rezxisPlayer,null);
                    if (event.getKickMessage() != null) {
                        if (Bukkit.getPlayer(uuid) != null)
                            Bukkit.getPlayer(uuid).kickPlayer(event.getKickMessage());
                    }
                }
            });
        }
        else
        {
            loadOrCreate(rezxisPlayer);
            RezxisPlayerLoadEvent event = fireLoadEvent(rezxisPlayer,preLogin);
            if (event.getKickMessage() != null && preLogin != null)
            {
                preLogin.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                preLogin.setKickMessage(event.getKickMessage());
            }
        }
        return rezxisPlayer;
    }

    public static void loadOrCreate(RezxisPlayer rezxisPlayer) {
        if(!RezxisPlayerTable.instance.load(rezxisPlayer))
            RezxisPlayerTable.instance.insert(rezxisPlayer);
    }

    private static RezxisPlayerLoadEvent fireLoadEvent(final RezxisPlayer rezxisPlayer) {
        return fireLoadEvent(rezxisPlayer,null);
    }

    private static RezxisPlayerLoadEvent fireLoadEvent(final RezxisPlayer rezxisPlayer,AsyncPlayerPreLoginEvent e) {
        String name = rezxisPlayer.getName();
        UUID uuid = rezxisPlayer.getUUID();
        final RezxisPlayerLoadEvent event = new RezxisPlayerLoadEvent(uuid, name, rezxisPlayer,e);
        Bukkit.getPluginManager().callEvent(event);
        if (event.getKickMessage() != null)
        {
            Player player = Bukkit.getPlayer(rezxisPlayer.getUUID());
            if(player != null)
                player.kickPlayer(event.getKickMessage());
            else
                event.addOnJoin(new JoinTask(EventPriority.LOWEST) {
                    @Override
                    public void execute(Player player) {
                        player.kickPlayer(event.getKickMessage());
                    }
                });
        }
        final RezxisPlayerLoadEvent.JoinTasks tasks = event.getTasks();
        if(tasks != null)
        {
            final Player player = Bukkit.getPlayer(uuid);
            //If online, do the tasks now.
            if(player != null)
            {
                Bukkit.getScheduler().runTask(BukkitVars.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        tasks.executeAll(player);
                    }
                });
            }
            //If not, add them to a map to be executed when they do join
            else {
                onJoin.put(uuid, tasks);
            }
        }
        return event;
    }

    public static RezxisPlayer getPlayerFromDb(int id)
    {
        RezxisPlayer rezxisPlayer = RezxisPlayerTable.instance.get(id);
        if(rezxisPlayer != null){
            if(Bukkit.getPlayer(rezxisPlayer.getUUID()) != null)
                return RezxisPlayer.get(Bukkit.getPlayer(rezxisPlayer.getUUID()));
            fireLoadEvent(rezxisPlayer);
        }
        return rezxisPlayer;
    }

    public static RezxisPlayer getPlayerByXfUser(int id)
    {
        RezxisPlayer rezxisPlayer = RezxisPlayerTable.instance.getByXen(id);
        if(rezxisPlayer != null){
            if(Bukkit.getPlayer(rezxisPlayer.getUUID()) != null)
                return RezxisPlayer.get(Bukkit.getPlayer(rezxisPlayer.getUUID()));
            fireLoadEvent(rezxisPlayer);
        }
        return rezxisPlayer;
    }

    public static RezxisPlayer getPlayerFromDb(UUID id)
    {
        if(Bukkit.getPlayer(id) != null)
            return RezxisPlayer.get(Bukkit.getPlayer(id));
        RezxisPlayer rezxisPlayer = RezxisPlayerTable.instance.get(id);
        if(rezxisPlayer != null)
            fireLoadEvent(rezxisPlayer);
        return rezxisPlayer;
    }

    public static RezxisPlayer getPlayerFromDb(String name)
    {
        if(Bukkit.getPlayerExact(name) != null)
            return RezxisPlayer.get(Bukkit.getPlayerExact(name));
        RezxisPlayer rezxisPlayer = RezxisPlayerTable.instance.get(name);
        if(rezxisPlayer != null)
            fireLoadEvent(rezxisPlayer);
        return rezxisPlayer;
    }

    @EventHandler
    (priority = EventPriority.LOW)
    public void onLogin(AsyncPlayerPreLoginEvent e)
    {
        if(e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;
        if (!onlineMode)
        {
            UUID offlineUUID = UUID.nameUUIDFromBytes(( "OfflinePlayer:" + e.getName() ).getBytes(Charsets.UTF_8));
            String offlineUUIDString = offlineUUID.toString().replace("-", "");

            if (!offlineUUIDString.equals(e.getUniqueId().toString().replace("-", "")))
                onlineMode = true;
        }
        if(RezxisPlayer.getOnlineByUUID(e.getUniqueId()) == null)
        {
            createPlayer(e.getUniqueId(), e.getName(), false, e);
        }else {
            Bukkit.getLogger().info("No need to create a rezxis player for "+e.getName()+", as there is already one here.");
            fireLoadEvent(RezxisPlayer.getOnlineByUUID(e.getUniqueId()),e);
        }
    }

    @EventHandler
    (priority = EventPriority.LOWEST)
    public void onJoinLowest(PlayerJoinEvent e)
    {
        if(onJoin.containsKey(e.getPlayer().getUniqueId()))
        {
            Bukkit.getLogger().info(e.getPlayer().getName() + " has join tasks.");
            onJoin.get(e.getPlayer().getUniqueId()).execute(EventPriority.LOWEST, e.getPlayer());
        }else
            Bukkit.getLogger().info(e.getPlayer().getName() + " has no join tasks.");
    }

    @EventHandler
            (priority = EventPriority.LOW)
    public void onJoinLow(PlayerJoinEvent e)
    {
        if(onJoin.containsKey(e.getPlayer().getUniqueId()))
            onJoin.get(e.getPlayer().getUniqueId()).execute(EventPriority.LOW,e.getPlayer());
    }

    @EventHandler
            (priority = EventPriority.NORMAL)
    public void onJoinNormal(PlayerJoinEvent e)
    {
        if(onJoin.containsKey(e.getPlayer().getUniqueId()))
            onJoin.get(e.getPlayer().getUniqueId()).execute(EventPriority.NORMAL, e.getPlayer());
    }

    @EventHandler
            (priority = EventPriority.HIGH)
    public void onJoinHigh(PlayerJoinEvent e)
    {
        if(onJoin.containsKey(e.getPlayer().getUniqueId()))
            onJoin.get(e.getPlayer().getUniqueId()).execute(EventPriority.HIGH,e.getPlayer());
    }

    @EventHandler
            (priority = EventPriority.HIGHEST)
    public void onJoinHighest(PlayerJoinEvent e)
    {
        if(onJoin.containsKey(e.getPlayer().getUniqueId()))
            onJoin.get(e.getPlayer().getUniqueId()).execute(EventPriority.HIGHEST, e.getPlayer());
    }

    @EventHandler
            (priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e)
    {
        if(onJoin.containsKey(e.getPlayer().getUniqueId()))
            onJoin.remove(e.getPlayer().getUniqueId()).execute(EventPriority.MONITOR, e.getPlayer());
    }

    @EventHandler
            (priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e)
    {
        removePlayer(RezxisPlayer.getOnlineByUUID(e.getPlayer().getUniqueId()));
    }

    @EventHandler
            (priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onKick(PlayerKickEvent e)
    {
        if(!e.getReason().toLowerCase().contains("another location"))
            removePlayer(RezxisPlayer.getOnlineByUUID(e.getPlayer().getUniqueId()));
    }

    public static void removePlayer(final RezxisPlayer rezxisPlayer) {
        if(rezxisPlayer != null)
        {
            Bukkit.getScheduler().runTaskLater(BukkitVars.getPlugin(),new Runnable() {
                @Override
                public void run() {
                    if(Bukkit.getPlayer(rezxisPlayer.getUUID()) == null)
                    {
                        RezxisPlayer.remove(rezxisPlayer.getPlayerId(), rezxisPlayer.getUUID());
                        rezxisPlayer.getComponentBag().removeAllComponents(rezxisPlayer);
                        Bukkit.getPluginManager().callEvent(new RezxisPlayerUnloadEvent(rezxisPlayer));
                    }
                }
            },5*20);
        }
    }

    public static void saveXenId(RezxisPlayer rezxisPlayer, int xenId)
    {
        rezxisPlayer.setXfUser(xenId);
        RezxisPlayerTable.instance.saveXenId(rezxisPlayer);
    }
}