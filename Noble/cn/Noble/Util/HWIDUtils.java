package cn.Noble.Util;

import javax.swing.*;

import cn.Noble.Client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class HWIDUtils {

	public static boolean isCheck;

	private static void show(){
		JOptionPane.showMessageDialog(null,new String(new byte[]{72, 87, 73, 68, 32, 104, 97, 115, 32, 98, 101, 101, 110, 32, 99, 111, 112, 105, 101, 100, 32, 116, 111, 32, 116, 104, 101, 32, 99, 108, 105, 112, 98, 111, 97, 114, 100, 44, 32, 112, 108, 101, 97, 115, 101, 32, 98, 114, 105, 110, 103, 32, 121, 111, 117, 114, 32, 117, 115, 101, 114, 110, 97, 109, 101, 32, 97, 110, 100, 32, 112, 97, 115, 115, 119, 111, 114, 100, 32, 116, 111, 32, 116, 104, 101, 32, 97, 117, 116, 104, 111, 114, 32, 116, 111, 32, 114, 101, 103, 105, 115, 116, 101, 114}));
	}

	public static String getHWID() {
		try {
			StringBuilder s = new StringBuilder();
			String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
			byte[] bytes = main.getBytes("UTF-8");
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] md5 = messageDigest.digest(bytes);
			int i = 0;
			for (byte b : md5) {
				s.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
				if (i != md5.length - 1) {
					s.append("");
				}
				i++;
//			System.out.println(calendar.get(Calendar.YEAR));
			}
			return (s.toString()).substring(s.length() - 15, s.length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}