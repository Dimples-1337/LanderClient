package cn.Arctic.Module.modules.GUI;

import java.awt.Color;

import cn.Arctic.Api.CustomUI.HUDScreen;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.GUI.clickgui.ClickUi;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import net.minecraft.client.Minecraft;

public class customgui
extends Module {

    public customgui() {
        super("Custom GUI", new String[]{"Custom GUI"}, ModuleType.Gui);
    }

    @Override
    public void onEnable() {
    	mc.displayGuiScreen(new HUDScreen());
        this.setEnabled(false);
        super.onEnable();
    }


}

