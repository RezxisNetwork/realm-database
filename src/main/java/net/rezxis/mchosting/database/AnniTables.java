package net.rezxis.mchosting.database;

import lombok.Getter;
import net.rezxis.mchosting.database.tables.anni.AnniGameTable;

public class AnniTables {

	@Getter
	private static AnniGameTable ATable;
	
	public static void register() {
		ATable = new AnniGameTable();
	}
}