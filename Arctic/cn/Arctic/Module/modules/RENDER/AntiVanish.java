/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.Arctic.Module.modules.RENDER;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;

public class AntiVanish
extends Module {
    private ArrayList<Entity> entites = new ArrayList();

    public AntiVanish() {
        super("AntiVanish", new String[]{}, ModuleType.Render);
    }

    @Override
    public void onDisable() {
        for (Entity e : this.entites) {
            e.setInvisible(true);
        }
        this.entites.clear();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        for (Object o : this.mc.world.loadedEntityList) {
            EntityLivingBase entity;
            if (!(o instanceof EntityLivingBase) || !(entity = (EntityLivingBase)o).isInvisible() || this.entites.contains(entity)) continue;
            this.entites.add(entity);
            entity.setInvisible(false);
        }
    }
}

