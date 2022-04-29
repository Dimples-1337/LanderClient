/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Font.me.superskidder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class FontLoaders2 {

    private final HashMap fonts = new HashMap();
    public static FontRenderer msFont14 = getSyyh(14, true);
    public static FontRenderer msFont15 = getSyyh(15, true);
    public static FontRenderer msFont16 = getSyyh(16, true);
    public static FontRenderer msFont17 = getSyyh(17, true);
    public static FontRenderer msFont18 = getSyyh(18, true);
    public static FontRenderer msFont20 = getSyyh(20, true);
    public static FontRenderer msFont22 = getSyyh(22, true);
    public static FontRenderer msFont25 = getSyyh(25, true);
    public static FontRenderer msFont30 = getSyyh(30, true);
    public static FontRenderer msFont36 = getSyyh(36, true);
    public static FontRenderer msFont45 = getSyyh(45, true);
    public static FontRenderer msFont72 = getSyyh(72, true);
    public static FontRenderer icon14 = getIcon(14, true);
    public static FontRenderer icon16 = getIcon(16, true);
    public static FontRenderer icon18 = getIcon(18, true);


    public static FontRenderer getSyyh(int size, boolean antiAlias) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Lander1/syyh.otf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }

        return new FontRenderer(font,size,antiAlias);
    }
	private static FontRenderer getIcon(int size, boolean antiAlias) {
		Font font;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Lander1/SessionInfo.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		 return new FontRenderer(font,size,antiAlias);
	}



}

