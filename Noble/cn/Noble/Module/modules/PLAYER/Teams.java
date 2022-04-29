package cn.Noble.Module.modules.PLAYER;

import cn.Noble.Client;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Teams extends Module{

	public Teams() {
		super("Teams", new String[] {"Teams"}, ModuleType.Player);
	}

	public static boolean isOnSameTeam(Entity entity) {
		if (!Client.instance.getModuleManager().getModuleByClass(Teams.class).isEnabled()) {
			return false;
		}
		if (Minecraft.getMinecraft().player.getDisplayName().getUnformattedText().startsWith("\u00a7")) {
			if (Minecraft.getMinecraft().player.getDisplayName().getUnformattedText().length() <= 2
					|| entity.getDisplayName().getUnformattedText().length() <= 2) {
				return false;
			}
			if (Minecraft.getMinecraft().player.getDisplayName().getUnformattedText().substring(0, 2)
					.equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
				return true;
			}

		}
		return false;
	}

	public static boolean isTeam(EntityPlayer e, EntityPlayer e2) {
		return e.getDisplayName().getFormattedText().contains("ยง" + isOnSameTeam(e))
				&& e2.getDisplayName().getFormattedText().contains("ยง" + isOnSameTeam(e));
	}
}
