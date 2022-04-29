/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package cn.Noble.Module.modules.RENDER;

import java.awt.Color;
import java.util.Iterator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventRender3D;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Colors;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;

public class ChestESP
        extends Module {
    private Numbers<Double> red = new Numbers<Double>("Red",255.0D, 0.0D, 255.0D, 5.0D);
    private Numbers<Double> blue = new Numbers<Double>("Blue",255.0D, 0.0D, 255.0D, 5.0D);
    private Numbers<Double> green = new Numbers<Double>("Green",255.0D, 0.0D, 255.0D, 5.0D);
    private Option<Boolean> rainbowcol = new Option<Boolean>("Rainbow", true);
    private Mode mode = new Mode("Attack", (Enum[])CHESTESP.values(), (Enum)CHESTESP.CSGO);
    
    public ChestESP() {
        super("ChestESP", new String[]{"chesthack"}, ModuleType.Render);
        this.setEnabled(true);
        this.setColor(new Color(90, 209, 165).getRGB());
        this.addValues(this.red,this.blue,this.green,this.rainbowcol,this.mode);
    }
    @Override
    public void onEnable() {
        // TODO Auto-generated method stub
        super.onEnable();
    }
    public void onDisable() {
        // TODO Auto-generated method stub
        super.onDisable();
    }
    @EventHandler
    private void onRender(EventRender3D event) {
        Iterator var3;
        if(this.mode.getValue() == CHESTESP.Box) {
            var3 = this.mc.world.loadedTileEntityList.iterator();

            while(true) {
                TileEntity ent;
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    ent = (TileEntity)var3.next();
                } while(!(ent instanceof TileEntityChest) && !(ent instanceof TileEntityEnderChest));

                this.mc.getRenderManager();
                double x = (double)ent.getPos().getX() -  mc.getRenderManager().renderPosX;
                this.mc.getRenderManager();
                double y = (double)ent.getPos().getY() -  mc.getRenderManager().renderPosY;
                this.mc.getRenderManager();
                double z = (double)ent.getPos().getZ() -  mc.getRenderManager().renderPosZ;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                Color rainbow = RenderUtil.rainbow(System.nanoTime(), 1.0F, 1.0F);
                GL11.glColor4f(((Boolean)this.rainbowcol.getValue()).booleanValue() ? (float)rainbow.getRed() / 255.0F : ((Double)this.red.getValue()).floatValue() / 255.0F, ((Boolean)this.rainbowcol.getValue()).booleanValue() ? (float)rainbow.getGreen() / 255.0F : ((Double)this.green.getValue()).floatValue() / 255.0F, ((Boolean)this.rainbowcol.getValue()).booleanValue() ? (float)rainbow.getBlue() / 255.0F : ((Double)this.blue.getValue()).floatValue() / 255.0F, 0.15F);
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x + ent.getBlockType().getBlockBoundsMinX(), y + ent.getBlockType().getBlockBoundsMinY(), z + ent.getBlockType().getBlockBoundsMinZ(), x + ent.getBlockType().getBlockBoundsMaxX(), y + ent.getBlockType().getBlockBoundsMaxY(), z + ent.getBlockType().getBlockBoundsMaxZ()));
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }else if(this.mode.getValue() == CHESTESP.CSGO) {
            var3 = this.mc.world.loadedTileEntityList.iterator();

            while(var3.hasNext()) {
                Object o = var3.next();
                if (o instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest)o;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((double)chest.getPos().getX() -  mc.getRenderManager().renderPosX + 0.5D, (double)chest.getPos().getY() -  mc.getRenderManager().renderPosY + 0.5D, (double)chest.getPos().getZ() -  mc.getRenderManager().renderPosZ + 0.5D);
                    GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                    float SCALE = 0.03F;
                    GL11.glScalef(-SCALE, -SCALE, -SCALE);
                    GlStateManager.disableDepth();
                    GlStateManager.disableLighting();
                    Gui.drawRect(-23, -23, 23, -20, Colors.BLACK.c);
                    Gui.drawRect(-23, 23, 23, 26, Colors.BLACK.c);
                    Gui.drawRect(20, 25, 23, -22, Colors.BLACK.c);
                    Gui.drawRect(-20, 25, -23, -22, Colors.BLACK.c);
                    Gui.drawRect(-22, -22, 22, -21, new Color(this.red.getValue().intValue(),this.green.getValue().intValue(),this.blue.getValue().intValue()).getRGB());
                    Gui.drawRect(-22, 24, 22, 25, new Color(this.red.getValue().intValue(),this.green.getValue().intValue(),this.blue.getValue().intValue()).getRGB());
                    Gui.drawRect(21, 22, 22, -19, new Color(this.red.getValue().intValue(),this.green.getValue().intValue(),this.blue.getValue().intValue()).getRGB());
                    Gui.drawRect(-21, 22, -22, -19, new Color(this.red.getValue().intValue(),this.green.getValue().intValue(),this.blue.getValue().intValue()).getRGB());
                    GlStateManager.enableDepth();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
//@EventCall
//private void onTileRender(EventRenderTileEntity event) {
//	if(this.mode.getValue() == CHESTESP.Outline) {
//        this.setColor(Colors.DARKMAGENTA.c);
//        TileEntity tileentityIn = event.getTileentityIn();
//        float partialTicks = event.getPartialTicks();
//        int destroyStage = event.getDestroyStage();
//        BlockPos blockpos = tileentityIn.getPos();
//        if (!(tileentityIn instanceof TileEntityChest)) {
//           return;
//        }
//
//        RenderUtil.checkSetupFBO();
//        event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
//        RenderUtil.outlineOne();
//        event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
//        RenderUtil.outlineTwo();
//        event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
//        RenderUtil.outlineThree();
//        RenderUtil.outlineFour();
//        GL11.glLineWidth(2.0F);
//        Color rainbow = Gui.rainbow(System.nanoTime(), 1.0F, 1.0F);
//        Color color = ((Boolean)this.rainbowcol.getValue()).booleanValue() ? new Color((float)rainbow.getRed() / 255.0F, (float)rainbow.getGreen() / 255.0F, (float)rainbow.getBlue() / 255.0F, 200) : new Color(FlatColors.ORANGE.c);
//        RenderUtil.color(((Boolean)this.rainbowcol.getValue()).booleanValue() ? color.getRGB() : (new Color(((Double)this.red.getValue()).floatValue() / 255.0F, ((Double)this.green.getValue()).floatValue() / 255.0F, ((Double)this.blue.getValue()).floatValue() / 255.0F)).getRGB());
//        event.getTileEntityRendererDispatcher().renderTileEntityAt(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks, destroyStage);
//        RenderUtil.outlineFive();
//        event.cancel = true;
//	}
//}
//@EventCall
//private void on2D(EventRender2D event) {
//if(this.mode.getValue() == CHESTESP.Shader) {
//    if (this.mc.gameSettings.ofFastRender) {
//        this.setEnabled(false);
//        return;
//     }
//     float partialTicks = this.mc.timer.renderPartialTicks;
//     Minecraft.getMinecraft().entityRenderer.setupCameraTransform(partialTicks, 0);
//     Iterator var4 = this.mc.world.loadedTileEntityList.iterator();
//
//     while(var4.hasNext()) {
//        Object o = var4.next();
//        TileEntity chest = (TileEntity)o;
//        if (chest instanceof TileEntityChest) {
//           Minecraft.getMinecraft().entityRenderer.disableLightmap();
//           TileEntityRendererDispatcher.instance.renderTileEntityAt(chest, (double)chest.getPos().getX() -  mc.getRenderManager().renderPosX, (double)chest.getPos().getY() -  mc.getRenderManager().renderPosY, (double)chest.getPos().getZ() -  mc.getRenderManager().renderPosZ, partialTicks);
//        }
//     }
//
//     Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
//     Gui.drawRect(0, 0, 0, 0, 0);
//	}
//}
static enum CHESTESP{
	Box,
	CSGO;
}
}

