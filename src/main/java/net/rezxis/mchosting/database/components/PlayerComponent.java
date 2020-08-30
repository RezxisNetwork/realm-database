package net.rezxis.mchosting.database.components;

import net.rezxis.mchosting.database.object.anni.RezxisPlayer;

public abstract class PlayerComponent {

    public int playerId;

    public void onAdd(RezxisPlayer rezxisPlayer) {
        playerId = rezxisPlayer.getPlayerId();
    }

    public void onRemove(RezxisPlayer rezxisPlayer) {}
}