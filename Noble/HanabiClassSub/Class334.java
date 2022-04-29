package HanabiClassSub;

import net.minecraft.client.multiplayer.*;
import java.net.*;
import java.util.*;
import java.io.*;
import net.minecraft.client.*;

public class Class334
{
    public static boolean isMod;
    public static boolean isDebugMode;
    public static String username;
    public static String password;
    public static boolean active;
    static WorldClient worldChange;
    
    public static void onGameLoop() {
        final WorldClient world = Minecraft.getMinecraft().world;
        if (Class334.worldChange == null) {
            Class334.worldChange = world;
            return;
        }
        if (world == null) {
            Class334.worldChange = null;
            return;
        }
        if (Class334.worldChange != null && world != null && Class334.worldChange != world) {
            Class334.worldChange = world;
        }
    }
    
    static {
        Class334.isMod = false;
        Class334.isDebugMode = false;
        Class334.active = true;
    }
}
