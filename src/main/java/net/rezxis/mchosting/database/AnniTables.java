package net.rezxis.mchosting.database;

import lombok.Getter;
import net.rezxis.mchosting.database.tables.anni.RezxisPlayerTable;

public class AnniTables {

	@Getter private static RezxisPlayerTable rezxisPlayerTable;
	
	public static void register() {
		rezxisPlayerTable = new RezxisPlayerTable();
	}
}
