package com.cross.gfw.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

	public static String file2Str(String filepath) {
		String jstr = null;
		try {
			FileInputStream in = new FileInputStream(filepath);
			if (null != in) {
				String ret = "";
				final int bufsize = 1024 * 4;
				byte[] buffer = new byte[bufsize];
				int byteread = in.read(buffer, 0, bufsize);
				while (0 <= byteread) {
					ret += new String(buffer, 0, byteread);
					byteread = in.read(buffer, 0, bufsize);
				}
				in.close();
				jstr = ret + "";
			}
		} catch (Exception e) {
		}
		return jstr;
	}

	public static String getFileDir(String filepath) {
		String p = "";
		if (filepath != null) {
			String[] ps = filepath.split("/");
			int depth = ps.length - 1;
			for (int d = 0; d < depth; d++) {
				p += ps[d];
				p += "/";
			}
		}
		return p;
	}

	public static boolean str2File(String path, String str) {
		if (str == null) {
			return false;
		}

		try {
			File dir = new File(getFileDir(path));
			dir.mkdirs();

			File file = new File(path);
			file.createNewFile();

			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(str);
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
