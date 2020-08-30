package net.rezxis.mchosting.database.object.anni;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.rezxis.mchosting.database.components.ComponentBag;
import net.rezxis.mchosting.database.components.PlayerComponent;

import java.util.HashMap;
import java.util.UUID;

/**
 * User: Austin
 * Date: 9/14/14
 * Time: 10:24 AM
 */
public class RezxisPlayer {
    private static HashMap<UUID,RezxisPlayer> players = new HashMap<>();
    private static HashMap<Integer,RezxisPlayer> playersById = new HashMap<>();
    private final String name;
    private String displayName;
    private boolean displayNameChanged = false;
    private final UUID uuid;
    private int playerId = -1;
    private int xfUser = -1;

    private final ComponentBag componentBag;

    public static boolean isLoaded(UUID uuid){
        return players.containsKey(uuid);
    }

    public static RezxisPlayer get(Player player) {
        if(player == null)
            return null;
        return players.containsKey(player.getUniqueId()) ? players.get(player.getUniqueId()) : RezxisPlayerManager.createPlayer(player);
    }

    public static RezxisPlayer getOnlineByUUID(UUID uuid) {
        return players.containsKey(uuid) ? players.get(uuid) : null;
    }

    public static Player getOnlineById(int id)
    {
        if(playersById.containsKey(id))
        {
            UUID uuid = playersById.get(id).getUUID();
            return Bukkit.getPlayer(uuid);
        }
        return null;
    }

    public static void remove(int id,UUID uuid) {
        players.remove(uuid);
        playersById.remove(id);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public RezxisPlayer(int id, String name, UUID uuid, int xfUser)
    {
        playersById.put(id,this);
        players.put(uuid,this);
        this.playerId = id;
        this.name = name;
        this.uuid = uuid;
        this.xfUser = xfUser;
        componentBag = new ComponentBag();
    }

    public RezxisPlayer(UUID uuid, String name)
    {
        this.name = name;
        this.uuid = uuid;
        players.put(uuid, this);
        componentBag = new ComponentBag();
    }

    public RezxisPlayer(Player player)
    {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        players.put(uuid,this);

        componentBag = new ComponentBag();
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setPlayerId(int playerId) {
        playersById.put(playerId,this);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public void setXfUser(int xfUser)
    {
        this.xfUser = xfUser;
    }

    public int getXfUser()
    {
        return xfUser;
    }

    public String getDisplayName()
    {
        Player player = getPlayer();
        if(player != null)
            return player.getDisplayName();
        else return name;
    }

    public void addComponent(PlayerComponent component)
    {
        componentBag.addComponent(this, component);
    }

    public void removeComponent(Class<? extends PlayerComponent> clazz)
    {
        componentBag.removeComponent(this, clazz);
    }

    public <T extends PlayerComponent> T getComponent(Class<T> clazz)
    {
        return componentBag.getComponent(clazz);
    }

    public ComponentBag getComponentBag() {
        return componentBag;
    }
}
