package cn.Noble.Module.modules.RENDER;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
public class StatusHUD extends Module{
	   private final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

	public StatusHUD() {
	     super("StatusHUD", new String[]{"superkillaura"}, ModuleType.Render);
	}
	@EventHandler
	   public void render2D(EventRender2D evnet) {
        boolean currentItem = true;
        GL11.glPushMatrix();
        ArrayList<ItemStack> stuff = new ArrayList<ItemStack>();
        boolean onwater = this.mc.player.isEntityAlive() && this.mc.player.isInsideOfMaterial(Material.water);
        int split = new ScaledResolution(this.mc).getScaledWidth() / 2 + 87;
        int y = new ScaledResolution(this.mc).getScaledHeight() - 16;
        for (int index = 3; index >= 0; --index) {
            ItemStack armer = this.mc.player.inventory.armorInventory[index];
            if (armer == null) continue;
            stuff.add(armer);
        }
        if (this.mc.player.getCurrentEquippedItem() != null) {
            stuff.add(this.mc.player.getCurrentEquippedItem());
        }
        if (this.mc.playerController.shouldDrawHUD()) {
     	   y -= 17;
        }
        if (this.mc.playerController.shouldDrawHUD() && onwater) {
     	   y -= 10;
        }
        for (ItemStack errything : stuff) {
            if (this.mc.world != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;        
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            
            this.mc.getRenderItem().zLevel = -150.0f;          
            mc.getRenderItem().renderItemAndEffectIntoGUI(errything, split, y);

            this.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
	   }
}
