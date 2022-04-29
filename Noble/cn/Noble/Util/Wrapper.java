package cn.Noble.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.Noble.Event.events.EventMove;
import cn.Noble.Manager.FriendManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;

public class Wrapper {
    public static final byte HOTBAR_UP = 0;
    public static final byte HOTBAR_DOWN = 1;
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static FontRenderer fr() {
        return Wrapper.mc.fontRendererObj;
    }

    public static int width() {
        return new ScaledResolution(mc).getScaledWidth();
    }

    public static int height() {
        return new ScaledResolution(mc).getScaledHeight();
    }

    public static Block block(BlockPos pos, double offset) {
        return Minecraft.getMinecraft().world.getBlockState(pos.add(0.0, offset, 0.0)).getBlock();
    }

    public static void position(double x, double y, double z, boolean grounded) {
        Minecraft.getMinecraft().player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, grounded));
    }

    public static float pitch() {
        return Minecraft.getMinecraft().player.rotationPitch;
    }

    public static void pitch(float pitch) {
        Minecraft.getMinecraft().player.rotationPitch = pitch;
    }

    public static float yaw() {
        return Minecraft.getMinecraft().player.rotationYaw;
    }

    public static void yaw(float yaw) {
        Minecraft.getMinecraft().player.rotationYaw = yaw;
    }

    public static double x() {
        return Minecraft.getMinecraft().player.posX;
    }

    public static void x(double x) {
        Minecraft.getMinecraft().player.posX = x;
    }

    public static double y() {
        return Minecraft.getMinecraft().player.posY;
    }

    public static void y(double y) {
        Minecraft.getMinecraft().player.posY = y;
    }

    public static double z() {
        return Minecraft.getMinecraft().player.posZ;
    }

    public static void z(double z) {
        Minecraft.getMinecraft().player.posZ = z;
    }

    public static String withColors(String identifier, String input) {
        String output = input;
        int index = output.indexOf(identifier);
        while (output.indexOf(identifier) != -1) {
            output = output.replace(identifier, "\u00a7");
            index = output.indexOf(identifier);
        }
        return output;
    }

    public static void sendMessage(String message, boolean toServer) {
        if (toServer) {
            Minecraft.getMinecraft().player.sendChatMessage(message);
        } else {
            Minecraft.getMinecraft().player.addChatMessage(new ChatComponentText(String.format("%s%s", "Null: " + (Object)((Object)EnumChatFormatting.GRAY), message)));
        }
    }

    public static void msgPlayer(String msg) {
        Minecraft.getMinecraft().player.addChatMessage(new ChatComponentText(String.format("%s%s", "Null: " + (Object)((Object)EnumChatFormatting.GRAY), msg)));
    }

    public static int windowWidth() {
        return new ScaledResolution(mc).getScaledWidth();
    }

    public static int windowHeight() {
        return new ScaledResolution(mc).getScaledHeight();
    }

    public static String capitalize(String line) {
        return String.valueOf(Character.toUpperCase(line.charAt(0))) + line.substring(1);
    }

    public static Entity getEntity(double distance) {
        if (Wrapper.getEntity(distance, 0.0, 0.0f) == null) {
            return null;
        }
        return (Entity)Wrapper.getEntity(distance, 0.0, 0.0f)[0];
    }

    public static Object[] getEntity(double distance, double expand, float partialTicks) {
        Entity var2 = mc.getRenderViewEntity();
        Entity entity = null;
        if (var2 == null || Minecraft.getMinecraft().world == null) {
            return null;
        }
        Wrapper.mc.mcProfiler.startSection("pick");
        Vec3 var3 = var2.getPositionEyes(0.0f);
        Vec3 var4 = var2.getLook(0.0f);
        Vec3 var5 = var3.addVector(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance);
        Vec3 var6 = null;
        float var7 = 1.0f;
        List<Entity> var8 = Minecraft.getMinecraft().world.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance).expand(1.0, 1.0, 1.0));
        double var9 = distance;
        int var10 = 0;
        while (var10 < var8.size()) {
            Entity var11 = var8.get(var10);
            if (var11.canBeCollidedWith()) {
                double var15;
                float var12 = var11.getCollisionBorderSize();
                AxisAlignedBB var13 = var11.getEntityBoundingBox().expand(var12, var12, var12);
                var13 = var13.expand(expand, expand, expand);
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
                        canRiderInteract = Reflector.callBoolean(var11, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
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
        Wrapper.mc.mcProfiler.endSection();
        if (entity == null || var6 == null) {
            return null;
        }
        return new Object[]{entity, var6};
    }

    public static void sendPacketInQueue(Packet p) {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft().player.sendQueue.addToSendQueue(p);
    }

    public static float getDistanceToEntity(Entity from, Entity to) {
        return from.getDistanceToEntity(to);
    }

    public static boolean isWithinFOV(Entity en, double angle) {
        double angleDifference = Wrapper.angleDifference(Minecraft.getMinecraft().player.rotationYaw, Wrapper.getRotationToEntity(en)[0]);
        if (!(angleDifference > 0.0 && angleDifference < angle || - (angle *= 0.5) < angleDifference && angleDifference < 0.0)) {
            return false;
        }
        return true;
    }

    public static double angleDifference(double a2, double b2) {
        return ((a2 - b2) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static double[] getRotationToEntity(Entity entity) {
        double pX = Minecraft.getMinecraft().player.posX;
        double pY = Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight();
        double pZ = Minecraft.getMinecraft().player.posZ;
        double eX = entity.posX;
        double eY = entity.posY + (double)(entity.height / 2.0f);
        double eZ = entity.posZ;
        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new double[]{yaw, 90.0 - pitch};
    }

    public static float[] faceTarget(Entity target, double p_70625_2_, double p_70625_3_, boolean miss) {
        double var6;
        double var4 = target.posX - Minecraft.getMinecraft().player.posX;
        double var8 = target.posZ - Minecraft.getMinecraft().player.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase)target;
            var6 = var10.posY + (double)var10.getEyeHeight() - (Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight());
        } else {
            var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().player.posY + (double)Minecraft.getMinecraft().player.getEyeHeight());
        }
        Random rnd = new Random();
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var13 = (float)(- Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / 3.141592653589793);
        float pitch = Wrapper.changeRotation(Minecraft.getMinecraft().player.rotationPitch, var13, p_70625_3_);
        float yaw = Wrapper.changeRotation(Minecraft.getMinecraft().player.rotationYaw, var12, p_70625_2_);
        return new float[]{yaw, pitch};
    }

    public static float changeRotation(double p_70663_1_, double p_70663_2_, double p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float((float)(p_70663_2_ - p_70663_1_));
        if ((double)var4 > p_70663_3_) {
            var4 = (float)p_70663_3_;
        }
        if ((double)var4 < - p_70663_3_) {
            var4 = (float)(- p_70663_3_);
        }
        return (float)(p_70663_1_ + (double)var4);
    }


    public static Entity findClosestToCross(double range) {
        Entity e = null;
        double best = 360.0;
        Minecraft.getMinecraft();
        for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
            Entity ent = (Entity)o;
            if (!(ent instanceof EntityPlayer)) continue;
            Minecraft.getMinecraft();
            double diffX = ent.posX - Minecraft.getMinecraft().player.posX;
            Minecraft.getMinecraft();
            double diffZ = ent.posZ - Minecraft.getMinecraft().player.posZ;
            float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
            Minecraft.getMinecraft();
            double difference = Math.abs(Wrapper.angleDifference(newYaw, Minecraft.getMinecraft().player.rotationYaw));
            Minecraft.getMinecraft();
            if (ent == Minecraft.getMinecraft().player) continue;
            Minecraft.getMinecraft();
            if ((double)Minecraft.getMinecraft().player.getDistanceToEntity(ent) > range || !(ent instanceof EntityPlayer) || FriendManager.isFriend(ent.getDisplayName().getUnformattedText()) || difference >= best) continue;
            best = difference;
            e = ent;
        }
        return e;
    }

    public static void updatePosition(double x, double y, double z) {
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, Minecraft.getMinecraft().player.onGround));
    }

    public static int getStringWidth(String text, int increase) {
        return Wrapper.fr().getStringWidth(text);
    }

    public static int RGBtoHEX(int r, int g, int b2, int a2) {
        return (a2 << 24) + (r << 16) + (g << 8) + b2;
    }

    public static List<EntityLivingBase> loadedEntityList() {
        ArrayList<EntityLivingBase> loadedList = new ArrayList<EntityLivingBase>();
        loadedList.remove(Minecraft.getMinecraft().player);
        return loadedList;
    }

    public static void sendPacketNoEvents(Packet a2) {
        mc.getNetHandler().getNetworkManager().sendPacket(a2);
    }

    public static int toRGBAHex(float r, float g, float b2, float a2) {
        return ((int)(a2 * 255.0f) & 255) << 24 | ((int)(r * 255.0f) & 255) << 16 | ((int)(g * 255.0f) & 255) << 8 | (int)(b2 * 255.0f) & 255;
    }

    public static boolean isOnSameTeam(boolean teams, EntityLivingBase e) {
        if (teams && e.isOnSameTeam(Minecraft.getMinecraft().player)) {
            return true;
        }
        return false;
    }

    public static int changeAlpha(int color, int alpha) {
        return alpha << 24 | (color &= 16777215);
    }

    public static float[] getRotations(Entity target, Entity caster) {
        double x = target.posX - caster.posX;
        double y = target.posY + (double)target.getEyeHeight() / 1.3 - (caster.posY + (double)caster.getEyeHeight());
        double z = target.posZ - caster.posZ;
        double pos = Math.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(y, pos)) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static void blink(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        try {
            double endX = (double)endPos.getX() + 0.5;
            double endY = (double)endPos.getY() + 1.0;
            double endZ = (double)endPos.getZ() + 0.5;
            double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            int count = 0;
            while (distance > slack) {
                distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
                if (count <= 120) {
                    double offset;
                    boolean next = false;
                    double diffX = curX - endX;
                    double diffY = curY - endY;
                    double diffZ = curZ - endZ;
                    double d = offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
                    if (diffX < 0.0) {
                        curX = Math.abs(diffX) > offset ? (curX += offset) : (curX += Math.abs(diffX));
                    }
                    if (diffX > 0.0) {
                        curX = Math.abs(diffX) > offset ? (curX -= offset) : (curX -= Math.abs(diffX));
                    }
                    if (diffY < 0.0) {
                        curY = Math.abs(diffY) > 0.25 ? (curY += 0.25) : (curY += Math.abs(diffY));
                    }
                    if (diffY > 0.0) {
                        curY = Math.abs(diffY) > 0.25 ? (curY -= 0.25) : (curY -= Math.abs(diffY));
                    }
                    if (diffZ < 0.0) {
                        curZ = Math.abs(diffZ) > offset ? (curZ += offset) : (curZ += Math.abs(diffZ));
                    }
                    if (diffZ > 0.0) {
                        curZ = Math.abs(diffZ) > offset ? (curZ -= offset) : (curZ -= Math.abs(diffZ));
                    }
                    Minecraft.getMinecraft();
                    Minecraft.getMinecraft();
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(curX, curY, curZ, Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch, true));
                    ++count;
                    continue;
                }
                break;
            }
        }
        catch (Exception endX) {
            // empty catch block
        }
    }

	public static ClientPlayerEntity getPlayer() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().player;
	}

	public static Minecraft getMinecraft() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft();
	}
}

