package cn.Noble.Module.modules.GUI;

import java.lang.reflect.Field;
import java.util.Map;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventTick;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.GUI.MotionBlur.MotionBlurResourceManager;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.RENDER.ClickGUI;
import cn.Noble.Util.Logger;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;

public class MotionBlur extends Module
{
    public static Numbers<Double> amount = new Numbers("Degree", 2.0, 1.0, 10.0, 1.0);
    int lastvalue = 0;
    private Map domainResourceManagers;

    public MotionBlur() {
        super("MotionBlur",new String[0], ModuleType.Render);
        super.addValues(amount);
    }

    public void onDisable() {
        MotionBlur.mc.entityRenderer.switchUseShader();
    }

    public void onEnable() {
        if (this.domainResourceManagers == null) {
            try {
                Field[] var2;
                for (Field field : var2 = SimpleReloadableResourceManager.class.getDeclaredFields()) {
                    if (field.getType() != Map.class) continue;
                    field.setAccessible(true);
                    this.domainResourceManagers = (Map)field.get((Object)Minecraft.getMinecraft().getResourceManager());
                    break;
                }
            }
            catch (Exception var6) {
                throw new RuntimeException((Throwable)var6);
            }
        }
        if (!this.domainResourceManagers.containsKey((Object)"motionblur")) {
            this.domainResourceManagers.put((Object)"motionblur", (Object)new MotionBlurResourceManager());
        }
        if (MotionBlur.isFastRenderEnabled()) {
            Logger.log("Please Turn OFF FastRender and Try Again.");
            super.setEnabled(false);
            return;
        }
        this.lastvalue = ((Double)amount.getValue()).intValue();
        MotionBlur.applyShader();
    }

    static boolean isFastRenderEnabled() {
        try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean((Object)Minecraft.getMinecraft().gameSettings);
        }
        catch (Exception var1) {
            return false;
        }
    }

    static void applyShader() {
        MotionBlur.mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
    }

    @EventHandler
    public void onClientTick(EventTick event) {
    	int inte = 0;
    	if(Client.instance.getModuleManager().getModuleByClass(ClickGUI.class).isEnabled()) {
    		mc.entityRenderer.switchUseShader();
    	}
        if (!(Minecraft.getMinecraft().entityRenderer.useShader || mc.world == null)
        		|| Client.instance.getModuleManager().getModuleByClass(ClickGUI.class).isEnabled()) {
        	
            this.lastvalue = ((Double)amount.getValue()).intValue();
            MotionBlur.applyShader();
        }
        if (this.domainResourceManagers == null) {
            try {
                Field[] var2;
                for (Field field : var2 = SimpleReloadableResourceManager.class.getDeclaredFields()) {
                    if (field.getType() != Map.class) continue;
                    field.setAccessible(true);
                    this.domainResourceManagers = (Map)field.get((Object)Minecraft.getMinecraft().getResourceManager());
                    break;
                }
            }
            catch (Exception var6) {
                throw new RuntimeException((Throwable)var6);
            }
        }
        if (!this.domainResourceManagers.containsKey((Object)"motionblur")) {
            this.domainResourceManagers.put((Object)"motionblur", (Object)new MotionBlurResourceManager());
        }
        if (MotionBlur.isFastRenderEnabled()) {
            Logger.log("Please Turn OFF FastRender and Try Again.");
            super.setEnabled(false);
            return;
        }
    }
}
