package net.danielfigge.minioninvaders.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class PathTools {

	public enum OS {
		FreeBSD, Linux, MacOS, Solaris, Windows, Unknown
	}

	public static OS getOS() {
		String os = System.getProperty("os.name").toUpperCase();
		if (os.contains("WIN")) {
			return OS.Windows;
		}
		if (os.contains("MAC")) {
			return OS.MacOS;
		}
		if (os.contains("NUX")) {
			return OS.Linux;
		}
		if (os.contains("FREEBSD")) {
			return OS.FreeBSD;
		}
		if ((os.contains("SOLARIS")) || (os.contains("SUN"))) {
			return OS.Solaris;
		}
		return OS.Unknown;
	}

	public static String getDataDirectory() {
		String dir;
		switch (getOS()) {
		case Linux:
			dir = System.getProperty("user.home");
			break;
		case MacOS:
			dir = System.getProperty("user.home") + "/Library/Application " + "Support";
			break;
		case Windows:
			dir = System.getenv("APPDATA");
			break;
		default:
			dir = System.getProperty("user.dir");
			break;
		}
		return dir + File.separator + ".MinionInvaders" + File.separator;
	}

	public static String getNativeDirectory() {
		return System.getProperty("user.dir") + File.separator + "native" + File.separator + getOS().toString() + File.separator;
	}

	public static void makeSureDataFilesExist() {
		try {
			// DataDirectory
			File dataDir = new File(getDataDirectory());
			if (!dataDir.exists()) {
				dataDir.mkdir();
			}
			// level.mi
			File levelMi = new File(PathTools.getDataDirectory() + "level.mi");
			if (!levelMi.exists()) {
				System.out.println("true");
				FileWriter fstream = new FileWriter(levelMi);
				BufferedWriter bw = new BufferedWriter(fstream);
				bw.write("1");
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}