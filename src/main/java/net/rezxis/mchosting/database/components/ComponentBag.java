package net.rezxis.mchosting.database.components;

import java.util.HashMap;

import net.rezxis.mchosting.database.object.anni.RezxisPlayer;

/**
 * User: Austin
 * Date: 8/14/14
 * Time: 1:45 AM
 */
public class ComponentBag {

    HashMap<Class<? extends PlayerComponent>,PlayerComponent> statuses = new HashMap<>();

    public void addComponent(RezxisPlayer rezxisPlayer, PlayerComponent playerComponent) {
        if(statuses.containsKey(playerComponent.getClass()))
        {
        	System.out.print("Tried to add a " + playerComponent.getClass() + " to " + rezxisPlayer.getName() + ", but they already have one!!! Remove it first.");
            //new Exception("Tried to add a " + playerComponent.getClass() + " to " + rezxisPlayer.getName() + ", but they already have one!!! Remove it first.").printStackTrace();
            //return;
        }
        statuses.put(playerComponent.getClass(), playerComponent);
        playerComponent.onAdd(rezxisPlayer);
    }

    public PlayerComponent removeComponent(RezxisPlayer rezxisPlayer, Class<? extends PlayerComponent> status)
    {
        PlayerComponent remove = statuses.remove(status);
        if(remove != null){
            remove.onRemove(rezxisPlayer);
        }
        return remove;
    }

    public void removeAllComponents(RezxisPlayer rezxisPlayer)
    {
        for (PlayerComponent playerComponent : statuses.values()) {
            playerComponent.onRemove(rezxisPlayer);
        }

        statuses.clear();
    }

    public boolean hasComponent(Class<? extends PlayerComponent> status)
    {
        return statuses.containsKey(status);
    }

    public <T extends PlayerComponent> T getComponent(Class<T> clazz)
    {
        return (T)statuses.get(clazz);
    }

    public boolean isEmpty() {
        return statuses.isEmpty();
    }
}
