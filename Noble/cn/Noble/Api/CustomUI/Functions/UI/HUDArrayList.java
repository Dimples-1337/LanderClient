package cn.Noble.Api.CustomUI.Functions.UI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.Noble.Client;
import cn.Noble.Api.CustomUI.HUDApi;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.GUI.HUD;
import cn.Noble.Module.modules.GUI.HUD.FontMode;
import cn.Noble.Util.render.RenderUtil;

public class HUDArrayList extends HUDApi {

	public HUDArrayList() {
		super("ArrayList", RenderUtil.width(), 1);
	}

	@Override
	public void onRender() {
		CFontRenderer fontRenderer = FontLoaders.CNMD18;
		int scaledWidth = x;
		int index = y;

		ArrayList<Module> displayModules = new ArrayList<>();
		Client.instance.getModuleManager();
		for (Module m : ModuleManager.getModules()) {
			if (m.isEnabled()) {
				displayModules.add(m);
			}
		}
		displayModules.sort((o1, o2) -> (int) fontRenderer.getStringWidth(o2.getName() + o2.getSuffix())
				- (int) fontRenderer.getStringWidth(o1.getName() + o1.getSuffix()));

		for (Module module : displayModules) {
			
			if (HUD.arrayLeft.getValue())
				fontRenderer.drawString(module.getName() + module.getSuffix(), scaledWidth - 2, index,
						new Color(255, 255, 255, 100).getRGB());

			else
				fontRenderer.drawString(module.getName() + module.getSuffix(),
						scaledWidth - fontRenderer.getStringWidth(module.getName() + module.getSuffix()) - 2, index,
						new Color(255, 255, 255, 100).getRGB());

			index += fontRenderer.getHeight() + 2;
		}
	}

	@Override
	public void InScreenRender() {
		CFontRenderer fontRenderer = FontLoaders.CNMD18;
		int scaledWidth = x;
		int index = y;

		ArrayList<Module> displayModules = new ArrayList<>();
		Client.instance.getModuleManager();
		for (Module m : ModuleManager.getModules()) {
			if (m.isEnabled()) {
				displayModules.add(m);
			}
		}
		displayModules.sort((o1, o2) -> (int) fontRenderer.getStringWidth(o2.getName() + o2.getSuffix())
				- (int) fontRenderer.getStringWidth(o1.getName() + o1.getSuffix()));

		for (Module module : displayModules) {
			
			if (HUD.arrayLeft.getValue())
				fontRenderer.drawString(module.getName() + module.getSuffix(), scaledWidth - 2, index,
						new Color(255, 255, 255, 100).getRGB());

			else
				fontRenderer.drawString(module.getName() + module.getSuffix(),
						scaledWidth - fontRenderer.getStringWidth(module.getName() + module.getSuffix()) - 2, index,
						new Color(255, 255, 255, 100).getRGB());

			index += fontRenderer.getHeight() + 2;
		}
	}
}
