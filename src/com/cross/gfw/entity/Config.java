package com.cross.gfw.entity;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.cross.gfw.util.FileUtils;

public class Config {
	
	public static boolean init() {
		
		boolean firstUse = false;
		String os = System.getProperty("os.name");
		String userName = System.getProperty("user.name");
		
		if(os.equals("Linux")) {
			FILE = "/home/"+userName+"/.cstgfw/config.json";
		} else if (os.equals("Windows")) {
			FILE = "/home/"+userName+"/.cstgfw/config.json";
		}
		
		File config = new File(FILE);
		if (!(firstUse = !config.exists())) {
			String data = FileUtils.file2Str(FILE);
			JSONObject jo = new JSONObject(data);
			USR = jo.optString("usr");
			LOCAL_DATE = jo.optString("date");
		}
		
		System.out.println(os + ", " + userName + ", " + USR + ", " + LOCAL_DATE);
		
		return firstUse;
	}

	// check user is granted
	public static boolean isGranted(String usr) {
		StringBuffer usrPdt = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(usr.getBytes());
			byte[] digest = md.digest();
			
			for (byte b : digest) {
				usrPdt.append(String.format("%02x", b & 0xff));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		if (null == granted.get(usrPdt.toString())) {
			return false;
		} else {
			return granted.get(usrPdt.toString());
		}
	}
	
	// add user into granted list
	public static void grant(String usr) {
		if(null == granted) {
			granted = new HashMap<String, Boolean>();
		}
		
		granted.put(usr, true);
	}
	
	public static boolean isNewVersionAvailable() {
		boolean flag = false;
		
		int ly = Integer.valueOf(LOCAL_DATE.substring(0, 4));
		int lm = Integer.valueOf(LOCAL_DATE.substring(4, 6));
		int ld = Integer.valueOf(LOCAL_DATE.substring(6, 8));
		
		int ry = Integer.valueOf(REMOT_DATE.substring(0, 4));
		int rm = Integer.valueOf(REMOT_DATE.substring(4, 6));
		int rd = Integer.valueOf(REMOT_DATE.substring(6, 8));
		
		if (ry > ly) {
			return true;
		} else {
			if (rm > lm) {
				return true;
			} else {
				if (rd > ld) {
					return true;
				}
			}
		}
		
		return flag;
	}
	
	public static final int MAIN_WINDOW_WIDTH = 300;
	
	public static final int MAIN_WINDOW_HEIGHT = 400;
	
	public static final String FONT = "Serif";
	
	public static final String HOSTS_URL = "https://code.csdn.net/zxfhacker/zxdstore/blob/master/CSTGFW/data/hosts_linux";
	public static final String MANIFEST_URL = "https://code.csdn.net/zxfhacker/zxdstore/blob/master/CSTGFW/manifest.json";
	
	public static String OS;// 操作系統
	public static String FILE;// 配置文件
	public static String USR;// 用戶，非MD5加密字符串
	public static String LOCAL_DATE;// 本地更新日期
	public static String REMOT_DATE;// 遠程更新日期
	
	private static Map<String, Boolean> granted;// 已授權用戶列表
}
