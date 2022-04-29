package cn.Arctic.Util;

import java.util.Random;

import net.minecraft.util.ResourceLocation;

public class RandomImgUtils {
    private static long startTime = 0L;
    static Random random = new Random();
    static int count = random.nextInt(4);
    public static int count2 = random.nextInt(4);

    public static ResourceLocation getCape1() {
        return new ResourceLocation("Lander1/Cape/Lander.png");
    }
    public static ResourceLocation getCape2() {
        return new ResourceLocation("Lander1/Cape/jiaran.png");
    }
    public static ResourceLocation getCape3() {
        return new ResourceLocation("Lander1/Cape/Misaka.png");
    }
    public static ResourceLocation getCape4() {
        return new ResourceLocation("Lander1/Cape/Morn..png");
    }
    public static ResourceLocation getCape5() {
        return new ResourceLocation("Lander1/Cape/Pdx666.png");
    }
    public static ResourceLocation getCape6() {
        return new ResourceLocation("Lander1/Cape/sb.png");
    }
    public static ResourceLocation getCape7() {
        return new ResourceLocation("Lander1/Cape/yellow.png");
    }
}
