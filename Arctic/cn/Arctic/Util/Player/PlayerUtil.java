package cn.Arctic.Util.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Multimap;

import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;


public class PlayerUtil {
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static float[] getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 4.0F;
        return getRotationFromPosition(x, z, y);
    }

    private static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().player.posX;
        double zDiff = z - Minecraft.getMinecraft().player.posZ;
        double yDiff = y - Minecraft.getMinecraft().player.posY - 0.6D;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }
    public static boolean isMoving2() {
   	 return ((mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F));
   }
    public static boolean isMoving() {
        if ((!mc.player.isCollidedHorizontally) && (!mc.player.isSneaking())) {
            return ((mc.player.movementInput.moveForward != 0.0F || mc.player.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }
	public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().player.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().player.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    public static BlockPos getHypixelBlockpos(String str){
    	int val = 89;
    	if(str != null && str.length() > 1){
    		char[] chs = str.toCharArray();
        	
        	int lenght = chs.length;
        	for(int i = 0; i < lenght; i++)
        		val += (int)chs[i] * str.length()* str.length() + (int)str.charAt(0) + (int)str.charAt(1);
        	val/=str.length();
    	}
    	return new BlockPos(val, -val%255, val);
    }
	public static float getDirection() {
        float yaw = Minecraft.getMinecraft().player.rotationYawHead;
        float forward = Minecraft.getMinecraft().player.moveForward;
        float strafe = Minecraft.getMinecraft().player.moveStrafing;
        yaw += (forward < 0.0F ? 180 : 0);
        if (strafe < 0.0F) {
            yaw += (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        if (strafe > 0.0F) {
            yaw -= (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        return yaw * 0.017453292F;
    }

    public static boolean isInWater() {
        return PlayerUtil.mc.world.getBlockState(new BlockPos(PlayerUtil.mc.player.posX, PlayerUtil.mc.player.posY, PlayerUtil.mc.player.posZ)).getBlock().getMaterial() == Material.water;
    }

    public static void toFwd(double speed) {
        float yaw = PlayerUtil.mc.player.rotationYaw * 0.017453292f;
        ClientPlayerEntity player = PlayerUtil.mc.player;
        player.motionX -= (double)MathHelper.sin((float)yaw) * speed;
        ClientPlayerEntity player2 = PlayerUtil.mc.player;
        player2.motionZ += (double)MathHelper.cos((float)yaw) * speed;
    }

    public static void setSpeed(double speed) {
        PlayerUtil.mc.player.motionX = - Math.sin((double)PlayerUtil.getDirection()) * speed;
        PlayerUtil.mc.player.motionZ = Math.cos((double)PlayerUtil.getDirection()) * speed;
    }

    public static double getSpeed() {
        return Math.sqrt((double)(Minecraft.getMinecraft().player.motionX * Minecraft.getMinecraft().player.motionX + Minecraft.getMinecraft().player.motionZ * Minecraft.getMinecraft().player.motionZ));
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        double d;
        ArrayList positions = new ArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        double posX = tpX - mc.player.posX;
        double posY = tpY - (mc.player.posY + (double)mc.player.getEyeHeight() + 1.1);
        double posZ = tpZ - mc.player.posZ;
        float yaw = (float)(Math.atan2((double)posZ, (double)posX) * 180.0 / 3.141592653589793 - 90.0);
        float pitch = (float)((- Math.atan2((double)posY, (double)Math.sqrt((double)(posX * posX + posZ * posZ)))) * 180.0 / 3.141592653589793);
        double tmpX = mc.player.posX;
        double tmpY = mc.player.posY;
        double tmpZ = mc.player.posZ;
        double steps = 1.0;
        for (d = speed; d < PlayerUtil.getDistance(mc.player.posX, mc.player.posY, mc.player.posZ, tpX, tpY, tpZ); d += speed) {
            steps += 1.0;
        }
        for (d = speed; d < PlayerUtil.getDistance(mc.player.posX, mc.player.posY, mc.player.posZ, tpX, tpY, tpZ); d += speed) {
            tmpX = mc.player.posX - Math.sin((double)PlayerUtil.getDirection(yaw)) * d;
            tmpZ = mc.player.posZ + Math.cos((double)PlayerUtil.getDirection(yaw)) * d;
            positions.add((Object)new Vector3f((float)tmpX, (float)(tmpY -= (mc.player.posY - tpY) / steps), (float)tmpZ));
        }
        positions.add((Object)new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.getMinecraft().player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.getMinecraft().player.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (Minecraft.getMinecraft().player.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return MathHelper.sqrt_double((double)(d0 * d0 + d2 * d2 + d3 * d3));
    }

    public static boolean MovementInput() {
        return PlayerUtil.mc.gameSettings.keyBindForward.isKeyDown() || PlayerUtil.mc.gameSettings.keyBindLeft.isKeyDown() || PlayerUtil.mc.gameSettings.keyBindRight.isKeyDown() || PlayerUtil.mc.gameSettings.keyBindBack.isKeyDown();
    }

    public static void blockHit(Entity en, boolean value) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack stack = mc.player.getCurrentEquippedItem();
        if (mc.player.getCurrentEquippedItem() != null && en != null && value && stack.getItem() instanceof ItemSword && (double)mc.player.swingProgress > 0.2) {
            mc.player.getCurrentEquippedItem().useItemRightClick((World)mc.world, (EntityPlayer)mc.player);
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        Iterator iterator;
        Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            double damage;
            Map.Entry entry = (Map.Entry)iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double d = damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            if (attributeModifier.getAmount() > 1.0) {
                return 1.0f + (float)damage;
            }
            return 1.0f;
        }
        return 1.0f;
    }

    public static int bestWeapon(Entity target) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.inventory.currentItem = 0;
        int firstSlot = 0;
        int bestWeapon = -1;
        int j = 1;
        for (int i = 0; i < 9; i = (int)((byte)(i + 1))) {
            mc.player.inventory.currentItem = i;
            ItemStack itemStack = mc.player.getHeldItem();
            if (itemStack == null) continue;
            int itemAtkDamage = (int)PlayerUtil.getItemAtkDamage(itemStack);
            if ((itemAtkDamage = (int)((float)itemAtkDamage + EnchantmentHelper.getModifierForCreature((ItemStack)itemStack, (EnumCreatureAttribute)EnumCreatureAttribute.UNDEFINED))) <= j) continue;
            j = itemAtkDamage;
            bestWeapon = i;
        }
        if (bestWeapon != -1) {
            return bestWeapon;
        }
        return firstSlot;
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            ItemStack itemstack = PlayerUtil.mc.player.inventoryContainer.getSlot(i1).getStack();
            if (itemstack == null || itemstack.getItem() != i) continue;
            PlayerUtil.mc.playerController.windowClick(0, i1, 0, 1, (EntityPlayer)PlayerUtil.mc.player);
            break;
        }
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = PlayerUtil.mc.player.inventory.getStackInSlot(i);
            if (itemstack != null) continue;
            return false;
        }
        return true;
    }
    
    public static Entity raycast(Entity entiy) {
        Entity var2 = mc.player;
        Vec3 var9 = entiy.getPositionVector().add(new Vec3(0, entiy.getEyeHeight(), 0));
        Vec3 var7 = mc.player.getPositionVector().add(new Vec3(0, mc.player.getEyeHeight(), 0));
        Vec3 var10 = null;
        float var11 = 1.0F;
        AxisAlignedBB a = mc.player.getEntityBoundingBox()
                .addCoord(var9.xCoord - var7.xCoord, var9.yCoord - var7.yCoord, var9.zCoord - var7.zCoord)
                .expand(var11, var11, var11);
        List var12 = mc.world.getEntitiesWithinAABBExcludingEntity(var2, a);
        double var13 = 4 + 0.5f;
        Entity b = null;
        for (int var15 = 0; var15 < var12.size(); ++var15) {
            Entity var16 = (Entity) var12.get(var15);
 
            if (var16.canBeCollidedWith()) {
                float var17 = var16.getCollisionBorderSize();
                AxisAlignedBB var18 = var16.getEntityBoundingBox().expand((double) var17, (double) var17,
                        (double) var17);
                MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
 
                if (var18.isVecInside(var7)) {
                    if (0.0D < var13 || var13 == 0.0D) {
                        b = var16;
                        var10 = var19 == null ? var7 : var19.hitVec;
                        var13 = 0.0D;
                    }
                } else if (var19 != null) {
                    double var20 = var7.distanceTo(var19.hitVec);
 
                    if (var20 < var13 || var13 == 0.0D) {
                        b = var16;
                        var10 = var19.hitVec;
                        var13 = var20;
                    }
                }
            }
        }
        return b;
    }
    
    public static Vec3 getLook(float p_174806_1_, float p_174806_2_) {
        float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292F - 3.1415927F);
        float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292F);
        float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292F);
        return new Vec3(var4 * var5, var6, var3 * var5);
    }
	public static void tellPlayer(String string) {
		mc.player.addChatMessage(new ChatComponentText(string));
		
	}

	public static boolean isOnGround(double d) {
		// TODO Auto-generated method stub
		return false;
	}
    public static double getDistanceToFall() {
        double distance = 0.0;
        double i2 = Minecraft.getMinecraft().player.posY;
        while (i2 > 0.0) {
            if (i2 < 0.0) break;
            Block block = BlockUtils.getBlock(new BlockPos(Minecraft.getMinecraft().player.posX, i2, Minecraft.getMinecraft().player.posZ));
            if (block.getMaterial() != Material.air && block.isCollidable() && (block.isFullBlock() || block instanceof BlockSlab || block instanceof BlockBarrier || block instanceof BlockStairs || block instanceof BlockGlass || block instanceof BlockStainedGlass)) {
                if (block instanceof BlockSlab) {
                    i2 -= 0.5;
                }
                distance = i2;
                break;
            }
            i2 -= 0.1;
        }
        return Minecraft.getMinecraft().player.posY - distance;
    }

    public static boolean isBlockUnder() {
        ClientPlayerEntity player = PlayerUtil.mc.player;
        WorldClient world = PlayerUtil.mc.world;
        AxisAlignedBB pBb = player.getEntityBoundingBox();
        double height = player.posY + (double)player.getEyeHeight();
        int offset = 0;
        while ((double)offset < height) {
            if (!world.getCollidingBoundingBoxes(player, pBb.offset(0.0, -offset, 0.0)).isEmpty()) {
                return true;
            }
            offset += 2;
        }
        return false;
    }

    public static boolean isInsideBlock() {
        ClientPlayerEntity player = PlayerUtil.mc.player;
        WorldClient world = PlayerUtil.mc.world;
        AxisAlignedBB bb = player.getEntityBoundingBox();
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                    AxisAlignedBB boundingBox;
                    Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block == null || block instanceof BlockAir || (boundingBox = block.getCollisionBoundingBox(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) == null || !player.getEntityBoundingBox().intersectsWith(boundingBox)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isHoldingSword() {
        return PlayerUtil.mc.player.getCurrentEquippedItem() != null && PlayerUtil.mc.player.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        final Minecraft mc = PlayerUtil.mc;
        if (Minecraft.getMinecraft().player.isPotionActive(Potion.moveSpeed)) {
            final Minecraft mc2 = PlayerUtil.mc;
            final int amplifier = Minecraft.getMinecraft().player.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static boolean isHoldingFood(Item item) {
        return !((item instanceof ItemSword) || (item instanceof ItemBow));
    }

    public static float getMaxFallDist() {
        final PotionEffect potioneffect = PlayerUtil.mc.player.getActivePotionEffect(Potion.jump);
        final int f = (potioneffect != null) ? (potioneffect.getAmplifier() + 1) : 0;
        return (float)(PlayerUtil.mc.player.getMaxFallHeight() + f);
    }

    public static boolean isOnHypixel() {
        return !mc.isSingleplayer() && PlayerUtil.mc.getCurrentServerData().serverIP.contains("hypixel");
    }

    public static int getJumpEffect() {
        return PlayerUtil.mc.player.isPotionActive(Potion.jump) ? (PlayerUtil.mc.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) : 0;
    }

    public static void setSpeed(EventMove moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (pseudoForward == 0.0 && pseudoStrafe == 0.0) {
        	moveEvent.setZ(0.0);
        	moveEvent.setX(0.0);
        }
        else {
            if (pseudoForward != 0.0) {
                if (pseudoStrafe > 0.0) {
                    yaw = pseudoYaw + ((pseudoForward > 0.0) ? -45 : 45);
                }
                else if (pseudoStrafe < 0.0) {
                    yaw = pseudoYaw + ((pseudoForward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (pseudoForward > 0.0) {
                    forward = 1.0;
                }
                else if (pseudoForward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            moveEvent.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
            moveEvent.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
        }
    }

    public static String getName(final NetworkPlayerInfo networkPlayerInfoIn) {
        return (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }
}

