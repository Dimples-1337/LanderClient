
package cn.Noble.GUI.ui;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cn.Noble.Client;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Font.me.superskidder.FontLoaders2;
import cn.Noble.GUI.SplashProgress;
import cn.Noble.GUI.Menu.GuiWelcome;
import cn.Noble.GUI.Menu.MainMenu;
import cn.Noble.GUI.login.GuiPasswordField;
import cn.Noble.Util.HWIDUtils;
import cn.Noble.Util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiLogin extends GuiScreen {

    public static GuiPasswordField password;
    public static GuiTextField username;
    public GuiButton loginButton;
    public GuiButton freeButton;
    public static boolean login;
    private int alpha = 255;
    public static boolean i;
    public static boolean j;
    int hue;
    boolean up;
    int hue2;
    boolean up2;
    int hue3;
    boolean up3;
    private static boolean crack;
    static final int allX = 90;
    
    public static boolean Passed = false;
    public static String process = "[Waiting For Login...]";
    public static String Now = "Noble Login";

    public GuiLogin() {
        try {
            Class<?> hwidClass = Class.forName("cn.Noble.Util.HWIDUtils");
            Class<?> webClass = Class.forName("cn.Noble.Util.WebUtils");
            Method getUrlData = webClass.getMethod("getUrlData", String.class);
            Method getHWID = hwidClass.getMethod("getHWID");
            String hwid = (String) getHWID.invoke(null);
            List<String> webList = (List<String>) getUrlData.invoke(null, new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 103, 105, 116, 101, 101, 46, 99, 111, 109, 47, 114, 117, 111, 99, 104, 101, 110, 45, 49, 50, 51, 47, 114, 117, 111, 99, 104, 101, 110, 105, 115, 115, 98, 47, 114, 97, 119, 47, 109, 97, 115, 116, 101, 114, 47, 49}));
            int count = 0;
            for (String webData : webList) {
                String[] dataBytes = webData.split(";");
                for (String dataByte : dataBytes) {
                    if (dataByte.equals(hwid)) {
                        count++;
                    }
                }
            }
            if ((count == 0 || count < 0 || count > 1)) {
                Class systemClass = Class.forName("java.lang.System");
                Method exit = systemClass.getDeclaredMethod("exit", int.class);
                exit.invoke(null, 0);
                return;
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                try {
                    if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
                        Client.username = username.getText();
                        String passwords = password.getText();
                        if (Login(username.getText(), passwords)) {
                            Class<?> mainMenuClass = Class.forName("cn.Noble.GUI.Menu.GuiWelcome");
                            GuiScreen mainMenu = (GuiScreen) mainMenuClass.getConstructor().newInstance();
                            Minecraft.getMinecraft().displayGuiScreen(mainMenu);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                System.exit(0);
        }
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        if (SplashProgress.alpha > 1) {
            SplashProgress.alpha -= 4;
        }
        if (i) {
            if (this.alpha < 255) {
                alpha += 5;//15
            }
        } else {
            if (SplashProgress.glide && SplashProgress.alpha < 10) {
                if (this.alpha > 0) {
                    alpha -= 5;
                }
            }
        }
        //hue
        if (hue > 150) {
            up = false;
        }
        if (hue == 0) {
            up = true;
        }
        if (up) {
            hue += 3;
        } else {
            hue -= 3;
        }
        //hue2
        if (hue2 > 150) {
            up2 = false;
        }
        if (hue2 == 0) {
            up2 = true;
        }
        if (up2) {
            hue2 += 6;
        } else {
            hue2 -= 6;
        }
        //hue3
        if (hue3 > 400) {
            up3 = false;
        }
        if (hue3 == 0) {
            up3 = true;
        }
        if (up3) {
            hue3 += 6;
        } else {
            hue3 -= 6;
        }
        if (!username.getText().isEmpty() && !password.getText().isEmpty() && Mouse.isButtonDown(0) && var1 > GuiScreen.width / 2 + 30 - allX && var1 < GuiScreen.width / 2 + 155 - allX && var2 > GuiScreen.height / 2 + 47 && var2 < GuiScreen.height / 2 + 70) {
            System.out.println("success");
            Client.username = username.getText();
            String usernames = username.getText();
            String passwords = password.getText();
            if (Login(usernames, passwords)) {
                i = true;
                j = true;
            } else {
                Client.username = null;
                i = true;
                j = false;
            }
        }
        final int loginX = -20;
        final int loginY = -20;
        this.drawDefaultBackground();
        RenderUtil.drawBorderedRect(width / 2 - 180 - loginX, height / 2 - 115 - loginY, width / 2 + 180 + loginX, height / 2 + 115 + loginY, 3, new Color(20, 20, hue, 170).getRGB(), new Color(0, 0, 0, 135).getRGB());
        RenderUtil.drawGradientSideways(width / 2 + 30 - allX, height / 2 + 47, width / 2 + 155 - allX, height / 2 + 70, new Color(hue, 40, 40).getRGB(), new Color(hue2, 40, 40, 200).getRGB());
        Gui.drawRect(width / 2 + 30 - allX, height / 2 - 9, width / 2 + 155 - allX, height / 2 - 8, new Color(200, 200, 200).getRGB());
        Gui.drawRect(width / 2 + 30 - allX, height / 2 + 30, width / 2 + 155 - allX, height / 2 + 31, new Color(200, 200, 200).getRGB());
        FontLoaders2.msFont20.drawString("Log In", GuiScreen.width / 2 + 80 - allX, GuiScreen.height / 2 + 55, -1);
        final int y2offsets = 140;
        final int x2offsets = 90;
        FontLoaders2.msFont30.drawString("Login | User Check", width / 2 - 150 + x2offsets, height / 2 + 53 + 28 - y2offsets, -1);
        if (password.getText().isEmpty() && !password.isFocused()) {
            String name = "Password";
            String smoothname = Character.toUpperCase(name.toLowerCase().charAt(0)) + name.toLowerCase().substring(1);
            FontLoaders2.msFont18.drawString(smoothname, GuiScreen.width / 2 + 35 - allX, GuiScreen.height / 2 + 20, new Color(200, 200, 200).getRGB());
        } else {
            String xing = "";
            for (char ignored : password.getText().toCharArray()) {
                xing = xing + "* ";
                FontLoaders2.msFont20.drawString(xing, GuiScreen.width / 2 + 35 - allX, GuiScreen.height / 2 + 20, new Color(200, 200, 200).getRGB());
            }
        }
        if (username.getText().isEmpty() && !username.isFocused()) {
            String name = "Username";
            String smoothname = Character.toUpperCase(name.toLowerCase().charAt(0)) + name.toLowerCase().substring(1);
            FontLoaders2.msFont18.drawString(smoothname, GuiScreen.width / 2 + 35 - allX, GuiScreen.height / 2 - 19, new Color(200, 200, 200).getRGB());
        } else {
            FontLoaders2.msFont18.drawString(username.getText(), GuiScreen.width / 2 + 35 - allX, GuiScreen.height / 2 - 19, new Color(200, 200, 200).getRGB());
        }
        super.drawScreen(var1, var2, var3);
        if (alpha >= 255 && i && j) {
            i = false;
            j = false;
            Minecraft.getMinecraft().displayGuiScreen(new GuiWelcome());
        }
        if (alpha >= 255 && i && !j) {
            i = false;
        }
      //  Gui.drawRect(0, 0, RenderUtil.width(), RenderUtil.height(), new Color(0, 0, 0, alpha).getRGB());
    }

    @Override
    public void initGui() {
        FontRenderer var1 = this.mc.fontRendererObj;
        int var2 = this.height / 2;
        super.initGui();

        username = new GuiTextField(var2, var1, this.width / 2 - 100, this.height / 2 - 50, 200, 20);
        password = new GuiPasswordField(var1, this.width / 2 - 100, this.height / 2 - 10, 200, 20);
        username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        try {
            super.keyTyped(var1, var2);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        if (var1 == 9) {
            if (!username.isFocused()) {
                username.setFocused(true);
            } else {
                username.setFocused(true);
                password.setFocused(!username.isFocused());
            }
        }

        username.textboxKeyTyped(var1, var2);
        password.textboxKeyTyped(var1, var2);
    }

    @Override
    protected void mouseClicked(int var1, int var2, int var3) {
        try {
            super.mouseClicked(var1, var2, var3);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        username.mouseClicked(var1, var2, var3);
        password.mouseClicked(var1, var2, var3);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }


    public static boolean Login(String usernames, String passwords) {
        process = "[Logging in...]";
        try {
            Class<?> hwidClass = Class.forName("cn.Noble.Util.HWIDUtils");
            Class<?> webClass = Class.forName("cn.Noble.Util.WebUtils");
            Method getUrlData = webClass.getMethod("getUrlData", String.class);
            Method getHWID = hwidClass.getMethod("getHWID");
            String hwid = (String) getHWID.invoke(null);
            List<String> webList = (List<String>) getUrlData.invoke(null, new String(new byte[]{104, 116, 116, 112, 115, 58, 47, 47, 103, 105, 116, 101, 101, 46, 99, 111, 109, 47, 114, 117, 111, 99, 104, 101, 110, 45, 49, 50, 51, 47, 114, 117, 111, 99, 104, 101, 110, 105, 115, 115, 98, 47, 114, 97, 119, 47, 109, 97, 115, 116, 101, 114, 47, 112, 97, 76, 111, 103, 105, 110}));
            int count = 0;
            for (String webData : webList) {
                if (webData.equals(usernames + ";" + passwords + ";" + hwid)) {
                    count++;
                }
                String[] dataBytes = webData.split(";");
                for (String dataByte : dataBytes) {
                    if (dataByte.equals(hwid)) {
                        count++;
                    } else if (dataByte.equals(usernames)) {
                        count++;
                    } else if (dataByte.equals(passwords)) {
                        count++;
                    }
                }
            }
            if (count == 4) {
                return true;
            } else {
                process = "[Failed.]";
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error");
            process = "[HWID Error.]";
            return false;
        }
    }
}




