/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.RENDER;

import java.awt.Color;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.Update.EventPostUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.MOVE.Scaffold;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright
extends Module {
	private float old;

    public FullBright() {
        super("FullBright", new String[]{"fbright", "brightness", "bright"}, ModuleType.Render);
        this.setEnabled(true);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @Override
    public void onEnable() {
        this.old = this.mc.gameSettings.gammaSetting;
        super.onDisable();
    }
    @EventHandler
    	public void onPost(EventPostUpdate e) {
//    	Client.instance.getModuleManager().getModuleByClass(Scaffold.class).setEnabled(false);
    	mc.player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(),5000, (int) 1));
    }
   

    @Override
    public void onDisable() {
    	mc.player.removePotionEffect(Potion.nightVision.getId());
    	super.onDisable();
    }
}

