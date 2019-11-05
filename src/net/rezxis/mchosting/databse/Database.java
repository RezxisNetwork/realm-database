package net.rezxis.mchosting.databse;

public class Database {

	public static Props props;
	
	public static void init() {
		try {
			@SuppressWarnings("unused")
			Class<?> cls = Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		props = new Props("database.propertis");
		MySQLProvider.init();
		System.out.println("Database was initialized.");
	}
	
	public static Props getProps() {
		return props;
	}
}
