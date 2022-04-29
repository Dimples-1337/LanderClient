package cn.Arctic.Module.modules.RENDER;

import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

public class NoHurtCam extends Module {

	public NoHurtCam() {
		super("NoHurtCam", new String[] { "seethru", "cham" }, ModuleType.Render);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	public void onDisable() {
		super.onDisable();
	}

	@EventHandler
	private void onHurt(EventRender2D event) {
		ScaledResolution mainWindow = mc.getMainWindow();
		if (mc.player.hurtTime > 0) {
			RenderUtil.drawBorderedRect(0, 0, mainWindow.getScaledWidth(), mainWindow.getScaledHeight(), 10,
					new Color(25 * mc.player.hurtTime, 200, 200, 200 * mc.player.hurtTime).getRGB(),
					new Color(0, 0, 0, 0).getRGB());
		}
	}
}
