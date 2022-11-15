package com.example.demoback.common.util;


import java.util.Random;
import java.util.UUID;

public class KeyWord {
	public static String getKeyWordTime() {
		String pk_id = "";
		Random random = new Random();
		pk_id = "" + System.currentTimeMillis() + random.nextInt(10)
				+ random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
				+ random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
		return pk_id;
	}
	public static String getKeyWordUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}