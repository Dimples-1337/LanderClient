package javax.vecmath;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public enum Base {
    INSTANCE;

    static Minecraft mc = Minecraft.getMinecraft();
    private final ResourceLocation background = new ResourceLocation("Eliru/Realize.png");
    private final ResourceLocation verify = new ResourceLocation("Eliru/verify.png"),
            singleleplayer = new ResourceLocation("Eliru/singleplayer.png"),
            settings = new ResourceLocation("Eliru/settings.png"),
            multiplayer = new ResourceLocation("Eliru/multiplayer.png"),
            exit = new ResourceLocation("Eliru/exit.png"),
            altmanager = new ResourceLocation("Eliru/altmanager.png");

public ResourceLocation getMainMenu() {
    return background;
}

public ResourceLocation getVerify() {
    return verify;
}

public ResourceLocation getSinglePlayer() {
    return singleleplayer;
}

public ResourceLocation getSettings() {
    return settings;
}

public ResourceLocation getMultiPlayer() {
    return multiplayer;
}

public ResourceLocation getAlt() {
    return altmanager;
}

public ResourceLocation getExit() {
    return exit;
}
}

