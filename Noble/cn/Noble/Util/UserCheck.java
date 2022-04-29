package cn.Noble.Util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import cn.Noble.Client;


public class UserCheck {
    public static boolean isDev = false;
    public static boolean isPlus = false;
    public static boolean isCheck = false;

    public static void Check(){
        try {
        	  if (WebUtils.get("https://gitee.com/ruochen-123/ruochenissb/raw/master/IRC-DEV").contains(Client.username)) {
                  isDev = true;
              } else if (WebUtils.get("https://gitee.com/ruochen-123/ruochenissb/blob/master/IRC-PLUS").contains(Client.username)) {
                isPlus = true;
            }else{
                isPlus = false;
                isDev = false;
            }
            isCheck = true;
        } catch (IOException e) {
            e.printStackTrace();
            isCheck = false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            isCheck = false;
			e.printStackTrace();
		}
    }
}
