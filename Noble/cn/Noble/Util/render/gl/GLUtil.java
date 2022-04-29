package cn.Noble.Util.render.gl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class GLUtil {
    private static final Map<Integer, Boolean> glCapMap = new HashMap();

    public GLUtil() {}

    public static void setGLCap(int cap, boolean flag) {
        glCapMap.put(cap, GL11.glGetBoolean(cap));
        if (flag) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }
    }

    private static void revertGLCap(int cap) {
        Boolean origCap = glCapMap.get(cap);
        if (origCap != null) {
            if (origCap) {
                GL11.glEnable(cap);
            } else {
                GL11.glDisable(cap);
            }
        }

    }

    public static void glEnable(int cap) {
        setGLCap(cap, true);
    }

    public static void glDisable(int cap) {
        setGLCap(cap, false);
    }

    public static void revertAllCaps() {
        Iterator var0 = glCapMap.keySet().iterator();

        while (var0.hasNext()) {
            Integer cap = (Integer) var0.next();
            revertGLCap(cap);
        }
    }
}