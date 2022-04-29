package cn.Noble.Module.modules.RENDER;

import net.minecraft.client.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.multiplayer.*;

public class Helper
{
    public static Minecraft mc;
    public static boolean xray;
    public static Boolean DIF;
    public static HashSet blockIDs;
    public static int opacity;
    public static Boolean bypass;
    public static List<Block> dimblock;
    public static ArrayList<BlockPos> glow;
    
    protected static List<Entity> getLoadedEntities() {
        Minecraft.getMinecraft();
        return Minecraft.getMinecraft().world.loadedEntityList;
    }
    
    public static boolean hasArmor(final EntityPlayer player) {
        if (player.inventory == null) {
            return false;
        }
        final ItemStack boots = player.inventory.armorInventory[0];
        final ItemStack pants = player.inventory.armorInventory[1];
        final ItemStack chest = player.inventory.armorInventory[2];
        final ItemStack head = player.inventory.armorInventory[3];
        return boots != null || pants != null || chest != null || head != null;
    }
    
    public static boolean containsID(final int id) {
        return Helper.blockIDs.contains(id);
    }
    
    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    public static ClientPlayerEntity player() {
        mc();
        return Minecraft.getMinecraft().player;
    }
    
    public static WorldClient world() {
        mc();
        return Minecraft.getMinecraft().world;
    }
    
    public static boolean onServer(final String server) {
        return !Helper.mc.isSingleplayer() && Helper.mc.getCurrentServerData().serverIP.toLowerCase().contains(server);
    }
    
    static {
        Helper.mc = Minecraft.getMinecraft();
        Helper.xray = false;
        Helper.blockIDs = new HashSet();
        Helper.bypass = true;
        Helper.dimblock = new ArrayList<Block>();
        Helper.glow = new ArrayList<BlockPos>();
    }
}
