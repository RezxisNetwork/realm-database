package net.rezxis.mchosting.database;

import lombok.Getter;
import lombok.Setter;

public class Database {

	@Getter
	private static String host;
	@Getter
	private static String user;
	@Getter
	private static String pass;
	@Getter
	private static String port;
	@Getter
	private static String name;
	@Getter@Setter
	private static boolean set = false;
	
	public static void init(String h,String u,String p,String po,String n, boolean hook) {
		if (set) {
			BukkitVars.init();
		}
		host = h;
		user = u;
		pass = p;
		port = po;
		name = n;
		try {
			@SuppressWarnings("unused")
			Class<?> cls = Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		MySQLProvider.init();
		if (hook) {
			Runtime.getRuntime().addShutdownHook(new Thread(()-> {
				MySQLProvider.closeAllConnections();
			} ));
		}
		System.out.println("Database was initialized.");
		Tables.register();
		AnniTables.register();
	}
	
	public static void init(String h,String u,String p,String po,String n) {
		init(h,u,p,po,n,true);
	}
	
	public static void init(String h,String u,String p,int po,String n) {
		init(h,u,p,String.valueOf(po),n,true);
	}
}
