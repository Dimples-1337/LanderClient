package cn.Arctic.Util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WatchdogHelper {
	public static String getBanSource(String player, String banID) throws Throwable {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://hypixel.net/api/players/" + player.trim());
		httpGet.setHeader("user-agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Mobile Safari/537.36");
		httpGet.setHeader("xf-api-key", "LnM-qSeQqtJlJmJnVt76GhU-SoiolWs9");
		String finalResult = "";
		try {
			try {
				CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
				try {
					finalResult = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				} finally {
					if (response != null) {
						response.close();
					}
				}
			} catch (Throwable throwable) {
				throw throwable;
			}
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}

		Gson gson1 = new Gson();
		JsonObject array = (JsonObject) new Gson().fromJson(finalResult.trim(), JsonObject.class);
		String finalID = banID.replace("#", "");
		String accessLocation = "https://hypixel.net/api/players/" + array.get("uuid").getAsString() + "/ban/"
				+ finalID;
		CloseableHttpClient closeableHttpClient1 = HttpClients.createDefault();
		HttpGet newHttp = new HttpGet(accessLocation);
		newHttp.setHeader("user-agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Mobile Safari/537.36");
		newHttp.setHeader("xf-api-key", "LnM-qSeQqtJlJmJnVt76GhU-SoiolWs9");
		CloseableHttpClient newCloseableHttpClient;
		try {
			try {
				CloseableHttpResponse response = closeableHttpClient1.execute(newHttp);
				try {
					finalResult = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				} finally {
					if (response != null) {
						response.close();
					}
				}
			} catch (Throwable throwable) {
				throw throwable;
			}
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}
		while (finalResult.contains("too_many_request")
				&& JOptionPane.showConfirmDialog((Component) null, "����Ƶ��, ��ȷ���������Ի�ȡ!") == 0) {
			String string4111 = "https://hypixel.net/api/players/" + array.get("uuid").getAsString() + "/ban/"
					+ finalID;
			newCloseableHttpClient = HttpClients.createDefault();
			HttpGet anoHttp = new HttpGet(string4111);
			anoHttp.setHeader("user-agent",
					"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Mobile Safari/537.36");
			anoHttp.setHeader("xf-api-key", "kB3PlymjFqMbQA-KhdJ5N5DcxBajLziW");
			try {
				try {
					CloseableHttpResponse closeableHttpResponse = newCloseableHttpClient.execute(anoHttp);
					try {
						finalResult = IOUtils.toString(closeableHttpResponse.getEntity().getContent(),
								StandardCharsets.UTF_8);
					} finally {
						if (closeableHttpResponse != null) {
							closeableHttpResponse.close();
						}

					}
				} catch (Throwable throwable) {
					throw throwable;
				}
			} catch (IOException iOException) {
				iOException.printStackTrace();
			}
		}
		return getWatchdogString(finalResult);
	}

	public static String getBanID(List<String> messages) {
		for (String message : messages) {
			if (message.contains("#")) {
				return message.substring(message.length() - 9, message.length());
			}
		}
		return "";
	}

	private static String getWatchdogString(String load) {
		final String[] webLines = load.split(",");
		for (String current : webLines) {
			if (current.contains("punishmentCategory")) {
				return current.replace("\n    ", "").replace(String.valueOf('"'), "").replace("punishmentCategory", "");
			}
		}
		return "";
	}

}
