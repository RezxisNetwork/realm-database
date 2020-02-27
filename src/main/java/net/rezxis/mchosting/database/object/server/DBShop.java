package net.rezxis.mchosting.database.object.server;

import java.util.ArrayList;

public class DBShop {

	private ArrayList<ShopItem> items = new ArrayList<>();
	
	public DBShop(ArrayList<ShopItem> items) {
		this.items = items;
	}
	
	public ArrayList<ShopItem> getItems() {
		return this.items;
	}
	
	public void addItem(ShopItem shop) {
		this.items.add(shop);
	}
	
	public void removeItem(ShopItem shop) {
		this.items.remove(shop);
	}
}
