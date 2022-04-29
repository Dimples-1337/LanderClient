package cn.Arctic.Module.modules.PLAYER;

import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.block.material.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.client.multiplayer.*;
import java.util.*;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Numbers;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;

public class AutoMine extends Module
{
    private EnumFacing facing;
    private final TimerUtil skipCheckTimer;
    static double max;
    public static Numbers<Double> blockhight;
    
    public AutoMine() {
        super("AutoMine", new String[] { "Automine" }, ModuleType.Player);
        this.facing = EnumFacing.EAST;
        this.skipCheckTimer = new TimerUtil();
        this.addValues(AutoMine.blockhight);
    }
    
    static {
        final Minecraft mc = AutoMine.mc;
        int height;
        if (Minecraft.getMinecraft().world == null) {
            height = 256;
        }else {
            final Minecraft mc2 = AutoMine.mc;
            height = Minecraft.getMinecraft().world.getHeight();
        }
        AutoMine.max = height;
        AutoMine.blockhight = new Numbers<Double>("BlockHeight",16.0, 1.0, AutoMine.max, 1.0);
    }
    
    @Override
    public void onEnable() {
        final Minecraft mc = AutoMine.mc;
        this.facing = EnumFacing.fromAngle(Minecraft.getMinecraft().player.rotationYaw);
        this.skipCheckTimer.setTime(5000L);
        super.onEnable();
    }
    
    @EventHandler
    public void onUpdate(final EventPreUpdate event) {
        this.setSuffix("Smart " + AutoMine.blockhight.getValue());
        final Minecraft mc = AutoMine.mc;
        final double posX = Minecraft.getMinecraft().player.posX;
        final Minecraft mc2 = AutoMine.mc;
        final double posY = Minecraft.getMinecraft().player.posY;
        final Minecraft mc3 = AutoMine.mc;
        final double y2 = posY + Minecraft.getMinecraft().player.getEyeHeight();
        final Minecraft mc4 = AutoMine.mc;
        BlockPos toFace = new BlockPos(posX, y2, Minecraft.getMinecraft().player.posZ).offset(this.facing);
        boolean foundItem = false;
        boolean foundOre = false;
        if (this.skipCheckTimer.hasReached(5000)) {
            final Minecraft mc5 = AutoMine.mc;
            final Block under = getBlockRelativeToEntity(Minecraft.getMinecraft().player, -0.01);
            final Minecraft mc6 = AutoMine.mc;
            final IBlockState stateUnder = getBlockState(getBlockPosRelativeToEntity(Minecraft.getMinecraft().player, -0.01));
            EntityItem theItem = null;
            for (final EntityItem item : getNearbyItems(5)) {
                final Minecraft mc7 = AutoMine.mc;
                if (Minecraft.getMinecraft().player.canEntityBeSeen(item) && item.ticksExisted > 20 && item.ticksExisted < 150) {
                    foundItem = true;
                    theItem = item;
                }
            }
            if (foundItem) {
                faceEntity(theItem);
                final Minecraft mc8 = AutoMine.mc;
                Minecraft.getMinecraft().player.moveFlying(0.0f, 1.0f, 0.1f);
                final double posY2 = theItem.posY;
                final Minecraft mc9 = AutoMine.mc;
                if (posY2 > Minecraft.getMinecraft().player.posY) {
                    final Minecraft mc10 = AutoMine.mc;
                    if (Minecraft.getMinecraft().player.onGround) {
                        final Minecraft mc11 = AutoMine.mc;
                        Minecraft.getMinecraft().player.jump();
                    }
                }
            }
            else {
                for (int x = -3; x <= 3; ++x) {
                    for (int y = -3; y <= 5; ++y) {
                        for (int z = -3; z <= 3; ++z) {
                            final Minecraft mc12 = AutoMine.mc;
                            final double x2 = Minecraft.getMinecraft().player.posX + x;
                            final Minecraft mc13 = AutoMine.mc;
                            final double y3 = Minecraft.getMinecraft().player.posY + y;
                            final Minecraft mc14 = AutoMine.mc;
                            final BlockPos blockPos = new BlockPos(x2, y3, Minecraft.getMinecraft().player.posZ + z);
                            final Block block = getBlock(blockPos);
                            final IBlockState state = getBlockState(blockPos);
                            if (state.getBlock().getMaterial() != Material.air) {
                                if (block instanceof BlockLiquid) {
                                    final Minecraft mc15 = AutoMine.mc;
                                    final WorldClient world = Minecraft.getMinecraft().world;
                                    final Minecraft mc16 = AutoMine.mc;
                                    final double posX2 = Minecraft.getMinecraft().player.posX;
                                    final Minecraft mc17 = AutoMine.mc;
                                    final double posY3 = Minecraft.getMinecraft().player.posY;
                                    final Minecraft mc18 = AutoMine.mc;
                                    final double y4 = posY3 + Minecraft.getMinecraft().player.getEyeHeight();
                                    final Minecraft mc19 = AutoMine.mc;
                                    final MovingObjectPosition trace0 = world.rayTraceBlocks(new Vec3(posX2, y4, Minecraft.getMinecraft().player.posZ), new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), true, false, true);
                                    if (trace0.getBlockPos() == null) {
                                        continue;
                                    }
                                    final BlockPos blockPosTrace = trace0.getBlockPos();
                                    final Block blockTrace = getBlock(blockPosTrace);
                                    if (blockTrace instanceof BlockLiquid) {
                                        this.facing = this.facing.getOpposite();
                                        final Minecraft mc20 = AutoMine.mc;
                                        final double posX3 = Minecraft.getMinecraft().player.posX;
                                        final Minecraft mc21 = AutoMine.mc;
                                        final double posY4 = Minecraft.getMinecraft().player.posY;
                                        final Minecraft mc22 = AutoMine.mc;
                                        final double y5 = posY4 + Minecraft.getMinecraft().player.getEyeHeight();
                                        final Minecraft mc23 = AutoMine.mc;
                                        toFace = new BlockPos(posX3, y5, Minecraft.getMinecraft().player.posZ).offset(this.facing);
                                        if (isBlockPosAir(toFace)) {
                                            toFace = toFace.down();
                                        }
                                        this.skipCheckTimer.reset();
                                        break;
                                    }
                                }
                                if (block instanceof BlockOre || block instanceof BlockRedstoneOre) {
                                    final Minecraft mc24 = AutoMine.mc;
                                    final WorldClient world2 = Minecraft.getMinecraft().world;
                                    final Minecraft mc25 = AutoMine.mc;
                                    final double posX4 = Minecraft.getMinecraft().player.posX;
                                    final Minecraft mc26 = AutoMine.mc;
                                    final double posY5 = Minecraft.getMinecraft().player.posY;
                                    final Minecraft mc27 = AutoMine.mc;
                                    final double y6 = posY5 + Minecraft.getMinecraft().player.getEyeHeight();
                                    final Minecraft mc28 = AutoMine.mc;
                                    final MovingObjectPosition trace0 = world2.rayTraceBlocks(new Vec3(posX4, y6, Minecraft.getMinecraft().player.posZ), new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), true, false, true);
                                    if (trace0.getBlockPos() != null) {
                                        final BlockPos blockPosTrace = trace0.getBlockPos();
                                        final Block blockTrace = getBlock(blockPosTrace);
                                        final double dist = getVec3(blockPos).distanceTo(getVec3(blockPosTrace));
                                        final Minecraft mc29 = AutoMine.mc;
                                        final double xx = Minecraft.getMinecraft().player.posX - (blockPosTrace.getX() + 0.5);
                                        final Minecraft mc30 = AutoMine.mc;
                                        final double posY6 = Minecraft.getMinecraft().player.posY;
                                        final double n = blockPosTrace.getY() + 0.5;
                                        final Minecraft mc31 = AutoMine.mc;
                                        final double yy = posY6 - (n - Minecraft.getMinecraft().player.getEyeHeight());
                                        final Minecraft mc32 = AutoMine.mc;
                                        final double zz = Minecraft.getMinecraft().player.posZ - (blockPosTrace.getZ() + 0.5);
                                        final double dist2 = Math.sqrt(x * x + y * y + z * z);
                                        if (dist2 >= 1.5) {
                                            float yaw = getFacePos(getVec3(blockPosTrace))[0];
                                            yaw = normalizeAngle(yaw);
                                            if (yaw == 45.0f) {
                                                continue;
                                            }
                                            if (yaw == -45.0f) {
                                                continue;
                                            }
                                            if (yaw == 135.0f) {
                                                continue;
                                            }
                                            if (yaw == -135.0f) {
                                                continue;
                                            }
                                        }
                                        if (dist <= 0.0) {
                                            toFace = blockPosTrace;
                                            foundOre = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (foundOre) {
                    faceBlock(toFace);
                    if (!isBlockPosAir(toFace)) {
                        this.mineBlock(toFace);
                    }
                }
                else {
                    final Minecraft mc33 = AutoMine.mc;
                    if (Minecraft.getMinecraft().player.posY > AutoMine.blockhight.getValue()) {
                        if (stateUnder.getBlock().getMaterial() != Material.air) {
                            this.mineBlockUnderPlayer();
                        }
                        else {
                            final Minecraft mc34 = AutoMine.mc;
                            if (Minecraft.getMinecraft().player.onGround) {
                                final Minecraft mc35 = AutoMine.mc;
                                Minecraft.getMinecraft().player.moveFlying(0.0f, 0.5f, 0.1f);
                            }
                        }
                    }
                    else {
                        if (isBlockPosAir(toFace)) {
                            toFace = toFace.down();
                            if (this.isBlockPosSafe(toFace)) {
                                if (isBlockPosAir(toFace)) {
                                    final Minecraft mc36 = AutoMine.mc;
                                    if (Minecraft.getMinecraft().player.onGround) {
                                        final Minecraft mc37 = AutoMine.mc;
                                        Minecraft.getMinecraft().player.moveFlying(0.0f, 1.0f, 0.1f);
                                    }
                                }
                            }
                            else {
                                final EnumFacing facing = this.facing;
                                final Minecraft mc38 = AutoMine.mc;
                                this.facing = EnumFacing.fromAngle(Minecraft.getMinecraft().player.rotationYaw + 90.0f);
                                final Minecraft mc39 = AutoMine.mc;
                                final double posX5 = Minecraft.getMinecraft().player.posX;
                                final Minecraft mc40 = AutoMine.mc;
                                final double posY7 = Minecraft.getMinecraft().player.posY;
                                final Minecraft mc41 = AutoMine.mc;
                                final double y7 = posY7 + Minecraft.getMinecraft().player.getEyeHeight();
                                final Minecraft mc42 = AutoMine.mc;
                                toFace = new BlockPos(posX5, y7, Minecraft.getMinecraft().player.posZ).offset(this.facing);
                                if (!this.isBlockPosSafe(toFace)) {
                                    final EnumFacing facing2 = this.facing;
                                    final Minecraft mc43 = AutoMine.mc;
                                    this.facing = EnumFacing.fromAngle(Minecraft.getMinecraft().player.rotationYaw - 90.0f);
                                    final Minecraft mc44 = AutoMine.mc;
                                    final double posX6 = Minecraft.getMinecraft().player.posX;
                                    final Minecraft mc45 = AutoMine.mc;
                                    final double posY8 = Minecraft.getMinecraft().player.posY;
                                    final Minecraft mc46 = AutoMine.mc;
                                    final double y8 = posY8 + Minecraft.getMinecraft().player.getEyeHeight();
                                    final Minecraft mc47 = AutoMine.mc;
                                    toFace = new BlockPos(posX6, y8, Minecraft.getMinecraft().player.posZ).offset(this.facing);
                                    if (!this.isBlockPosSafe(toFace.down())) {
                                        final EnumFacing facing3 = this.facing;
                                        final Minecraft mc48 = AutoMine.mc;
                                        this.facing = EnumFacing.fromAngle(Minecraft.getMinecraft().player.rotationYaw + 180.0f);
                                        final Minecraft mc49 = AutoMine.mc;
                                        final double posX7 = Minecraft.getMinecraft().player.posX;
                                        final Minecraft mc50 = AutoMine.mc;
                                        final double posY9 = Minecraft.getMinecraft().player.posY;
                                        final Minecraft mc51 = AutoMine.mc;
                                        final double y9 = posY9 + Minecraft.getMinecraft().player.getEyeHeight();
                                        final Minecraft mc52 = AutoMine.mc;
                                        toFace = new BlockPos(posX7, y9, Minecraft.getMinecraft().player.posZ).offset(this.facing);
                                    }
                                }
                            }
                        }
                        if (isBlockPosAir(toFace)) {
                            final Minecraft mc53 = AutoMine.mc;
                            final double posX8 = Minecraft.getMinecraft().player.posX;
                            final Minecraft mc54 = AutoMine.mc;
                            final double posY10 = Minecraft.getMinecraft().player.posY;
                            final Minecraft mc55 = AutoMine.mc;
                            final double y10 = posY10 + Minecraft.getMinecraft().player.getEyeHeight();
                            final Minecraft mc56 = AutoMine.mc;
                            toFace = new BlockPos(posX8, y10, Minecraft.getMinecraft().player.posZ).offset(this.facing);
                        }
                        faceBlock(toFace);
                        if (!isBlockPosAir(toFace)) {
                            this.mineBlock(toFace);
                        }
                    }
                }
            }
        }
        else {
            final Minecraft mc57 = AutoMine.mc;
            if (Minecraft.getMinecraft().player.onGround) {
                final Minecraft mc58 = AutoMine.mc;
                Minecraft.getMinecraft().player.moveFlying(0.0f, 1.0f, 0.1f);
            }
            if (isBlockPosAir(toFace)) {
                toFace = toFace.down();
            }
            faceBlock(toFace);
            if (!isBlockPosAir(toFace)) {
                this.mineBlock(toFace);
            }
        }
    }
    
    public void mineBlockUnderPlayer() {
        final Minecraft mc = AutoMine.mc;
        final BlockPos pos = getBlockPosRelativeToEntity(Minecraft.getMinecraft().player, -0.01);
        this.mineBlock(pos);
    }
    
    public void mineBlock(final BlockPos pos) {
        final Block block = getBlock(pos);
        faceBlock(pos);
        final Minecraft mc = AutoMine.mc;
        Minecraft.getMinecraft().player.swingItem();
        final Minecraft mc2 = AutoMine.mc;
        Minecraft.getMinecraft().playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
    }
    
    public boolean isBlockPosSafe(final BlockPos pos) {
        return this.checkBlockPos(pos, 10);
    }
    
    public boolean checkBlockPos(final BlockPos pos, final int checkHeight) {
        boolean safe = true;
        boolean blockInWay = false;
        int fallDist = 0;
        if (getBlock(pos).getMaterial() == Material.lava || getBlock(pos).getMaterial() == Material.water) {
            return false;
        }
        if (getBlock(pos.up(1)).getMaterial() == Material.lava || getBlock(pos.up(1)).getMaterial() == Material.water) {
            return false;
        }
        if (getBlock(pos.up(2)).getMaterial() == Material.lava || getBlock(pos.up(2)).getMaterial() == Material.water) {
            return false;
        }
        for (int i = 1; i < checkHeight + 1; ++i) {
            final BlockPos pos2 = pos.down(i);
            final Block block = getBlock(pos2);
            if (block.getMaterial() == Material.air) {
                if (!blockInWay) {
                    ++fallDist;
                }
            }
            else {
                if (!blockInWay && (block.getMaterial() == Material.lava || block.getMaterial() == Material.water)) {
                    return false;
                }
                if (!blockInWay) {
                    blockInWay = true;
                }
            }
        }
        if (fallDist > 2) {
            safe = false;
        }
        return safe;
    }
    
    public static Block getBlockRelativeToEntity(final Entity en, final double d) {
        return getBlock(new BlockPos(en.posX, en.posY + d, en.posZ));
    }
    
    public static Block getBlock(final BlockPos pos) {
        final Minecraft mc = AutoMine.mc;
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
    }
    
    public static BlockPos getBlockPosRelativeToEntity(final Entity en, final double d) {
        return new BlockPos(en.posX, en.posY + d, en.posZ);
    }
    
    public static IBlockState getBlockState(final BlockPos blockPos) {
        final Minecraft mc = AutoMine.mc;
        return Minecraft.getMinecraft().world.getBlockState(blockPos);
    }
    
    public static ArrayList<EntityItem> getNearbyItems(final int range) {
        final ArrayList<EntityItem> eList = new ArrayList<EntityItem>();
        Minecraft.getMinecraft();
        for (final Object o : Minecraft.getMinecraft().world.getLoadedEntityList()) {
            if (!(o instanceof EntityItem)) {
                continue;
            }
            final EntityItem e = (EntityItem)o;
            Minecraft.getMinecraft();
            if (Minecraft.getMinecraft().player.getDistanceToEntity(e) >= range) {
                continue;
            }
            eList.add(e);
        }
        return eList;
    }
    
    public static void faceEntity(final Entity en) {
        facePos(new Vec3(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));
    }
    
    public static void facePos(final Vec3 vec) {
        final double n = vec.xCoord + 0.5;
        Minecraft.getMinecraft();
        final double diffX = n - Minecraft.getMinecraft().player.posX;
        final double n2 = vec.yCoord + 0.5;
        Minecraft.getMinecraft();
        final double posY = Minecraft.getMinecraft().player.posY;
        Minecraft.getMinecraft();
        final double diffY = n2 - (posY + Minecraft.getMinecraft().player.getEyeHeight());
        final double n3 = vec.zCoord + 0.5;
        Minecraft.getMinecraft();
        final double diffZ = n3 - Minecraft.getMinecraft().player.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        Minecraft.getMinecraft();
        final ClientPlayerEntity player = Minecraft.getMinecraft().player;
        Minecraft.getMinecraft();
        final float rotationYaw = Minecraft.getMinecraft().player.rotationYaw;
        final float n4 = yaw;
        Minecraft.getMinecraft();
        player.rotationYaw = rotationYaw + MathHelper.wrapAngleTo180_float(n4 - Minecraft.getMinecraft().player.rotationYaw);
        Minecraft.getMinecraft();
        final ClientPlayerEntity player2 = Minecraft.getMinecraft().player;
        Minecraft.getMinecraft();
        final float rotationPitch = Minecraft.getMinecraft().player.rotationPitch;
        final float n5 = pitch;
        Minecraft.getMinecraft();
        player2.rotationPitch = rotationPitch + MathHelper.wrapAngleTo180_float(n5 - Minecraft.getMinecraft().player.rotationPitch);
    }
    
    public static boolean isBlockPosAir(final BlockPos blockPos) {
        final Minecraft mc = AutoMine.mc;
        return Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
    }
    
    public static Vec3 getVec3(final BlockPos blockPos) {
        return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    public static float[] getFacePos(final Vec3 vec) {
        final double n = vec.xCoord + 0.5;
        Minecraft.getMinecraft();
        final double diffX = n - Minecraft.getMinecraft().player.posX;
        final double n2 = vec.yCoord + 0.5;
        Minecraft.getMinecraft();
        final double posY = Minecraft.getMinecraft().player.posY;
        Minecraft.getMinecraft();
        final double diffY = n2 - (posY + Minecraft.getMinecraft().player.getEyeHeight());
        final double n3 = vec.zCoord + 0.5;
        Minecraft.getMinecraft();
        final double diffZ = n3 - Minecraft.getMinecraft().player.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        final float[] array = new float[2];
        final int n4 = 0;
        Minecraft.getMinecraft();
        final float rotationYaw = Minecraft.getMinecraft().player.rotationYaw;
        final float n5 = yaw;
        Minecraft.getMinecraft();
        array[n4] = rotationYaw + MathHelper.wrapAngleTo180_float(n5 - Minecraft.getMinecraft().player.rotationYaw);
        final int n6 = 1;
        Minecraft.getMinecraft();
        final float rotationPitch = Minecraft.getMinecraft().player.rotationPitch;
        final float n7 = pitch;
        Minecraft.getMinecraft();
        array[n6] = rotationPitch + MathHelper.wrapAngleTo180_float(n7 - Minecraft.getMinecraft().player.rotationPitch);
        return array;
    }
    
    public static float normalizeAngle(final float angle) {
        return (angle + 360.0f) % 360.0f;
    }
    
    public static void faceBlock(final BlockPos blockPos) {
        facePos(getVec3(blockPos));
    }
}
