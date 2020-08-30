package net.rezxis.mchosting.database;

import lombok.Getter;
import net.rezxis.mchosting.database.tables.BackupsTable;
import net.rezxis.mchosting.database.tables.CrateTable;
import net.rezxis.mchosting.database.tables.FilesTable;
import net.rezxis.mchosting.database.tables.IPTable;
import net.rezxis.mchosting.database.tables.PIPTable;
import net.rezxis.mchosting.database.tables.PlayersTable;
import net.rezxis.mchosting.database.tables.PluginsTable;
import net.rezxis.mchosting.database.tables.RezxisHostTable;
import net.rezxis.mchosting.database.tables.RezxisKVTable;
import net.rezxis.mchosting.database.tables.ServersTable;
import net.rezxis.mchosting.database.tables.ThirdPartyTable;
import net.rezxis.mchosting.database.tables.UuidTable;
import net.rezxis.mchosting.database.tables.VoteTable;


public class Tables {

	@Getter private static BackupsTable bTable;
	@Getter private static CrateTable cTable;
	@Getter private static FilesTable fTable;
	@Getter private static IPTable ipTable;
	@Getter private static PIPTable pipTable;
	@Getter private static PlayersTable pTable;
	@Getter private static PluginsTable plTable;
	@Getter private static ServersTable sTable;
	@Getter private static ThirdPartyTable tTable;
	@Getter private static UuidTable uTable;
	@Getter private static VoteTable vTable;
	@Getter private static RezxisKVTable rezxisKVTable;
	@Getter private static RezxisHostTable rezxisHostTable;
	
	public static void register() {
		bTable = new BackupsTable();
		cTable = new CrateTable();
		fTable = new FilesTable();
		ipTable = new IPTable();
		pipTable = new PIPTable();
		pTable = new PlayersTable();
		plTable = new PluginsTable();
		sTable = new ServersTable();
		tTable = new ThirdPartyTable();
		uTable = new UuidTable();
		vTable = new VoteTable();
		rezxisKVTable = new RezxisKVTable();
		rezxisHostTable = new RezxisHostTable();
	}
}
