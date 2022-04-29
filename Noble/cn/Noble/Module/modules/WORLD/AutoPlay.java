
package cn.Noble.Module.modules.WORLD;

import java.util.Timer;
import java.util.TimerTask;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventChat;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventPacketSend;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.AnimationUtils;
import cn.Noble.GUI.NewNotification.NotificationPublisher;
import cn.Noble.GUI.NewNotification.NotificationType;
import cn.Noble.GUI.notifications.Notification.Type;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.COMBAT.Aura;
import cn.Noble.Util.Colors;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.Timer.TimeHelper;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
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
					AutoPlay.mc.player.sendChatMessage("GG");
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
