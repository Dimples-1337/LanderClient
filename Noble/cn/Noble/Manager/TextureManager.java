package cn.Noble.Manager;


import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import net.minecraft.client.Minecraft;

public class TextureManager {
    public static Texture iconClickgui_BG;
    public static Texture iconClickgui_DownRect;
    public static Texture iconClickgui_LeftLine;
    public static Texture iconClickgui_ModBg;
    public static Texture iconModeBg;
    public static Texture Numbers_Line;
    public static Texture Option_Button;
    public static Texture Option_Line_Off;
    public static Texture Option_Line_On;

    public static Texture SinglePlayer;
    public static Texture MultiPlayer;
    public static Texture key;
    public static Texture settings;
    public static Texture exit;

    public static Texture ButtonRect_Off;
    public static Texture ButtonRect_On;
    public static Texture ModButton;
    public static Texture Option_On;
    public static Texture Option_Off;
    public static Texture Numbers_Rect;
    public static Texture Numbers_true;
    public static Texture Numbers_Button;
    String NewClickguiroot = "assets/minecraft/Eliru/NewClickgui/";

    public void loadTextures(){
        try {
            iconClickgui_BG = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Clickgui_Bg.png"));
            iconClickgui_DownRect = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Clickgui_DownRect.png"));
            iconClickgui_LeftLine = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Clickgui_LeftLine.png"));
            iconClickgui_ModBg = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Clickgui_ModBg.png"));
            iconModeBg = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Mode_Bg.png"));
            Numbers_Button = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Numbers_Button.png"));
            Numbers_Line = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Numbers_Line.png"));
            Option_Button = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Option_Button.png"));
            Option_Line_Off = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Option_Line_Off.png"));
            Option_Line_On = TextureLoader.getTexture("PNG", Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream(NewClickguiroot +"Option_Line_On.png"));
            SinglePlayer =TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/mainmenu/SinglePlayer.png"));
            MultiPlayer =TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/mainmenu/MultiPlayer.png"));
            key =TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/mainmenu/key.png"));
            settings =TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/mainmenu/settings.png"));
            exit =TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/mainmenu/exit.png"));

            ButtonRect_Off=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("ass ets/minecraft/Eliru/ZelixClickgui/ButtonRect_Off.png"));
            ButtonRect_On=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/ZelixClickgui/ButtonRect_On.png"));
            ModButton=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/ZelixClickgui/ModButton.png"));
            Option_On=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/ZelixClickgui/Option_On.png"));
            Option_Off=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/ZelixClickgui/Option_Off.png"));
            Numbers_Rect=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/ZelixClickgui/Numbers_Rect.png"));
            Numbers_true=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/ZelixClickgui/Numbers_true.png"));
            Numbers_Button=TextureLoader.getTexture("PNG",Minecraft.getMinecraft().getTextureManager().getClass()
                    .getClassLoader().getResourceAsStream("assets/minecraft/Eliru/ZelixClickgui/Numbers_Button.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
