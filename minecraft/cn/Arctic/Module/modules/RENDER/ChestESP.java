package cn.Arctic.Module.modules.RENDER;


import java.awt.Color;

import org.lwjgl.opengl.GL11;

import HanabiClassSub.LiquidRender;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;


public class ChestESP extends Module
{
    private final Mode<Enum> modeValue;
    private final Option<Boolean> chestValue;
    private final Option<Boolean> enderChestValue;
    private final Option<Boolean> furnaceValue;
    private final Option<Boolean> dispenserValue;
    private final Option<Boolean> hopperValue;

    public ChestESP() {
        super("ChestESP", new String[] { "StorageESP" }, ModuleType.Render);
        this.modeValue = new Mode<Enum>("Mode", Modes.values(), Modes.OtherBox);
        this.chestValue = new Option<Boolean>("Chest",Boolean.valueOf(true));
        this.enderChestValue = new Option<Boolean>("EnderChest",Boolean.valueOf(true));
        this.furnaceValue = new Option<Boolean>("Furnace",Boolean.valueOf(true));
        this.dispenserValue = new Option<Boolean>("Dispenser", Boolean.valueOf(true));
        this.hopperValue = new Option<Boolean>("Hopper", Boolean.valueOf(true));
        this.addValues(this.modeValue, this.chestValue, this.enderChestValue, this.furnaceValue, this.dispenserValue, this.hopperValue);
        removed = true;
           
            }
    

    @EventHandler
    public void onRender3D(final EventRender3D event) {
        try {
            final float gamma = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = 100000.0f;
            final Minecraft mc = ChestESP.mc;
            for (final TileEntity tileEntity : Minecraft.world.loadedTileEntityList) {
                Color color = null;
                if (this.chestValue.getValue() && tileEntity instanceof TileEntityChest) {
                    color = new Color(new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
                }
                if (this.enderChestValue.getValue() && tileEntity instanceof TileEntityEnderChest) {
                    color = Color.MAGENTA;
                }
                if (this.furnaceValue.getValue() && tileEntity instanceof TileEntityFurnace) {
                    color = Color.BLACK;
                }
                if (this.dispenserValue.getValue() && tileEntity instanceof TileEntityDispenser) {
                    color = Color.BLACK;
                }
                if (this.hopperValue.getValue() && tileEntity instanceof TileEntityHopper) {
                    color = Color.GRAY;
                }
                if (color == null) {
                    continue;
                }
                if (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest)) {
                    LiquidRender.drawBlockBox(tileEntity.getPos(), color, !this.modeValue.getValue().equals(Modes.OtherBox));
                }
                else {
                    if (Modes.OtherBox.equals(this.modeValue.getValue()) || modeValue.getValue() == Modes.Box) {
                        LiquidRender.drawBlockBox(tileEntity.getPos(), color, !this.modeValue.getValue().equals(Modes.OtherBox));
                        continue;
                    } else if (Modes.TowD.equals(this.modeValue.getValue())) {
                        LiquidRender.draw2D(tileEntity.getPos(), color.getRGB(), Color.BLACK.getRGB());
                        continue;
                    } else if (Modes.WireFrame.equals(this.modeValue.getValue())) {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, EventRender3D.getInstance().getPartialTicks(), -1);
                        LiquidRender.glColor(color);
                        GL11.glLineWidth(1.5f);
                        TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, EventRender3D.getInstance().getPartialTicks(), -1);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        continue;
                    }
                }
            }
            final Minecraft mc2 = ChestESP.mc;
            for (final Entity entity : Minecraft.world.loadedEntityList) {
                if (entity instanceof EntityMinecartChest) {
                    if (Modes.OtherBox.equals(this.modeValue.getValue()) || modeValue.getValue() == Modes.Box) {
                        LiquidRender.drawEntityBox(entity, new Color(0, 66, 255, 130), !this.modeValue.getValue().equals(Modes.OtherBox));
                        continue;
                    } else if (Modes.TowD.equals(this.modeValue.getValue())) {
                        LiquidRender.draw2D(entity.getPosition(), new Color(0, 66, 255).getRGB(), Color.BLACK.getRGB());
                        continue;
                    } else if (Modes.WireFrame.equals(this.modeValue.getValue())) {
                        final boolean entityShadow = ChestESP.mc.gameSettings.entityShadows;
                        ChestESP.mc.gameSettings.entityShadows = false;
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        LiquidRender.glColor(new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
                        ChestESP.mc.getRenderManager().renderEntityStatic(entity, ChestESP.mc.timer.renderPartialTicks, true);
                        LiquidRender.glColor(new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
                        GL11.glLineWidth(1.5f);
                        ChestESP.mc.getRenderManager().renderEntityStatic(entity, ChestESP.mc.timer.renderPartialTicks, true);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        ChestESP.mc.gameSettings.entityShadows = entityShadow;
                        continue;
                    }
                }
            }
            LiquidRender.glColor(new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
            ChestESP.mc.gameSettings.gammaSetting = gamma;
        }
        catch (Exception ex) {}
    }

    enum Modes
    {
        Box,
        OtherBox,
        TowD,
        WireFrame;
    }
}
