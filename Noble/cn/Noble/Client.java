/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.ibm.icu.text.SimpleDateFormat;

import cn.Noble.Api.CustomUI.HUDManager;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.login.AltManager;
import cn.Noble.GUI.notifications.Notification;
import cn.Noble.GUI.notifications.Notification.Type;
import cn.Noble.Manager.*;
import cn.Noble.Module.Module;
import cn.Noble.Module.modules.COMBAT.Aura;
import cn.Noble.Module.modules.GUI.HUD;
import cn.Noble.Util.thealtening.AltService;
import cn.Noble.Values.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import viamcp.ViaMCP;

public class Client {
	public static String name = "Noble";
	public static String version = "220226";
	public static boolean online = false;
	public static int AutoDigID = 26;
	public static Client instance = new Client();
	public static String username = "";

	public static boolean shouldShaderBack = true;

	public static boolean paiduser;
	public static boolean passedcheck;

	private static ArrayList<Notification> notifications = new ArrayList<>();

	public Logger logger = LogManager.getLogger();
	private static ModuleManager modulemanager;
	private CommandManager commandmanager;
	private AltManager accountManager;
	private FriendManager friendmanager;
	private TextureManager texturemanager;
	public FileManager fileMgr;
	public ModuleManager modMgr;
	public HUDManager hudManager;
	public FontLoaders fontLoaders;

	public static final MamagerRegistry MANAGER_REGISTRY = new MamagerRegistry();
	public static ResourceLocation CLIENT_CAPE = new ResourceLocation("Melody/Cape.png");
	public ResourceLocation logo = new ResourceLocation("Client/logo.png");
	public KeyBinding keyBindMelodyCommand = new KeyBinding("key.command", 52, "key.categories.multiplayer");
	public static float Pitch;
	public static String dev ="RuoChen";

	public void start() {

		try {
			ViaMCP.getInstance().start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.passedcheck = false;
		this.paiduser = false;

		this.texturemanager = new TextureManager();

		this.commandmanager = new CommandManager();
		this.commandmanager.init();

		this.friendmanager = new FriendManager();
		this.friendmanager.init();

		this.modulemanager = new ModuleManager();
		this.modulemanager.init();

		this.hudManager = new HUDManager();
		this.hudManager.init();

		this.accountManager = new AltManager();
		AltManager.init();
		AltManager.setupAlts();
		FileManager.init();
	}

	public static ModuleManager getModuleManager() {
		return modulemanager;
	}

	public CommandManager getCommandManager() {
		return this.commandmanager;
	}

	public AltManager getAltManager() {
		return this.accountManager;
	}

	public static void drawNotifications() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		double startY = res.getScaledHeight() - 5;
		final double lastY = startY;
		for (int i = 0; i < notifications.size(); i++) {
			Notification not = notifications.get(i);
			if (not.shouldDelete())
				notifications.remove(i);
			not.draw(startY, lastY);
			startY -= not.getHeight() + 1;
		}
	}

	public static void sendClientMessage(String message, Type type, int time) {
		notifications.add(new Notification(message, type, time));
	}

	public void stop() {
		this.logger.info("[Noble] Shutting Down...");
		this.logger.info("[Noble] Save Settings...");
		this.saveSettings();
	}

	public void saveSettings() {
		String values = "";
		for (final Module m : ModuleManager.getModules()) {
			for (final Value v : m.getValues()) {
				values = String.valueOf(values)
						+ String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
			}
		}
		FileManager.save("Values.json", values, false);
		this.logger.info("[Noble] Value Settings Saved.");

		String enabled = "";
		for (final Module j : ModuleManager.getModules()) {
			if (j.isEnabled()) {
				enabled = String.valueOf(enabled) + String.format("%s%s", j.getName(), System.lineSeparator());
			}
		}
		FileManager.save("Enabled.json", enabled, false);
		this.logger.info("[Noble] Enable Settings Saved.");
		this.logger.info("[Noble] Settings Saved Successfully.");
	}

	private AltService altService = new AltService();

	public void switchToMojang() {
		try {
			altService.switchService(AltService.EnumAltService.MOJANG);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			System.out.println("Couldnt switch to MoJANG altservice");
		}
	}

	public void switchToTheAltening() {
		try {
			altService.switchService(AltService.EnumAltService.THEALTENING);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			System.out.println("Couldnt switch to altening altservice");
		}
	}

	public Object ForamFix(Object o, int recursion) {
		if (o == null)
			return o;

		Class c = o.getClass();
		if (o instanceof Multimap) {
			if (o instanceof ImmutableMultimap) {
				// TODO 自动生成的马
			} else {
				for (Object key : ((Multimap) o).keySet()) {
					List l = Lists.newArrayList(((Multimap) o).values());
					for (int i = 0; i < l.size(); i++) {
						l.set(i, ForamFix(l.get(i), recursion + 1));
					}

					((Multimap) o).replaceValues(key, l);
				}
			}
		} else if (o instanceof Map) {
			if (o instanceof ImmutableMap) {
				ImmutableMap im = (ImmutableMap) o;
				Map newMap = new HashMap();
				boolean deduplicated = false;
				for (Object key : im.keySet()) {
					Object a = im.get(key);
					Object b = ForamFix(a, recursion + 1);
					newMap.put(key, b != null ? b : a);
					if (b != null && b != a)
						deduplicated = true;
				}
				if (deduplicated) {
					return ImmutableMap.copyOf(newMap);
				}
			} else {
				for (Object key : ((Map) o).keySet()) {
					Object value = ((Map) o).get(key);
					Object valueD = ForamFix(value, recursion + 1);
					if (valueD != null && value != valueD)
						((Map) o).put(key, valueD);
				}
			}
		} else if (o instanceof List) {
			if (o instanceof ImmutableList) {
				ImmutableList il = (ImmutableList) o;
				List newList = new ArrayList();
				boolean deduplicated = false;
				for (int i = 0; i < il.size(); i++) {
					Object a = il.get(i);
					Object b = ForamFix(a, recursion + 1);
					newList.add(b != null ? b : a);
					if (b != null && b != a)
						deduplicated = true;
				}
				if (deduplicated) {
					return ImmutableList.copyOf(newList);
				}
			} else {
				List l = (List) o;
				for (int i = 0; i < l.size(); i++) {
					l.set(i, ForamFix(l.get(i), recursion + 1));
				}
			}
		} else if (c.isArray()) {
			for (int i = 0; i < Array.getLength(o); i++) {
				Object entry = Array.get(o, i);
				Object entryD = ForamFix(entry, recursion + 1);
				if (entryD != null && entry != entryD)
					Array.set(o, i, entryD);
			}
		}

		return o;
	}

}
