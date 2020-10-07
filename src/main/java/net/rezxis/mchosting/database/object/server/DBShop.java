package net.rezxis.mchosting.database.object.server;

import java.util.ArrayList;

public class DBShop {

	private ArrayList<DBShopItembase> items = new ArrayList<>();
	
	public DBShop(ArrayList<DBShopItembase> items) {
		this.items = items;
	}
	
	public ArrayList<DBShopItembase> getItems() {
		return this.items;
	}
	
	public void addItem(DBShopItembase shop) {
		this.items.add(shop);
	}
	
	public void removeItem(DBShopItembase shop) {
		this.items.remove(shop);
	}
}
