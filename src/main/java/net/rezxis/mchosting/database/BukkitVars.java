package net.rezxis.mchosting.database;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import net.rezxis.mchosting.database.object.anni.RezxisPlayerManager;

public class BukkitVars {

	@Getter@Setter
	private static JavaPlugin plugin;
	
	public static void init() {
		plugin.getServer().getPluginManager().registerEvents(new RezxisPlayerManager(), plugin);
	}
}