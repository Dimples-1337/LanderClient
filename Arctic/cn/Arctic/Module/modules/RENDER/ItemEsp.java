package cn.Arctic.Module.modules.RENDER;


import org.lwjgl.opengl.GL11;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

public class ItemEsp extends Module {
    public static Option outlinedboundingBox = new Option("OutlinedBoundingBox", true);
    public static Option boundingBox = new Option("BoundingBox",true);
    public static Mode<Enum> heigh = new Mode("Height", (Enum[]) height.values(), (Enum) height.High);

    public ItemEsp() {
        super("ItemESP", new String[]{"ItemESP"}, ModuleType.Render);
        this.addValues(this.outlinedboundingBox, this.boundingBox, this.heigh);
        removed = true;
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        for (Object o : mc.world.loadedEntityList) {
            if (!(o instanceof EntityItem)) continue;
            EntityItem item = (EntityItem) o;
            double var10000 = item.posX;
            Minecraft.getMinecraft().getRenderManager();
            double x = var10000 - RenderManager.renderPosX;
            var10000 = item.posY + 0.5D;
            Minecraft.getMinecraft().getRenderManager();
            double y = var10000 - RenderManager.renderPosY;
            var10000 = item.posZ;
            Minecraft.getMinecraft().getRenderManager();
            double z = var10000 - RenderManager.renderPosZ;
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0F);
            GL11.glColor4f(1, 1, 1, .75F);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            if (((Boolean) this.outlinedboundingBox.getValue()).booleanValue() && this.heigh.getValue() == height.High) {
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - .2D, y - 0.05, z - .2D, x + .2D, y - 0.45d, z + .2D));
            }
            if (((Boolean) this.outlinedboundingBox.getValue()).booleanValue() && this.heigh.getValue() == height.Low) {
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
            }
            GL11.glColor4f(1, 1, 1, 0.15f);
            if (((Boolean) this.boundingBox.getValue()).booleanValue() && this.heigh.getValue() == height.High) {
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x - .2D, y - 0.05, z - .2D, x + .2D, y - 0.45d, z + .2D));
            }
            GL11.glColor4f(1, 1, 1, 0.15f);
            if (((Boolean) this.boundingBox.getValue()).booleanValue() && this.heigh.getValue() == height.Low) {
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x - .2D, y - 0.3d, z - .2D, x + .2D, y - 0.4d, z + .2D));
            }
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }
    

    enum height {
        High,
        Low;
    }
}
