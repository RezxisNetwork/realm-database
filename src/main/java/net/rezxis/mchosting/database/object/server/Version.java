package net.rezxis.mchosting.database.object.server;

public enum Version {

	M1_16_3,M1_16_2,M1_16_1,
	M1_15_2,M1_15_1,M1_15,
	M1_14_4,M1_14_3,M1_14_2,M1_14_1,M1_14,
	M1_13_2,M1_13_1,M1_13,
	M1_12_2,M1_12_1,M1_12,
	M1_11,
	M1_10_2,
	M1_9_4,M1_9_2,M1_9,
	M1_8_8,M1_8_3,M1_8;	
	
	public String getFriendly() {
		return this.name().replace("M", "").replace("_", ".");
	}
}
