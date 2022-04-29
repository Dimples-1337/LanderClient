package cn.Noble.Util;

import net.minecraft.client.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.material.*;
import java.util.*;

import cn.Noble.Util.math.RotationUtil;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.enchantment.*;
import net.minecraft.potion.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.client.multiplayer.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

public class BlockUtils
{
    static double x;
    static double y;
    static double z;
    static double xPreEn;
    static double yPreEn;
    static double zPreEn;
    static double xPre;
    static double yPre;
    static double zPre;
    static Minecraft mc;
    
    public static float[] getFacingRotations(final int x2, final int y2, final int z2, final EnumFacing facing) {
        final Minecraft mc = BlockUtils.mc;
        final EntitySnowball temp = new EntitySnowball(mc.world);
        temp.posX = x2 + 0.5;
        temp.posY = y2 + 0.5;
        temp.posZ = z2 + 0.5;
        final EntitySnowball entitySnowball5;
        final EntitySnowball entitySnowball10;
        final EntitySnowball entitySnowball4 = entitySnowball10 = (entitySnowball5 = temp);
        entitySnowball10.posX += facing.getDirectionVec().getX() * 0.25;
        final EntitySnowball entitySnowball7;
        final EntitySnowball entitySnowball11;
        final EntitySnowball entitySnowball6 = entitySnowball11 = (entitySnowball7 = temp);
        entitySnowball11.posY += facing.getDirectionVec().getY() * 0.25;
        final EntitySnowball entitySnowball9;
        final EntitySnowball entitySnowball12;
        final EntitySnowball entitySnowball8 = entitySnowball12 = (entitySnowball9 = temp);
        entitySnowball12.posZ += facing.getDirectionVec().getZ() * 0.25;
        return null;
    }
    
    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        final Minecraft mc = BlockUtils.mc;
        if (getBlockAtPosC(Minecraft.getMinecraft().player, 0.30000001192092896, 0.10000000149011612, 0.30000001192092896).getMaterial().isLiquid()) {
            final Minecraft mc2 = BlockUtils.mc;
            if (getBlockAtPosC(Minecraft.getMinecraft().player, -0.30000001192092896, 0.10000000149011612, -0.30000001192092896).getMaterial().isLiquid()) {
                onLiquid = true;
            }
        }
        return onLiquid;
    }
    
    public static boolean isOnLadder() {
        final Minecraft mc = BlockUtils.mc;
        if (Minecraft.getMinecraft().player == null) {
            return false;
        }
        boolean onLadder = false;
        final Minecraft mc2 = BlockUtils.mc;
        final int y2 = (int)Minecraft.getMinecraft().player.getEntityBoundingBox().offset(0.0, 1.0, 0.0).minY;
        final Minecraft mc3 = BlockUtils.mc;
        int x2 = MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().minX);
        while (true) {
            final int n = x2;
            final Minecraft mc4 = BlockUtils.mc;
            if (n >= MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().maxX) + 1) {
                if (!onLadder) {
                    final Minecraft mc5 = BlockUtils.mc;
                    if (!Minecraft.getMinecraft().player.isOnLadder()) {
                        return false;
                    }
                }
                return true;
            }
            final Minecraft mc6 = BlockUtils.mc;
            int z2 = MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().minZ);
            while (true) {
                final int n2 = z2;
                final Minecraft mc7 = BlockUtils.mc;
                if (n2 >= MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().maxZ) + 1) {
                    ++x2;
                    break;
                }
                final Block block = getBlock(x2, y2, z2);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                        return false;
                    }
                    onLadder = true;
                }
                ++z2;
            }
        }
    }
    
    public static boolean isOnIce() {
        final Minecraft mc = BlockUtils.mc;
        if (Minecraft.getMinecraft().player == null) {
            return false;
        }
        boolean onIce = false;
        final Minecraft mc2 = BlockUtils.mc;
        final int y2 = (int)Minecraft.getMinecraft().player.getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;
        final Minecraft mc3 = BlockUtils.mc;
        int x2 = MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().minX);
        while (true) {
            final int n = x2;
            final Minecraft mc4 = BlockUtils.mc;
            if (n >= MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().maxX) + 1) {
                return onIce;
            }
            final Minecraft mc5 = BlockUtils.mc;
            int z2 = MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().minZ);
            while (true) {
                final int n2 = z2;
                final Minecraft mc6 = BlockUtils.mc;
                if (n2 >= MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().maxZ) + 1) {
                    ++x2;
                    break;
                }
                final Block block = getBlock(x2, y2, z2);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockIce) && !(block instanceof BlockPackedIce)) {
                        return false;
                    }
                    onIce = true;
                }
                ++z2;
            }
        }
    }
    
    public static boolean isReplaceable(final BlockPos blockPosition) {
        return getMaterial(blockPosition).isReplaceable();
    }
    
    public static Material getMaterial(final BlockPos blockPosition) {
        return getBlock(blockPosition).getMaterial();
    }
    
    public boolean isInsideBlock() {
        final Minecraft mc = BlockUtils.mc;
        int x2 = MathHelper.floor_double(Minecraft.getMinecraft().player.boundingBox.minX);
        while (true) {
            final int n = x2;
            final Minecraft mc2 = BlockUtils.mc;
            if (n >= MathHelper.floor_double(Minecraft.getMinecraft().player.boundingBox.maxX) + 1) {
                return false;
            }
            final Minecraft mc3 = BlockUtils.mc;
            int y2 = MathHelper.floor_double(Minecraft.getMinecraft().player.boundingBox.minY);
            while (true) {
                final int n2 = y2;
                final Minecraft mc4 = BlockUtils.mc;
                if (n2 >= MathHelper.floor_double(Minecraft.getMinecraft().player.boundingBox.maxY) + 1) {
                    ++x2;
                    break;
                }
                final Minecraft mc5 = BlockUtils.mc;
                int z2 = MathHelper.floor_double(Minecraft.getMinecraft().player.boundingBox.minZ);
                while (true) {
                    final int n3 = z2;
                    final Minecraft mc6 = BlockUtils.mc;
                    if (n3 >= MathHelper.floor_double(Minecraft.getMinecraft().player.boundingBox.maxZ) + 1) {
                        ++y2;
                        break;
                    }
                    final Minecraft mc7 = BlockUtils.mc;
                    final Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        final Block block2 = block;
                        final Minecraft mc8 = BlockUtils.mc;
                        final WorldClient world = Minecraft.getMinecraft().world;
                        final BlockPos pos = new BlockPos(x2, y2, z2);
                        final Minecraft mc9 = BlockUtils.mc;
                        final AxisAlignedBB boundingBox;
                        if ((boundingBox = block2.getCollisionBoundingBox(world, pos, Minecraft.getMinecraft().world.getBlockState(new BlockPos(x2, y2, z2)))) != null) {
                            final Minecraft mc10 = BlockUtils.mc;
                            if (Minecraft.getMinecraft().player.boundingBox.intersectsWith(boundingBox)) {
                                return true;
                            }
                        }
                    }
                    ++z2;
                }
            }
        }
    }
    
    public static boolean isBlockUnderPlayer(final Material material, final float height) {
        final Minecraft mc = BlockUtils.mc;
        if (getBlockAtPosC(Minecraft.getMinecraft().player, 0.3100000023841858, height, 0.3100000023841858).getMaterial() == material) {
            final Minecraft mc2 = BlockUtils.mc;
            if (getBlockAtPosC(Minecraft.getMinecraft().player, -0.3100000023841858, height, -0.3100000023841858).getMaterial() == material) {
                final Minecraft mc3 = BlockUtils.mc;
                if (getBlockAtPosC(Minecraft.getMinecraft().player, -0.3100000023841858, height, 0.3100000023841858).getMaterial() == material) {
                    final Minecraft mc4 = BlockUtils.mc;
                    if (getBlockAtPosC(Minecraft.getMinecraft().player, 0.3100000023841858, height, -0.3100000023841858).getMaterial() == material) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x2, final double y2, final double z2) {
        return getBlock(new BlockPos(inPlayer.posX - x2, inPlayer.posY - y2, inPlayer.posZ - z2));
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
    }
    
    public static Block getBlockAbovePlayer(final EntityPlayer inPlayer, final double height) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY + inPlayer.height + height, inPlayer.posZ));
    }
    
    public static Block getBlock(final int x2, final int y2, final int z2) {
        final Minecraft mc = BlockUtils.mc;
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
    }
    
    public static Block getBlock(final BlockPos pos) {
        final Minecraft mc = BlockUtils.mc;
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
    }
    
    private static void preInfiniteReach(final double range, final double maxXZTP, final double maxYTP, final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions, final Vec3 targetPos, final boolean tpStraight, final boolean up2, final boolean attack, final boolean tpUpOneBlock, final boolean sneaking) {
    }
    
    private static void postInfiniteReach() {
    }
    
    public static Block getBlock(final double x2, final double y2, final double z2) {
        final Minecraft mc = BlockUtils.mc;
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos((int)x2, (int)y2, (int)z2)).getBlock();
    }
    
    public static boolean infiniteReach(final double range, final double maxXZTP, final double maxYTP, final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions, final EntityLivingBase en2) {
        int ind = 0;
        BlockUtils.xPreEn = en2.posX;
        BlockUtils.yPreEn = en2.posY;
        BlockUtils.zPreEn = en2.posZ;
        final Minecraft mc = BlockUtils.mc;
        BlockUtils.xPre = Minecraft.getMinecraft().player.posX;
        final Minecraft mc2 = BlockUtils.mc;
        BlockUtils.yPre = Minecraft.getMinecraft().player.posY;
        final Minecraft mc3 = BlockUtils.mc;
        BlockUtils.zPre = Minecraft.getMinecraft().player.posZ;
        boolean attack = true;
        boolean up2 = false;
        final boolean tpUpOneBlock = false;
        final boolean hit = false;
        final boolean tpStraight = false;
        positions.clear();
        positionsBack.clear();
        final double step = maxXZTP / range;
        int steps = 0;
        for (int i2 = 0; i2 < range && maxXZTP * ++steps <= range; ++i2) {}
        MovingObjectPosition rayTrace = null;
        MovingObjectPosition rayTrace2 = null;
        final Object rayTraceCarpet = null;
        final Minecraft mc4 = BlockUtils.mc;
        final double posX = Minecraft.getMinecraft().player.posX;
        final Minecraft mc5 = BlockUtils.mc;
        final double posY = Minecraft.getMinecraft().player.posY;
        final Minecraft mc6 = BlockUtils.mc;
        Label_0973: {
            if (!rayTraceWide(new Vec3(posX, posY, Minecraft.getMinecraft().player.posZ), new Vec3(en2.posX, en2.posY, en2.posZ), false, false, true)) {
                final Minecraft mc7 = BlockUtils.mc;
                final double posX2 = Minecraft.getMinecraft().player.posX;
                final Minecraft mc8 = BlockUtils.mc;
                final double posY2 = Minecraft.getMinecraft().player.posY;
                final Minecraft mc9 = BlockUtils.mc;
                final double y = posY2 + Minecraft.getMinecraft().player.getEyeHeight();
                final Minecraft mc10 = BlockUtils.mc;
                final Vec3 vec31 = new Vec3(posX2, y, Minecraft.getMinecraft().player.posZ);
                final double posX3 = en2.posX;
                final double posY3 = en2.posY;
                final Minecraft mc11 = BlockUtils.mc;
                if ((rayTrace2 = rayTracePos(vec31, new Vec3(posX3, posY3 + Minecraft.getMinecraft().player.getEyeHeight(), en2.posZ), false, false, true)) == null) {
                    break Label_0973;
                }
            }
            final Minecraft mc12 = BlockUtils.mc;
            final double posX4 = Minecraft.getMinecraft().player.posX;
            final Minecraft mc13 = BlockUtils.mc;
            final double posY4 = Minecraft.getMinecraft().player.posY;
            final Minecraft mc14 = BlockUtils.mc;
            final Vec3 vec32 = new Vec3(posX4, posY4, Minecraft.getMinecraft().player.posZ);
            final double posX5 = en2.posX;
            final Minecraft mc15 = BlockUtils.mc;
            rayTrace = rayTracePos(vec32, new Vec3(posX5, Minecraft.getMinecraft().player.posY, en2.posZ), false, false, true);
            if (rayTrace == null) {
                final Minecraft mc16 = BlockUtils.mc;
                final double posX6 = Minecraft.getMinecraft().player.posX;
                final Minecraft mc17 = BlockUtils.mc;
                final double posY5 = Minecraft.getMinecraft().player.posY;
                final Minecraft mc18 = BlockUtils.mc;
                final double y2 = posY5 + Minecraft.getMinecraft().player.getEyeHeight();
                final Minecraft mc19 = BlockUtils.mc;
                final Vec3 vec33 = new Vec3(posX6, y2, Minecraft.getMinecraft().player.posZ);
                final double posX7 = en2.posX;
                final Minecraft mc20 = BlockUtils.mc;
                final double posY6 = Minecraft.getMinecraft().player.posY;
                final Minecraft mc21 = BlockUtils.mc;
                if ((rayTrace2 = rayTracePos(vec33, new Vec3(posX7, posY6 + Minecraft.getMinecraft().player.getEyeHeight(), en2.posZ), false, false, true)) == null) {
                    final Minecraft mc22 = BlockUtils.mc;
                    final double posX8 = Minecraft.getMinecraft().player.posX;
                    final Minecraft mc23 = BlockUtils.mc;
                    final double posY7 = Minecraft.getMinecraft().player.posY;
                    final Minecraft mc24 = BlockUtils.mc;
                    final MovingObjectPosition ent = rayTracePos(new Vec3(posX8, posY7, Minecraft.getMinecraft().player.posZ), new Vec3(en2.posX, en2.posY, en2.posZ), false, false, false);
                    if (ent != null && ent.entityHit == null) {
                        final Minecraft mc25 = BlockUtils.mc;
                        BlockUtils.y = Minecraft.getMinecraft().player.posY;
                        final Minecraft mc26 = BlockUtils.mc;
                        BlockUtils.yPreEn = Minecraft.getMinecraft().player.posY;
                        break Label_0973;
                    }
                    final Minecraft mc27 = BlockUtils.mc;
                    BlockUtils.y = Minecraft.getMinecraft().player.posY;
                    BlockUtils.yPreEn = en2.posY;
                    break Label_0973;
                }
            }
            MovingObjectPosition trace = null;
            if (rayTrace == null) {
                trace = rayTrace2;
            }
            if (rayTrace2 == null) {
                trace = rayTrace;
            }
            if (trace != null) {
                if (trace.getBlockPos() == null) {
                    attack = false;
                    return false;
                }
                boolean fence = false;
                final BlockPos target = trace.getBlockPos();
                up2 = true;
                BlockUtils.y = target.up().getY();
                BlockUtils.yPreEn = target.up().getY();
                Block lastBlock = null;
                Boolean found = false;
                for (int j2 = 0; j2 < maxYTP; ++j2) {
                    final Minecraft mc28 = BlockUtils.mc;
                    final double posX9 = Minecraft.getMinecraft().player.posX;
                    final double y3 = target.getY() + j2;
                    final Minecraft mc29 = BlockUtils.mc;
                    final MovingObjectPosition tr2 = rayTracePos(new Vec3(posX9, y3, Minecraft.getMinecraft().player.posZ), new Vec3(en2.posX, target.getY() + j2, en2.posZ), false, false, true);
                    if (tr2 != null && tr2.getBlockPos() != null) {
                        final BlockPos blockPos = tr2.getBlockPos();
                        final Minecraft mc30 = BlockUtils.mc;
                        final Block block = Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
                        if (block.getMaterial() == Material.air) {
                            fence = (lastBlock instanceof BlockFence);
                            BlockUtils.y = target.getY() + j2;
                            BlockUtils.yPreEn = target.getY() + j2;
                            if (fence) {
                                ++BlockUtils.y;
                                ++BlockUtils.yPreEn;
                                if (j2 + 1 > maxYTP) {
                                    found = false;
                                    break;
                                }
                            }
                            found = true;
                            break;
                        }
                        lastBlock = block;
                    }
                }
                final Minecraft mc31 = BlockUtils.mc;
                final double difX = Minecraft.getMinecraft().player.posX - BlockUtils.xPreEn;
                final Minecraft mc32 = BlockUtils.mc;
                final double difZ = Minecraft.getMinecraft().player.posZ - BlockUtils.zPreEn;
                final double divider = step * 0.0;
                if (!found) {
                    attack = false;
                    return false;
                }
            }
        }
        if (!attack) {
            return false;
        }
        for (int k2 = 0; k2 < steps; ++k2) {
            ++ind;
            if (k2 == 1 && up2) {
                final Minecraft mc33 = BlockUtils.mc;
                BlockUtils.x = Minecraft.getMinecraft().player.posX;
                BlockUtils.y = BlockUtils.yPreEn;
                final Minecraft mc34 = BlockUtils.mc;
                BlockUtils.z = Minecraft.getMinecraft().player.posZ;
                sendPacket(false, positionsBack, positions);
            }
            if (k2 != steps - 1) {
                final Minecraft mc35 = BlockUtils.mc;
                final double difX2 = Minecraft.getMinecraft().player.posX - BlockUtils.xPreEn;
                final Minecraft mc36 = BlockUtils.mc;
                final double difY = Minecraft.getMinecraft().player.posY - BlockUtils.yPreEn;
                final Minecraft mc37 = BlockUtils.mc;
                final double difZ2 = Minecraft.getMinecraft().player.posZ - BlockUtils.zPreEn;
                final double divider2 = step * k2;
                final Minecraft mc38 = BlockUtils.mc;
                BlockUtils.x = Minecraft.getMinecraft().player.posX - difX2 * divider2;
                final Minecraft mc39 = BlockUtils.mc;
                BlockUtils.y = Minecraft.getMinecraft().player.posY - difY * (up2 ? 1.0 : divider2);
                final Minecraft mc40 = BlockUtils.mc;
                BlockUtils.z = Minecraft.getMinecraft().player.posZ - difZ2 * divider2;
                sendPacket(false, positionsBack, positions);
            }
            else {
                final Minecraft mc41 = BlockUtils.mc;
                final double difX2 = Minecraft.getMinecraft().player.posX - BlockUtils.xPreEn;
                final Minecraft mc42 = BlockUtils.mc;
                final double difY = Minecraft.getMinecraft().player.posY - BlockUtils.yPreEn;
                final Minecraft mc43 = BlockUtils.mc;
                final double difZ2 = Minecraft.getMinecraft().player.posZ - BlockUtils.zPreEn;
                final double divider2 = step * k2;
                final Minecraft mc44 = BlockUtils.mc;
                BlockUtils.x = Minecraft.getMinecraft().player.posX - difX2 * divider2;
                final Minecraft mc45 = BlockUtils.mc;
                BlockUtils.y = Minecraft.getMinecraft().player.posY - difY * (up2 ? 1.0 : divider2);
                final Minecraft mc46 = BlockUtils.mc;
                BlockUtils.z = Minecraft.getMinecraft().player.posZ - difZ2 * divider2;
                sendPacket(false, positionsBack, positions);
                final double xDist = BlockUtils.x - BlockUtils.xPreEn;
                final double zDist = BlockUtils.z - BlockUtils.zPreEn;
                final double yDist = BlockUtils.y - en2.posY;
                final double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (dist > 4.0) {
                    BlockUtils.x = BlockUtils.xPreEn;
                    BlockUtils.y = BlockUtils.yPreEn;
                    BlockUtils.z = BlockUtils.zPreEn;
                    sendPacket(false, positionsBack, positions);
                }
                else if (dist > 0.05 && up2) {
                    BlockUtils.x = BlockUtils.xPreEn;
                    BlockUtils.y = BlockUtils.yPreEn;
                    BlockUtils.z = BlockUtils.zPreEn;
                    sendPacket(false, positionsBack, positions);
                }
                if (Math.abs(yDist) < maxYTP) {
                    final Minecraft mc47 = BlockUtils.mc;
                    if (Minecraft.getMinecraft().player.getDistanceToEntity(en2) >= 4.0f) {
                        BlockUtils.x = BlockUtils.xPreEn;
                        BlockUtils.y = en2.posY;
                        BlockUtils.z = BlockUtils.zPreEn;
                        sendPacket(false, positionsBack, positions);
                        continue;
                    }
                }
                attack = false;
            }
        }
        for (int k2 = positions.size() - 2; k2 > -1; --k2) {
            BlockUtils.x = positions.get(k2).xCoord;
            BlockUtils.y = positions.get(k2).yCoord;
            BlockUtils.z = positions.get(k2).zCoord;
            sendPacket(false, positionsBack, positions);
        }
        final Minecraft mc48 = BlockUtils.mc;
        BlockUtils.x = Minecraft.getMinecraft().player.posX;
        final Minecraft mc49 = BlockUtils.mc;
        BlockUtils.y = Minecraft.getMinecraft().player.posY;
        final Minecraft mc50 = BlockUtils.mc;
        BlockUtils.z = Minecraft.getMinecraft().player.posZ;
        sendPacket(false, positionsBack, positions);
        if (!attack) {
            positions.clear();
            positionsBack.clear();
            return false;
        }
        return true;
    }
    
    public static double normalizeAngle(final double angle) {
        return (angle + 360.0) % 360.0;
    }
    
    public static float normalizeAngle(final float angle) {
        return (angle + 360.0f) % 360.0f;
    }
    
    public static void sendPacket(final boolean goingBack, final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions) {
        final C03PacketPlayer.C04PacketPlayerPosition playerPacket = new C03PacketPlayer.C04PacketPlayerPosition(BlockUtils.x, BlockUtils.y, BlockUtils.z, true);
        BlockUtils.mc.getNetHandler().getNetworkManager().sendPacket(playerPacket);
        if (goingBack) {
            positionsBack.add(new Vec3(BlockUtils.x, BlockUtils.y, BlockUtils.z));
            return;
        }
        positions.add(new Vec3(BlockUtils.x, BlockUtils.y, BlockUtils.z));
    }
    
    public static void attackInf(final EntityLivingBase entity) {
        final Minecraft mc = BlockUtils.mc;
        Minecraft.getMinecraft().player.swingItem();
        final Minecraft mc2 = BlockUtils.mc;
        final float sharpLevel = EnchantmentHelper.getModifierForCreature(Minecraft.getMinecraft().player.getHeldItem(), entity.getCreatureAttribute());
        final Minecraft mc3 = BlockUtils.mc;
        boolean b = false;
        Label_0116: {
            if (Minecraft.getMinecraft().player.fallDistance > 0.0f) {
                final Minecraft mc4 = BlockUtils.mc;
                if (!Minecraft.getMinecraft().player.onGround) {
                    final Minecraft mc5 = BlockUtils.mc;
                    if (!Minecraft.getMinecraft().player.isOnLadder()) {
                        final Minecraft mc6 = BlockUtils.mc;
                        if (!Minecraft.getMinecraft().player.isInWater()) {
                            final Minecraft mc7 = BlockUtils.mc;
                            if (!Minecraft.getMinecraft().player.isPotionActive(Potion.blindness)) {
                                final Minecraft mc8 = BlockUtils.mc;
                                if (Minecraft.getMinecraft().player.ridingEntity == null) {
                                    b = true;
                                    break Label_0116;
                                }
                            }
                        }
                    }
                }
            }
            b = false;
        }
        final boolean vanillaCrit = b;
        final Minecraft mc9 = BlockUtils.mc;
        Minecraft.getMinecraft().player.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (sharpLevel > 0.0f) {
            final Minecraft mc10 = BlockUtils.mc;
            Minecraft.getMinecraft().player.onEnchantmentCritical(entity);
        }
    }
    
    public static void attackinfGuardian(final EntityLivingBase entity) {
        final Minecraft mc = BlockUtils.mc;
        final float sharpLevel = EnchantmentHelper.getModifierForCreature(Minecraft.getMinecraft().player.getHeldItem(), entity.getCreatureAttribute());
        final Minecraft mc2 = BlockUtils.mc;
        boolean b = false;
        Label_0106: {
            if (Minecraft.getMinecraft().player.fallDistance > 0.0f) {
                final Minecraft mc3 = BlockUtils.mc;
                if (!Minecraft.getMinecraft().player.onGround) {
                    final Minecraft mc4 = BlockUtils.mc;
                    if (!Minecraft.getMinecraft().player.isOnLadder()) {
                        final Minecraft mc5 = BlockUtils.mc;
                        if (!Minecraft.getMinecraft().player.isInWater()) {
                            final Minecraft mc6 = BlockUtils.mc;
                            if (!Minecraft.getMinecraft().player.isPotionActive(Potion.blindness)) {
                                final Minecraft mc7 = BlockUtils.mc;
                                if (Minecraft.getMinecraft().player.ridingEntity == null) {
                                    b = true;
                                    break Label_0106;
                                }
                            }
                        }
                    }
                }
            }
            b = false;
        }
        final boolean vanillaCrit = b;
        final Minecraft mc8 = BlockUtils.mc;
        Minecraft.getMinecraft().player.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (sharpLevel > 0.0f) {
            final Minecraft mc9 = BlockUtils.mc;
            Minecraft.getMinecraft().player.onEnchantmentCritical(entity);
        }
    }
    
    public static float[] getFacePos(final Vec3 vec) {
        final double n2 = vec.xCoord + 0.5;
        Minecraft.getMinecraft();
        final double n9 = n2;
        final Minecraft mc = BlockUtils.mc;
        final double diffX = n9 - Minecraft.getMinecraft().player.posX;
        final double n3 = vec.yCoord + 0.5;
        Minecraft.getMinecraft();
        final Minecraft mc2 = BlockUtils.mc;
        final double posY = Minecraft.getMinecraft().player.posY;
        Minecraft.getMinecraft();
        final double n10 = n3;
        final double n11 = posY;
        final Minecraft mc3 = BlockUtils.mc;
        final double diffY = n10 - (n11 + Minecraft.getMinecraft().player.getEyeHeight());
        final double n4 = vec.zCoord + 0.5;
        Minecraft.getMinecraft();
        final double n12 = n4;
        final Minecraft mc4 = BlockUtils.mc;
        final double diffZ = n12 - Minecraft.getMinecraft().player.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        final float[] array = new float[2];
        final boolean n5 = false;
        Minecraft.getMinecraft();
        final Minecraft mc5 = BlockUtils.mc;
        final float rotationYaw = Minecraft.getMinecraft().player.rotationYaw;
        final float n6 = yaw;
        Minecraft.getMinecraft();
        final float[] array2 = array;
        final int n13 = 0;
        final float n14 = rotationYaw;
        final float n15 = n6;
        final Minecraft mc6 = BlockUtils.mc;
        array2[n13] = n14 + MathHelper.wrapAngleTo180_float(n15 - Minecraft.getMinecraft().player.rotationYaw);
        final boolean n7 = true;
        Minecraft.getMinecraft();
        final Minecraft mc7 = BlockUtils.mc;
        final float rotationPitch = Minecraft.getMinecraft().player.rotationPitch;
        final float n8 = pitch;
        Minecraft.getMinecraft();
        final float[] array3 = array;
        final int n16 = 1;
        final float n17 = rotationPitch;
        final float n18 = n8;
        final Minecraft mc8 = BlockUtils.mc;
        array3[n16] = n17 + MathHelper.wrapAngleTo180_float(n18 - Minecraft.getMinecraft().player.rotationPitch);
        return array;
    }
    
    public static float[] getFacePosRemote(final Vec3 src, final Vec3 dest) {
        final double diffX = dest.xCoord - src.xCoord;
        final double diffY = dest.yCoord - src.yCoord;
        final double diffZ = dest.zCoord - src.zCoord;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    public static MovingObjectPosition rayTracePos(final Vec3 vec31, final Vec3 vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
        final float[] rots = getFacePosRemote(vec32, vec31);
        final float yaw = rots[0];
        final double angleA = Math.toRadians(normalizeAngle(yaw));
        final double angleB = Math.toRadians(normalizeAngle(yaw) + 180.0f);
        final double size = 2.1;
        final double size2 = 2.1;
        final Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * 2.1);
        final Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * 2.1);
        final Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * 2.1);
        final Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * 2.1);
        final Minecraft mc = BlockUtils.mc;
        final MovingObjectPosition trace1 = Minecraft.getMinecraft().world.rayTraceBlocks(left, left2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final Minecraft mc2 = BlockUtils.mc;
        final MovingObjectPosition trace2 = Minecraft.getMinecraft().world.rayTraceBlocks(vec31, vec32, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final Minecraft mc3 = BlockUtils.mc;
        final MovingObjectPosition trace3 = Minecraft.getMinecraft().world.rayTraceBlocks(right, right2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final MovingObjectPosition trace4 = null;
        final MovingObjectPosition trace5 = null;
        if (trace2 != null || trace1 != null || trace3 != null || trace4 != null || trace5 != null) {
            if (returnLastUncollidableBlock) {
                if (trace5 != null && (getBlock(trace5.getBlockPos()).getMaterial() != Material.air || trace5.entityHit != null)) {
                    return trace5;
                }
                if (trace4 != null && (getBlock(trace4.getBlockPos()).getMaterial() != Material.air || trace4.entityHit != null)) {
                    return trace4;
                }
                if (trace3 != null && (getBlock(trace3.getBlockPos()).getMaterial() != Material.air || trace3.entityHit != null)) {
                    return trace3;
                }
                if (trace1 != null && (getBlock(trace1.getBlockPos()).getMaterial() != Material.air || trace1.entityHit != null)) {
                    return trace1;
                }
                if (trace2 != null && (getBlock(trace2.getBlockPos()).getMaterial() != Material.air || trace2.entityHit != null)) {
                    return trace2;
                }
            }
            else {
                if (trace5 != null) {
                    return trace5;
                }
                if (trace4 != null) {
                    return trace4;
                }
                if (trace3 != null) {
                    return trace3;
                }
                if (trace1 != null) {
                    return trace1;
                }
                if (trace2 != null) {
                    return trace2;
                }
            }
        }
        if (trace2 != null) {
            return trace2;
        }
        if (trace3 != null) {
            return trace3;
        }
        if (trace1 != null) {
            return trace1;
        }
        if (trace5 != null) {
            return trace5;
        }
        if (trace4 == null) {
            return null;
        }
        return trace4;
    }
    
    public static boolean rayTraceWide(final Vec3 vec31, final Vec3 vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
        float yaw = getFacePosRemote(vec32, vec31)[0];
        yaw = normalizeAngle(yaw);
        yaw += 180.0f;
        yaw = MathHelper.wrapAngleTo180_float(yaw);
        final double angleA = Math.toRadians(yaw);
        final double angleB = Math.toRadians(yaw + 180.0f);
        final double size = 2.1;
        final double size2 = 2.1;
        final Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * 2.1);
        final Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * 2.1);
        final Vec3 leftA = new Vec3(vec31.xCoord + Math.cos(angleA) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 rightA = new Vec3(vec31.xCoord + Math.cos(angleB) * 2.1, vec31.yCoord, vec31.zCoord + Math.sin(angleB) * 2.1);
        final Vec3 left2A = new Vec3(vec32.xCoord + Math.cos(angleA) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleA) * 2.1);
        final Vec3 right2A = new Vec3(vec32.xCoord + Math.cos(angleB) * 2.1, vec32.yCoord, vec32.zCoord + Math.sin(angleB) * 2.1);
        final Minecraft mc = BlockUtils.mc;
        final MovingObjectPosition trace1 = Minecraft.getMinecraft().world.rayTraceBlocks(left, left2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final Minecraft mc2 = BlockUtils.mc;
        final MovingObjectPosition trace2 = Minecraft.getMinecraft().world.rayTraceBlocks(vec31, vec32, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final Minecraft mc3 = BlockUtils.mc;
        final MovingObjectPosition trace3 = Minecraft.getMinecraft().world.rayTraceBlocks(right, right2, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final MovingObjectPosition trace4 = null;
        final MovingObjectPosition trace5 = null;
        if (returnLastUncollidableBlock) {
            return (trace1 != null && getBlock(trace1.getBlockPos()).getMaterial() != Material.air) || (trace2 != null && getBlock(trace2.getBlockPos()).getMaterial() != Material.air) || (trace3 != null && getBlock(trace3.getBlockPos()).getMaterial() != Material.air) || (trace4 != null && getBlock(trace4.getBlockPos()).getMaterial() != Material.air) || (trace5 != null && getBlock(trace5.getBlockPos()).getMaterial() != Material.air);
        }
        return trace1 != null || trace2 != null || trace3 != null || trace5 != null || trace4 != null;
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static IBlockState getState(final BlockPos pos) {
        final Minecraft mc = BlockUtils.mc;
        return Minecraft.getMinecraft().world.getBlockState(pos);
    }
    
    private static PlayerControllerMP getPlayerController() {
        Minecraft.getMinecraft();
        final Minecraft mc = BlockUtils.mc;
        return Minecraft.getMinecraft().playerController;
    }
    
    public static void processRightClickBlock(final BlockPos pos, final EnumFacing side, final Vec3 hitVec) {
        getPlayerController();
    }
    
    public static boolean isInLiquid() {
        final Minecraft mc = BlockUtils.mc;
        if (Minecraft.getMinecraft().player.isInWater()) {
            return true;
        }
        boolean var1 = false;
        final Minecraft mc2 = BlockUtils.mc;
        final int var2 = (int)Minecraft.getMinecraft().player.getEntityBoundingBox().minY;
        final Minecraft mc3 = BlockUtils.mc;
        int var3 = MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().minX);
        while (true) {
            final int n = var3;
            final Minecraft mc4 = BlockUtils.mc;
            if (n >= MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().maxX) + 1) {
                return var1;
            }
            final Minecraft mc5 = BlockUtils.mc;
            int var4 = MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().minZ);
            while (true) {
                final int n2 = var4;
                final Minecraft mc6 = BlockUtils.mc;
                if (n2 >= MathHelper.floor_double(Minecraft.getMinecraft().player.getEntityBoundingBox().maxZ) + 1) {
                    ++var3;
                    break;
                }
                final Minecraft mc7 = BlockUtils.mc;
                final Block var5 = Minecraft.getMinecraft().world.getBlockState(new BlockPos(var3, var2, var4)).getBlock();
                if (var5 != null && var5.getMaterial() != Material.air) {
                    if (!(var5 instanceof BlockLiquid)) {
                        return false;
                    }
                    var1 = true;
                }
                ++var4;
            }
        }
    }
    
    public static void updateTool(final BlockPos pos) {
        final Minecraft mc = BlockUtils.mc;
        final Block block = Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            final Minecraft mc2 = BlockUtils.mc;
            final ItemStack itemStack = Minecraft.getMinecraft().player.inventory.mainInventory[i];
            if (itemStack != null) {
                if (itemStack.getStrVsBlock(block) > strength) {
                    strength = itemStack.getStrVsBlock(block);
                    bestItemIndex = i;
                }
            }
        }
        if (bestItemIndex != -1) {
            final Minecraft mc3 = BlockUtils.mc;
            Minecraft.getMinecraft().player.inventory.currentItem = bestItemIndex;
        }
    }
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
    }
}
