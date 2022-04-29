package cn.Noble.Util.RayCast;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.base.Predicates;

import cn.Noble.Util.math.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.optifine.reflect.Reflector;

public class RayCastUtil {

    static Minecraft mc = Minecraft.getMinecraft();

    public static Entity raycastEntity(double range, IEntityFilter entityFilter) {
        return raycastEntity(range, RotationUtil.prevRotations[0], RotationUtil.prevRotations[1], entityFilter);
    }

    private static Entity raycastEntity(double range, float yaw, float pitch, IEntityFilter entityFilter) {
        Entity renderViewEntity = mc.getRenderViewEntity();
        if (renderViewEntity != null) {
            if (mc.world != null) {
                double blockReachDistance = range;
                net.minecraft.util.Vec3 eyePosition = renderViewEntity.getPositionEyes(1.0f);
                float yawCos = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
                float yawSin = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
                float pitchCos = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
                float pitchSin = MathHelper.sin(-pitch * ((float)Math.PI / 180));
                CustomVec3 entityLook = new CustomVec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
                net.minecraft.util.Vec3 vector = eyePosition.addVector(entityLook.getX() * blockReachDistance, entityLook.getY() * blockReachDistance, entityLook.getZ() * blockReachDistance);
                List<Entity> entityList = mc.world.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.getX() * blockReachDistance, entityLook.getY() * blockReachDistance, entityLook.getZ() * blockReachDistance).expand(1.0, 1.0, 1.0), (com.google.common.base.Predicate<? super Entity>)Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
                Entity pointedEntity = null;
                for (Entity entity : entityList) {
                    double eyeDistance;
                    if (!entityFilter.canRaycast(entity)) continue;
                    float collisionBorderSize = entity.getCollisionBorderSize();
                    AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                    MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
                    if (axisAlignedBB.isVecInside(eyePosition)) {
                        if (!(blockReachDistance >= 0.0)) continue;
                        pointedEntity = entity;
                        blockReachDistance = 0.0;
                        continue;
                    }
                    if (movingObjectPosition == null || !((eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec)) < blockReachDistance) && blockReachDistance != 0.0) continue;
                    if (entity == renderViewEntity.ridingEntity && !Reflector.callBoolean(renderViewEntity, Reflector.ForgeEntity_canRiderInteract, new Object[0])) {
                        if (blockReachDistance != 0.0) continue;
                        pointedEntity = entity;
                        continue;
                    }
                    pointedEntity = entity;
                    blockReachDistance = eyeDistance;
                }
                return pointedEntity;
            }
        }
        return null;
    }

    public static interface IEntityFilter {
        public boolean canRaycast(Entity var1);
    }
	
}
