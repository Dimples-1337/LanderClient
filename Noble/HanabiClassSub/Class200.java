package HanabiClassSub;

import net.minecraft.client.*;
import net.minecraft.potion.*;
import net.minecraft.block.material.*;
import net.minecraft.client.entity.*;
import net.minecraft.block.*;
import org.lwjgl.util.vector.*;
import net.minecraft.world.*;
import net.minecraft.entity.ai.attributes.*;
import com.google.common.collect.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Class200
{
    private static Minecraft mc;
    
    public void portMove(final float n, final float n2, final float n3) {
        Class200.mc.player.setPosition(-Math.sin(Math.toRadians(n)) * n2 + Class200.mc.player.posX, n3 + Class200.mc.player.posY, Math.cos(Math.toRadians(n)) * n2 + Class200.mc.player.posZ);
    }
    
    public static double getBaseMoveSpeed() {
        if (Class334.password.length() < 32) {
           
        }
        double n = 0.2873;
        if (Minecraft.getMinecraft().player.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (Minecraft.getMinecraft().player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }
    
    public static float getDirection() {
        float rotationYaw = Class200.mc.player.rotationYaw;
        if (Class200.mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float n = 1.0f;
        if (Class200.mc.player.moveForward < 0.0f) {
            n = -0.5f;
        }
        else if (Class200.mc.player.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (Class200.mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (Class200.mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }
    
    public static boolean isInWater() {
        return Class200.mc.world.getBlockState(new BlockPos(Class200.mc.player.posX, Class200.mc.player.posY, Class200.mc.player.posZ)).getBlock().getMaterial() == Material.water;
    }
    
    public static void toFwd(final double n) {
        final float n2 = Class200.mc.player.rotationYaw * 0.017453292f;
        final ClientPlayerEntity player = Class200.mc.player;
        player.motionX -= MathHelper.sin(n2) * n;
        final ClientPlayerEntity player2 = Class200.mc.player;
        player2.motionZ += MathHelper.cos(n2) * n;
    }
    
    public static void setSpeed(final double n) {
        Class200.mc.player.motionX = -(Math.sin(getDirection()) * n);
        Class200.mc.player.motionZ = Math.cos(getDirection()) * n;
    }
    
    public static double getSpeed() {
        if (Class334.password.length() < 32) {
            System.exit(0);
        }
        return Math.sqrt(Minecraft.getMinecraft().player.motionX * Minecraft.getMinecraft().player.motionX + Minecraft.getMinecraft().player.motionZ * Minecraft.getMinecraft().player.motionZ);
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer entityPlayer) {
        return getBlock(new BlockPos(entityPlayer.posX, entityPlayer.posY - 1.0, entityPlayer.posZ));
    }
    
    public static Block getBlock(final BlockPos blockPos) {
        return Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
    }
    
    public static Block getBlockAtPosC(final EntityPlayer entityPlayer, final double n, final double n2, final double n3) {
        return getBlock(new BlockPos(entityPlayer.posX - n, entityPlayer.posY - n2, entityPlayer.posZ - n3));
    }
    
    public static ArrayList<Vector3f> vanillaTeleportPositions(final double n, final double n2, final double n3, final double n4) {
        final ArrayList<Vector3f> list = new ArrayList<Vector3f>();
        final Minecraft getMinecraft = Minecraft.getMinecraft();
        final double n5 = n - getMinecraft.getMinecraft().player.posX;
        final double n6 = n2 - (getMinecraft.getMinecraft().player.posY + getMinecraft.getMinecraft().player.getEyeHeight() + 1.1);
        final double n7 = n3 - getMinecraft.getMinecraft().player.posZ;
        final float n8 = (float)(Math.atan2(n7, n5) * 180.0 / 3.141592653589793 - 90.0);
        final float n9 = (float)(-Math.atan2(n6, Math.sqrt(n5 * n5 + n7 * n7)) * 180.0 / 3.141592653589793);
        final double posX = getMinecraft.getMinecraft().player.posX;
        double posY = getMinecraft.getMinecraft().player.posY;
        final double posZ = getMinecraft.getMinecraft().player.posZ;
        double n10 = 1.0;
        for (double n11 = n4; n11 < getDistance(getMinecraft.getMinecraft().player.posX, getMinecraft.getMinecraft().player.posY, getMinecraft.getMinecraft().player.posZ, n, n2, n3); n11 += n4) {
            ++n10;
        }
        for (double n12 = n4; n12 < getDistance(getMinecraft.getMinecraft().player.posX, getMinecraft.getMinecraft().player.posY, getMinecraft.getMinecraft().player.posZ, n, n2, n3); n12 += n4) {
            final double n13 = getMinecraft.getMinecraft().player.posX - Math.sin(getDirection(n8)) * n12;
            final double n14 = getMinecraft.getMinecraft().player.posZ + Math.cos(getDirection(n8)) * n12;
            posY -= (getMinecraft.getMinecraft().player.posY - n2) / n10;
            list.add(new Vector3f((float)n13, (float)posY, (float)n14));
        }
        list.add(new Vector3f((float)n, (float)n2, (float)n3));
        return list;
    }
    
    public static float getDirection(float n) {
        if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
            n += 180.0f;
        }
        float n2 = 1.0f;
        if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
            n2 = -0.5f;
        }
        else if (Minecraft.getMinecraft().player.moveForward > 0.0f) {
            n2 = 0.5f;
        }
        if (Minecraft.getMinecraft().player.moveStrafing > 0.0f) {
            n -= 90.0f * n2;
        }
        if (Minecraft.getMinecraft().player.moveStrafing < 0.0f) {
            n += 90.0f * n2;
        }
        n *= 0.017453292f;
        return n;
    }
    
    public static double getDistance(final double n, final double n2, final double n3, final double n4, final double n5, final double n6) {
        final double n7 = n - n4;
        final double n8 = n2 - n5;
        final double n9 = n3 - n6;
        return MathHelper.sqrt_double(n7 * n7 + n8 * n8 + n9 * n9);
    }
    
    public static void blockHit(final Entity entity, final boolean b) {
        final Minecraft getMinecraft = Minecraft.getMinecraft();
        final ItemStack getCurrentEquippedItem = getMinecraft.getMinecraft().player.getCurrentEquippedItem();
        if (getMinecraft.getMinecraft().player.getCurrentEquippedItem() != null && entity != null && b && getCurrentEquippedItem.getItem() instanceof ItemSword && getMinecraft.getMinecraft().player.swingProgress > 0.2) {
            getMinecraft.getMinecraft().player.getCurrentEquippedItem().useItemRightClick((World)getMinecraft.getMinecraft().world, (EntityPlayer)getMinecraft.getMinecraft().player);
        }
    }
    
    public static void shiftClick(final Item item) {
        for (int i = 9; i < 37; ++i) {
            final ItemStack getStack = Class200.mc.player.inventoryContainer.getSlot(i).getStack();
            if (getStack != null && getStack.getItem() == item) {
                Class200.mc.playerController.windowClick(0, i, 0, 1, (EntityPlayer)Class200.mc.player);
                break;
            }
        }
    }
    
    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            if (Class200.mc.player.inventory.getStackInSlot(i) == null) {
                return false;
            }
        }
        return true;
    }
    
    public static void tellPlayer(final String s) {
        if (s != null && Class200.mc.player != null) {
            Class200.mc.player.addChatMessage((IChatComponent)new ChatComponentText(s));
        }
    }
    
    public static boolean isMoving() {
        return !Class200.mc.player.isCollidedHorizontally && !Class200.mc.player.isSneaking() && (Class200.mc.player.movementInput.moveForward != 0.0f || Class200.mc.player.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean isMoving2() {
        return Class200.mc.player.moveForward != 0.0f || Class200.mc.player.moveStrafing != 0.0f;
    }
    
    public static void blinkToPos(final double[] array, final BlockPos blockPos, final double n, final double[] array2) {
        double n2 = array[0];
        double n3 = array[1];
        double n4 = array[2];
        final double n5 = blockPos.getX() + 0.5;
        final double n6 = blockPos.getY() + 1.0;
        final double n7 = blockPos.getZ() + 0.5;
        double n8 = Math.abs(n2 - n5) + Math.abs(n3 - n6) + Math.abs(n4 - n7);
        int n9 = 0;
        while (n8 > n) {
            n8 = Math.abs(n2 - n5) + Math.abs(n3 - n6) + Math.abs(n4 - n7);
            if (n9 > 120) {
                break;
            }
            final double n10 = n2 - n5;
            final double n11 = n3 - n6;
            final double n12 = n4 - n7;
            final double n13 = ((n9 & 0x1) == 0x0) ? array2[0] : array2[1];
            if (n10 < 0.0) {
                if (Math.abs(n10) > n13) {
                    n2 += n13;
                }
                else {
                    n2 += Math.abs(n10);
                }
            }
            if (n10 > 0.0) {
                if (Math.abs(n10) > n13) {
                    n2 -= n13;
                }
                else {
                    n2 -= Math.abs(n10);
                }
            }
            if (n11 < 0.0) {
                if (Math.abs(n11) > 0.25) {
                    n3 += 0.25;
                }
                else {
                    n3 += Math.abs(n11);
                }
            }
            if (n11 > 0.0) {
                if (Math.abs(n11) > 0.25) {
                    n3 -= 0.25;
                }
                else {
                    n3 -= Math.abs(n11);
                }
            }
            if (n12 < 0.0) {
                if (Math.abs(n12) > n13) {
                    n4 += n13;
                }
                else {
                    n4 += Math.abs(n12);
                }
            }
            if (n12 > 0.0) {
                if (Math.abs(n12) > n13) {
                    n4 -= n13;
                }
                else {
                    n4 -= Math.abs(n12);
                }
            }
            Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(n2, n3, n4, true));
            ++n9;
        }
    }
    
    static {
        Class200.mc = Minecraft.getMinecraft();
    }
}
