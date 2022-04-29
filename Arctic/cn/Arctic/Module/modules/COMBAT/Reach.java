package cn.Arctic.Module.modules.COMBAT;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventAttack;
import cn.Arctic.Event.events.EventClickMouse;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;

public class Reach extends Module
{
    private double currentRange;
    private long lastAttack;
    private long lastMS;
    private Option<Boolean> combo = new Option<Boolean>("combo", true);
    public static Numbers<Double> min;
    public static Numbers<Double> max;

    public Reach() {
        super("Reach", new String[] {"longArm", "longArms"}, ModuleType.Combat);
        this.currentRange = 3.0;
        this.lastAttack = 0L;
        this.lastMS = -1L;
        this.min = new Numbers<>("MinRange", 3.0, 3.0, 6.0, 0.1);
        this.max = new Numbers<>("MaxRange", 4.0, 3.0, 6.0, 0.1);
        this.addValues(this.min, this.max, this.combo);
    }

    private boolean hasTimePassedMS() {
        return this.getCurrentMS() >= this.lastMS + 2000L;
    }

    private void updateBefore() {
        this.lastMS = this.getCurrentMS();
    }

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    @EventHandler
    public void onAttack(EventAttack event) {
        this.lastAttack = System.currentTimeMillis();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (!this.isEnabled()) {
            return;
        }
        if (this.hasTimePassedMS()) {
            if ((this.max.getValue() - this.min.getValue()) < 0.0) {
                return;
            }
            this.currentRange = ThreadLocalRandom.current().nextDouble(this.min.getValue(), this.max.getValue());
            this.updateBefore();
        }
    }

    @EventHandler
    public void onClick(EventClickMouse event) {
        if (!this.isEnabled()) {
            return;
        }
        if (this.combo.getValue() && System.currentTimeMillis() - this.lastAttack > 300L) {
            return;
        }
        final Object[] objects = this.getEntity(this.currentRange);
        if (objects == null) {
            return;
        }
        mc.objectMouseOver = new MovingObjectPosition((Entity) objects[0], (Vec3) objects[1]);
        mc.pointedEntity = (Entity) objects[0];
    }

    private Object[] getEntity(double distance) {
        Entity var2 = mc.getRenderViewEntity();
        Entity entity = null;
        if (var2 == null || mc.world == null) {
            return null;
        }
        mc.mcProfiler.startSection("pick");
        Vec3 var3 = var2.getPositionEyes(0.0f);
        Vec3 var4 = var2.getLook(0.0f);
        Vec3 var5 = var3.addVector(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance);
        Vec3 var6 = null;
        List<Entity> var8 = mc.world.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance).expand(1.0, 1.0, 1.0));
        double var9 = distance;
        int var10 = 0;
        while (var10 < var8.size()) {
            Entity var11 = var8.get(var10);
            if (var11.canBeCollidedWith()) {
                double var15;
                float var12 = var11.getCollisionBorderSize();
                AxisAlignedBB var13 = var11.getEntityBoundingBox().expand(var12, var12, var12);
                var13 = var13.expand(0.0, 0.0, 0.0);
                MovingObjectPosition var14 = var13.calculateIntercept(var3, var5);
                if (var13.isVecInside(var3)) {
                    if (0.0 < var9 || var9 == 0.0) {
                        entity = var11;
                        var6 = var14 == null ? var3 : var14.hitVec;
                        var9 = 0.0;
                    }
                } else if (var14 != null && ((var15 = var3.distanceTo(var14.hitVec)) < var9 || var9 == 0.0)) {
                    boolean canRiderInteract = false;
                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        canRiderInteract = Reflector.callBoolean(var11, Reflector.ForgeEntity_canRiderInteract);
                    }
                    if (var11 == var2.ridingEntity && !canRiderInteract) {
                        if (var9 == 0.0) {
                            entity = var11;
                            var6 = var14.hitVec;
                        }
                    } else {
                        entity = var11;
                        var6 = var14.hitVec;
                        var9 = var15;
                    }
                }
            }
            ++var10;
        }
        if (var9 < distance && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        mc.mcProfiler.endSection();
        if (entity == null || var6 == null) {
            return null;
        }
        return new Object[]{entity, var6};
    }
}
