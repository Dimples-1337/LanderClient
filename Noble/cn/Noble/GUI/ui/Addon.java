package cn.Noble.GUI.ui;

import java.security.MessageDigest;

import javax.swing.*;
import javax.swing.*;
import javax.swing.*;
import javax.swing.*;
import javax.swing.*;
import javax.swing.border.*;

public class Addon {
	
	public static String QaQ() throws Exception {
		String var0 = hug(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME")
				+ System.getProperty("user.name"));
		return var0;
	}

	public static String hug(String var0) throws Exception {
		MessageDigest var1 = MessageDigest.getInstance("SHA-512");
		byte[] var2 = new byte[40];
		var1.update(var0.getBytes("iso-8859-1"), 0, var0.length());
		var2 = var1.digest();
		return QwQ(var2);
	}

	public static String QwQ(byte[] var0) {
		StringBuffer var1 = new StringBuffer();
		for (int var2 = 0; var2 < var0.length; ++var2) {
			int var3 = var0[var2] >>> 4 & 15;
			int var4 = 0;

			do {
				if (var3 >= 0 && var3 <= 9) {
					var1.append((char) (48 + var3));
				} else {
					var1.append((char) (97 + (var3 - 10)));
				}

				var3 = var0[var2] & 15;
			} while (var4++ < 1);
		}

		return var1.toString();
	}

}
