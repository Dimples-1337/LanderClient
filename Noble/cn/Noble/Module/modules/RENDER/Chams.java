/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cn.Noble.Module.modules.RENDER;

import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPostRenderPlayer;
import cn.Noble.Event.events.EventPreRenderPlayer;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;

public class Chams
extends Module {
    public static enum ChamsMode{
        Color,
        Normal;
    }

    public Mode<Enum> mode = new Mode("Mode", (Enum[])ChamsMode.values(), (Enum)ChamsMode.Color);
    public  Numbers<Double> r = new Numbers<Double>("R",1.0,0.1,1.0,0.1);
    public Numbers<Double> g = new Numbers<Double>("G",1.0,0.1,1.0,0.1);
    public Numbers<Double> b = new Numbers<Double>("B",1.0,0.1,1.0,0.1);
    public Chams() {
        super("Chams", new String[]{"seethru", "cham"}, ModuleType.Render);
        this.addValues(this.mode,this.r,this.g,this.b);
    }
    @Override
    public void onEnable() {
        // TODO Auto-generated method stub
        super.onEnable();
    }
    public void onDisable() {
        // TODO Auto-generated method stub
        super.onDisable();
    }
    @EventHandler
    private void preRenderPlayer(EventPreRenderPlayer e2) {
        for (Object o : mc.world.playerEntities) {
            EntityPlayer p = (EntityPlayer) o;
            if(p instanceof EntityPlayer) {
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -1100000.0f);
            }
        }


    }

    @EventHandler
    private void postRenderPlayer(EventPostRenderPlayer e2) {
        for (Object o : mc.world.playerEntities) {
            EntityPlayer p = (EntityPlayer) o;
            if(p instanceof EntityPlayer) {
                GL11.glDisable(32823);
                GL11.glPolygonOffset(1.0f, 1100000.0f);
            }
        }
    }



}

