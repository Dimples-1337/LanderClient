package cn.Arctic.Util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.Arctic.Util.encryption.Encryption;



public class WebUtils {

    public static String loadKey = Encryption.initKey(Encryption.initKey(new String(new byte[]{63,11,41,56,51,21,23,13,21,34,56,16,41,0,56,46,0,45,56,6,1,54,64,56,15,56,46,13,11,13,15,54,123,46,123,61,1,85,02,56,123,4,13,31,123,123,123,48,4,16,66,61,68,7,16,1,7,61,64,6,46,6,46,16,46,61,4,56,51,65,65,46,53,4,76,86,1,13,1,4,41,3,5,7,6,8,99,4})));

    public static List<String> getUrlData(String urlStr) {
        URL url = null;
        BufferedReader br = null;
        try {
            List<String> list = new ArrayList<>();
            url = new URL(urlStr);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            int data = 0;
            StringBuilder sb = new StringBuilder();
            while ((data = br.read()) != -1) {
                sb.append((char) data);
            }
            String allData = new String(sb);
            byte[] decrypt = Encryption.decrypt(allData, loadKey);
            String deData = new String(decrypt);
            String[] split = deData.split("\n");
            list = Arrays.asList(split);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String get(String url) throws IOException, NoSuchAlgorithmException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }
        in.close();
        String result;
        result = response.toString();
        //result=Minecraft.obfHWID(HWIDUtils.getHWID());
        return result;
    }

    public static String sendGet(final String url) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String result = "";
        BufferedReader in = null;

        try {
            final String urlNameString = url;
            final URL realUrl = new URL(urlNameString);
            final URLConnection connection = realUrl.openConnection();
            connection.setDoOutput(true);
            connection.setReadTimeout(99781);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1) ;Chrome 33333");
            connection.connect();
            final Map<String, List<String>> map = connection.getHeaderFields();

            for (Map.Entry<String, List<String>> s : map.entrySet()) {
                //System.out.println("Key: " + s.getKey() + " | " + "Value: " + s.getValue() + ".");
            }

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";

            while ((line = in.readLine()) != null) {
                result = String.valueOf(result) + line + "\n";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }

        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception ex3) {
            ex3.printStackTrace();
        }
//result= Minecraft.obfHWID(HWIDUtils.getHWID());
        return result;
    }
}
