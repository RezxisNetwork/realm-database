package net.rezxis.mchosting.databse;

public class ShopItem {
	private String name;
	private String itemType;
	private String cmd;
	private long price;
	private long earned;
	
	public ShopItem(String name, String itemType, long price, String cmd, long earned) {
		this.name = name;
		this.itemType = itemType;
		this.price = price;
		this.cmd = cmd;
		this.earned = earned;
	}
	
	public long getEarned() {
		return this.earned;
	}
	
	public void setEarned(long earned) {
		this.earned = earned;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setItemType(String type) {
		this.itemType = type;
	}
	
	public String getItemType() {
		return this.itemType;
	}
	
	public long getPrice() {
		return this.price;
	}
	
	public void setPrice(long price) {
		this.price = price;
	}
	
	public String getCMD() {
		return this.cmd;
	}
	
	public void setCMD(String cmd) {
		this.cmd = cmd;
	}
}