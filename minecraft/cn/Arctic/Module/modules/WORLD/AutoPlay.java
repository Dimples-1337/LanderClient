
package cn.Arctic.Module.modules.WORLD;

import java.util.Timer;
import java.util.TimerTask;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventChat;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.GUI.AnimationUtils;
import cn.Arctic.GUI.NewNotification.NotificationPublisher;
import cn.Arctic.GUI.NewNotification.NotificationType;
import cn.Arctic.GUI.notifications.Notification.Type;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Module.modules.MOVE.Speed;
import cn.Arctic.Util.Colors;
import cn.Arctic.Util.Chat.Helper;
import cn.Arctic.Util.Timer.TimeHelper;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.potion.Potion;
import net.minecraft.util.ScreenShotHelper;

public class AutoPlay extends Module {

	public static Option<Boolean> AutoGG = new Option<Boolean>("GG", true);
	public static Option<Boolean> AD = new Option<Boolean>("AD", false);
	public static Option<Boolean> ScreenShot = new Option<Boolean>("ScreenShot", false);

	public AutoPlay() {
		super("AutoPlay", new String[] { "AutoPlay", "AutoGG" }, ModuleType.World);
		this.addValues(AutoGG, AD, ScreenShot);
	}

	@EventHandler
	public void onPacket(final EventPacketRecieve event) {
		if (event.getPacket() instanceof S02PacketChat) {
			final S02PacketChat packet = (S02PacketChat) event.getPacket();
			final String game = this.getSubString(packet.getChatComponent().toString(),
					"style=Style{hasParent=true, color=§b, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=RUN_COMMAND, value='/play ",
					"'},");
			if (!game.contains("TextComponent") && !game.equalsIgnoreCase("")) {
				this.next(game);
				if (AutoPlay.AutoGG.getValue() && AutoPlay.AD.getValue()) {
					AutoPlay.mc.player.sendChatMessage("GG -> discord dot gg/tEW6sF2qWV");
				} else if (AutoPlay.AutoGG.getValue() && !AD.getValue()) {
					AutoPlay.mc.player.sendChatMessage("[Lander]GG");
				}
				if (AutoPlay.ScreenShot.getValue()) {
					mc.gameSettings.keyBindScreenshot.pressed = true;
				}
			}
		}
	}

	public String getSubString(final String text, final String left, final String right) {
		String result = "";
		int zLen;
		if (left == null || left.isEmpty()) {
			zLen = 0;
		} else {
			zLen = text.indexOf(left);
			if (zLen > -1) {
				zLen += left.length();
			} else {
				zLen = 0;
			}
		}
		int yLen = text.indexOf(right, zLen);
		if (yLen < 0 || right == null || right.isEmpty()) {
			yLen = text.length();
		}
		result = text.substring(zLen, yLen);
		return result;
	}

	public void next(final String game) {
		NotificationPublisher.queue("AutoPlay", "Sending You to a New Game In 3s.", NotificationType.INFO, 3000, true);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				AutoPlay.mc.player.sendChatMessage("/play " + game);
			}
		}, 3000L);
	}

}
