package cn.Arctic.GUI.clickgui.Lander2;


import java.awt.Color;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cn.Arctic.Client;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.clickgui.KeyBindButton;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Module.modules.RENDER.ClickGUI;
import cn.Arctic.Util.RenderUtil123;
import cn.Arctic.Util.TranslateUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import cn.Arctic.values.Value;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class GuiClickUI extends GuiScreen {
    private static List<Module> inSetting = new CopyOnWriteArrayList<>();
    private static ModuleType currentModuleType;
    private static int x, y, wheel;
    private boolean need2move;
    private int dragX, dragY;
    private TranslateUtil translate = new TranslateUtil(0, 0);

    public GuiClickUI() {
        need2move = false;
        dragX = 0;
        dragY = 0;
        translate.setX(0);
        translate.setY(0);
    }

    @Override
    public void initGui() {
        super.initGui();
        if (x > width)
            x = 30;
        if (y > height)
            y = 30;
        need2move = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    FontRenderer font1 =  FontLoaders2.msFont18;
    FontRenderer font2 =  FontLoaders2.msFont16;
    FontRenderer font3 = FontLoaders2.msFont14;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean showSetting;
        int valueSizeY;
        float valueY;
        if (need2move) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
        if (!Mouse.isButtonDown(0) && need2move)
            need2move = false;
       // RenderUtil123.drawBorderedRect(x, y, x + 273, y + 198, 3, new Color(20, 20, 20).getRGB(), getColor());
        RenderUtil123.drawBorderedRect(x + 2, y + 2, x + 273 - 2, y + 198 - 2, 1, new Color(20, 20, 20).getRGB(),
                new Color(20, 20, 20).getRGB());
        Gui.drawRect(x + 70, y + 35, x + 269, y + 195, new Color(0, 0, 0).getRGB());
        FontLoaders2.msFont30.drawStringWithShadow(Client.name, x + 10, y + 12, new Color(180, 180, 180).getRGB());
        font2.drawStringWithShadow(Client.version, x + 12, y + 24, new Color(180, 180, 180).getRGB());

        RenderUtil123.drawGradientSideways(x + 70, y + 35, x + 80, y + 195, new Color(20, 20, 20).getRGB(),
                new Color(0, 0, 0, 0).getRGB());

        int cateY = 0;
        for (ModuleType ModuleType : ModuleType.values()) {
            int strX = x + 40;
            int strY = y + 55 + cateY;
            boolean hover = mouseX > x + 5 && mouseX < x + 65 && mouseY > strY && mouseY < strY + 20;
            FontLoaders2.msFont20.drawCenteredString(ModuleType.name().substring(0,1).toUpperCase() + ModuleType.name().substring(1).toLowerCase(), strX - 1, strY + 6,
                    (ModuleType == currentModuleType) ? getColor()
                            : new Color(hover ? 255 : 140, hover ? 255 : 140, hover ? 255 : 140).getRGB());
            cateY += 20;
        }
        int startX = (x + 80) + 2;
        int startY = (y + 9) + 2 + 28;
        int length = 185;
        float moduleY = translate.getY();
        RenderUtil123.startGlScissor(startX, startY + 14, length, 140);
        for (Module m : Client.instance.getModuleManager().getModules()) {
            if (m.type != currentModuleType)
                continue;
            RenderUtil123.drawRoundRect(startX, startY + moduleY, startX + length, startY + moduleY + 22, 10,
                    new Color(20, 20, 20).getRGB());
            font1.drawStringWithShadow(m.getName(), startX + 8, startY + 9 + moduleY, -1);
          //  FontLoaders2.msFont14.drawStringWithShadow("[ "+"]", m.getName().length()+510, startY + 6 + moduleY, new Color(255,255,255).getRGB());
            RenderUtil123.drawRoundRect(startX + length - 55, startY + moduleY + 7, startX + length - 35,
                    startY + moduleY + 17, 5, new Color(0, 0, 0).getRGB());
            boolean onToggleButton = mouseX > startX + length - 55 && mouseX < startX + length - 35
                    && mouseY > startY + moduleY + 7 && mouseY < startY + moduleY + 17;
                    boolean onModuleRect = mouseX > startX && mouseX < startX + length && mouseY > startY + moduleY
                            && mouseY < startY + moduleY + 28 && mouseY < startY + 14 + 140 && mouseY > startY;
            int left = m.isEnabled() ? startX + length - 14 : startX + length - 24;
            int right = m.isEnabled() ? startX + length - 6 : startX + length - 16;
            RenderUtil123.drawRoundRect(left-30, startY + moduleY + 8, right-30, startY + moduleY + 16, 4, getColor());
            RenderUtil123.drawRoundRect(startX-30 + length - 24, startY + moduleY + 8, startX + length - 46,
                    startY + moduleY + 16, 4, new Color(0, 0, 0, 150).getRGB());
            
            if (onToggleButton) {
                RenderUtil123.drawRoundRect(startX + length - 55, startY + moduleY + 7, startX + length - 35,
                        startY + moduleY + 17, 5, new Color(0, 0, 0, 100).getRGB());
            }
            showSetting = inSetting.contains(m);
            valueSizeY = (m.getValues().size() * 20) + 5;
            valueY = moduleY + 35;
            if (showSetting) {
            	 FontLoaders2.msFont20.drawString("v", startX+167, startY + moduleY + 6,new Color(255,255,255).getRGB());
                RenderUtil123.drawRoundRect(startX + 3, startY + moduleY + 24, startX + length - 3, startY + moduleY + 24+ valueSizeY, 10,
                        new Color(30, 30, 30).getRGB());
                RenderUtil123.drawRoundRect(startX + 3, startY + moduleY + 25, startX + length - 3,
                        startY + moduleY + 23 + valueSizeY, 10, new Color(30, 30, 30).getRGB());
                for (Value<?> setting : m.getValues()) {
                    if (setting instanceof Mode) {
                        Mode s = (Mode) setting;
                        font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 1, -1);
                        RenderUtil123.drawRoundRect(startX + length - 85, startY + valueY - 4, startX + length - 6,
                                startY + valueY + 8, 6, new Color(10, 10, 10).getRGB());
                        int longValue = ((startX + length - 6) - (startX + length - 85)) / 2;
                        font2.drawCenteredString(s.getModeAsString(), (startX + length - 6) - longValue,
                                startY + valueY - 0.5F, getColor());
                        boolean hover = mouseX > startX + length - 85 && mouseX < startX + length - 6
                                && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 8;
                        if (hover) {
                            RenderUtil123.drawRoundRect(startX + length - 85, startY + valueY - 4, startX + length - 6,
                                    startY + valueY + 8, 3, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    if (setting instanceof Numbers) {
                        Numbers<Number> s = (Numbers) setting;
                        font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 3, -1);
                        double inc = s.getIncrement().doubleValue();
                        double max = s.getMaximum().doubleValue();
                        double min = s.getMinimum().doubleValue();
                        double valn = s.getValue().doubleValue();
                        int longValue = ((startX + length - 6) - (startX + length - 83));
                        font3.drawStringWithShadow(s.getValue().doubleValue() + "", startX + length - 84,
                                startY + valueY - 2, -1);
                        RenderUtil123.drawRoundRect(startX + length - 85, startY + valueY + 5, startX + length - 6,
                                startY + valueY + 7, 1, new Color(10, 10, 10).getRGB());
                        RenderUtil123.drawRoundRect(startX + length - 85, startY + valueY + 5,
                                (startX + length - 85) + (longValue * (valn - min) / (max - min)) + 2,
                                startY + valueY + 7, 1, getColor());
                        boolean hover = mouseX > startX + length - 88 && mouseX < startX + length - 3
                                && mouseY > startY + valueY + 2 && mouseY < startY + valueY + 11;
                        if (hover) {
                            RenderUtil123.drawRoundRect(startX + length - 85, startY + valueY + 5, startX + length - 6,
                                    startY + valueY + 7, 1, new Color(0, 0, 0, 100).getRGB());
                            if (Mouse.isButtonDown(0)) {
                                double valAbs = mouseX - (startX + length - 85);
                                double perc = valAbs / ((longValue) * Math.max(Math.min(valn / max, 0), 1));
                                perc = Math.min(Math.max(0, perc), 1);
                                double valRel = (max - min) * perc;
                                double val = min + valRel;
                                val = Math.round(val * (1 / inc)) / (1 / inc);
                                s.setValue(val);
                            }
                        }
                    }
                    if (setting instanceof Option) {
                        Option<Boolean> s = (Option) setting;
                        font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 3, -1);
                        boolean hover = mouseX > startX + length - 18 && mouseX < startX + length - 6
                                && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 8;
                        RenderUtil123.drawRoundRect(startX + length - 18, startY + valueY - 4, startX + length - 6,
                                startY + valueY + 8, 2, new Color(10, 10, 10).getRGB());
                        if (s.getValue()) {
                            RenderUtil123.drawRoundRect(startX + length - 17, startY + valueY - 3, startX + length - 7,
                                    startY + valueY + 7, 2, getColor());
                        }
                        if (hover) {
                            RenderUtil123.drawRoundRect(startX + length - 18, startY + valueY - 4, startX + length - 6,
                                    startY + valueY + 8, 2, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    valueY += 20;
                }
				RenderUtil123.drawGradientSidewaysV(startX + 3, startY + moduleY + 24, startX + length - 3,
						startY + moduleY + 34, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0).getRGB());
            }
            else if(!showSetting) {
            	FontLoaders2.msFont22.drawString("^", startX+165, startY + moduleY + 9,new Color(255,255,255).getRGB());
            }
            moduleY += (showSetting ? (26 + valueSizeY) : 26);
        }
        RenderUtil123.stopGlScissor();
        RenderUtil123.drawGradientSidewaysV(x + 3, y + 35, x + 273 - 3, y + 45, new Color(0, 0, 0, 0).getRGB(),
                new Color(0, 0, 0).getRGB());
        int real = Mouse.getDWheel();
        float moduleHeight = moduleY - translate.getY();
        if (Mouse.hasWheel() && mouseX > startX && mouseY > startY && mouseX < startX + 270 && mouseY < startY + 237) {
            if (real > 0 && wheel < 0) {
                for (int i = 0; i < 5; i++) {
                    if (!(wheel < 0))
                        break;
                    wheel += 5;
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    if (!(real < 0 && moduleHeight > 158 && Math.abs(wheel) < (moduleHeight - (154))))
                        break;
                    wheel -= 5;
                }
            }
        }
        translate.interpolate(0, wheel, 0.15F);

//		GlStateManager.pushMatrix();
//		GlStateManager.enableBlend();
//		GL11.glEnable(GL11.GL_LINE_SMOOTH);
//		float[] colors = ColorUtil.OneToFourTranslator(HUD.getColor());
//		mc.getTextureManager().bindTexture(new ResourceLocation("rainy/arrow.png"));
//		GlStateManager.color(colors[0], colors[1], colors[2]);
//		int size = 16;
//		Gui.drawModalRectWithCustomSizedTexture(mouseX, mouseY, 0, 0, size, size, size, size);
//		GL11.glDisable(GL11.GL_LINE_SMOOTH);
//		GlStateManager.disableBlend();
//		GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean hover2top = mouseX > x && mouseX < x + 273 && mouseY > y && mouseY < y + 35;
        if (hover2top && mouseButton == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            need2move = true;
        } else {
            int cateY = 0;
            for (ModuleType ModuleType : ModuleType.values()) {
                int strX = x + 40;
                int strY = y + 55 + cateY;
                boolean hover = mouseX > x + 5 && mouseX < x + 65 && mouseY > strY && mouseY < strY + 20;
                if (hover && mouseButton == 0) {
                    currentModuleType = ModuleType;
                    wheel = 0;
                    translate.setY(0);
                    break;
                }
                cateY += 20;
            }
            int startX = (x + 80) + 2;
            int startY = (y + 9) + 2 + 25;
            int length = 185;
            float moduleY = translate.getY();
            for (Module m : Client.instance.getModuleManager().getModules()) {
                if (m.type != currentModuleType)
                    continue;
                //Mouse click
                boolean onToggleButton = mouseX > startX + length - 55 && mouseX < startX + length - 35
                        && mouseY > startY + moduleY + 7 && mouseY < startY + moduleY + 20 && mouseY < startY + 14 + 140
                        && mouseY > startY;
                boolean onModuleRect = mouseX > startX && mouseX < startX + length && mouseY > startY + moduleY
                        && mouseY < startY + moduleY + 28 && mouseY < startY + 14 + 140 && mouseY > startY;
                if (onToggleButton && mouseButton == 0)
                    m.setEnabled(!m.isEnabled());
                if (onModuleRect && mouseButton == 1) {
                	 FontLoaders2.msFont22.drawString("^", startX+165, startY + moduleY + 6,new Color(255,255,255).getRGB());
                    if (inSetting.contains(m))
                        inSetting.remove(m);
                    else if (!m.getValues().isEmpty())
                        inSetting.add(m);
                }
                boolean showSetting = inSetting.contains(m);
                int valueSizeY = (m.getValues().size() * 20) + 5;
                float valueY = moduleY + 35;
                if (showSetting) {
                    RenderUtil123.drawRect(startX + 3, startY + moduleY + 24, startX + length - 3,
                            startY + moduleY + 24 + valueSizeY, new Color(30, 30, 30).getRGB());
                    for (Value<?> setting : m.getValues()) {
                        if (setting instanceof Mode) {
                            Mode s = (Mode) setting;
                            boolean hover = mouseX > startX + length - 85 && mouseX < startX + length - 6
                                    && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 11
                                    && mouseY < startY + 14 + 140 && mouseY > startY;
                            if (hover) {
                                if (mouseButton == 1) {
                                    Enum current = (Enum) s.getValue();
                                    s.setValue(s.getModes()[current.ordinal() - 1 < 0 ? s.getModes().length - 1
                                            : current.ordinal() - 1]);
                                }
                                if (mouseButton == 0) {
                                    Enum current = (Enum) s.getValue();
                                    int next = (current.ordinal() + 1 >= s.getModes().length) ? 0
                                            : (current.ordinal() + 1);
                                    s.setValue(s.getModes()[next]);
                                }
                            }
                        }
                        if (setting instanceof Option) {
                            Option<Boolean> s = (Option) setting;
                            boolean hover = mouseX > startX + length - 18 && mouseX < startX + length - 6
                                    && mouseY > startY + valueY - 4 && mouseY < startY + valueY + 11
                                    && mouseY < startY + 14 + 140 && mouseY > startY;
                            if (hover && (mouseButton == 0 || mouseButton == 2)) {
                                s.setValue(!s.getValue());
                            }
                        }
                        valueY += 20;
                    }
                }
                moduleY += (showSetting ? (26 + valueSizeY) : 26);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        int startY = (y + 9) + 2 + 28;
        boolean hover2top = mouseX > x + 1 && mouseX < x + 349 && mouseY > y + 1 && mouseY < y + 9
                && mouseY < startY + 14 + 140 && mouseY > startY;
        if (hover2top && state == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            need2move = false;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ClickGUI.memoriseX = x;
        ClickGUI.memoriseY = y;
        ClickGUI.memoriseWheel = wheel;
        ClickGUI.memoriseML = inSetting;
        ClickGUI.memoriseCatecory = currentModuleType;

        try {
            Mouse.setNativeCursor(null);
        } catch (Throwable ignore) {
        }
    }

    org.lwjgl.input.Cursor emptyCursor;

    private void hideCursor() {
        if (emptyCursor == null) {
            if (Mouse.isCreated()) {
                int min = org.lwjgl.input.Cursor.getMinCursorSize();
                IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
                try {
                    emptyCursor = new org.lwjgl.input.Cursor(min, min, min / 2, min / 2, 1, tmp, null);
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Could not create empty cursor before Mouse object is created");
            }
        }
        try {
            Mouse.setNativeCursor(Mouse.isInsideWindow() ? emptyCursor : null);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void setX(int state) {
        x = state;
    }

    public static void setY(int state) {
        y = state;
    }

    public static void setModuleType(ModuleType state) {
        currentModuleType = state;
    }

    public static void setInSetting(List<Module> moduleList) {
        inSetting = moduleList;
    }

    public static void setWheel(int state) {
        wheel = state;
    }

    public static int getColor() {
        return new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(), 255)
                .getRGB();
    }
}