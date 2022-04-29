package cn.Noble.Module.modules.RENDER;

import java.awt.Color;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventTick;
import cn.Noble.GUI.clickgui.NewClickGui;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.GUI.HUD;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import net.minecraft.client.Minecraft;

public class ClickGUI
extends Module {

    public ClickGUI() {
        super("ClickGUI", new String[]{"clickui"}, ModuleType.Render);
    }

    @Override
    public void onEnable() {
    	mc.displayGuiScreen(new NewClickGui());
        this.setEnabled(false);
        super.onEnable();
    }
}

