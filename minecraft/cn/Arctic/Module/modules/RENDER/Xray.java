
package cn.Arctic.Module.modules.RENDER;



import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.Xray.EventBlockRenderSide;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.GUI.Xray.XrayBlock;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.RenderUtilqkl;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;


public class Xray
        extends Module {
	private int opacity = 160;
	public static Numbers<Double> OPACITY = new Numbers<Double>("Opacity", 160.0, 0.0, 255.0, 5.0);
	public static Option<Boolean> cave = new Option<>("Cave",true);
	private Option<Boolean> ESP = new Option<>("ESP", true);
	private Option<Boolean> Tracers = new Option<>("Tracers", true);
	public static Option<Boolean> Tags = new Option<Boolean>("Tags", false);
	private Option<Boolean> coal = new Option<>("Coal", false);//16
	private Option<Boolean> iron = new Option<>("Iron", true);//15
	private Option<Boolean> gold = new Option<>("Gold", true);//14
	private Option<Boolean> lapis = new Option<>("Lapis",false);//21
	private Option<Boolean> redstone = new Option<>("RedStone", true);//73
	private Option<Boolean> diamond = new Option<>("Diamond",  true);//56
	private Option<Boolean> emerald = new Option<>("Emerald", false);//129
	//private Option<Boolean> quartz = new Option<>("Quartz", "Quartz", false);//153

	public Xray() {
		super("Xray", new String[]{}, ModuleType.Render);
		addValues(OPACITY,cave,ESP,Tracers,Tags, coal, iron, gold, lapis, redstone, diamond, emerald);
		if (gold.getValue()) {
			blocks.add(14);//Gold_ore
			blocks.add(41);//Gold_block
		}
		if (iron.getValue()) {
			blocks.add(15);//Iron_ore
			blocks.add(42);//Iron_Block
		}
		if (coal.getValue()) {
			blocks.add(16);//Coal_ore
			blocks.add(173);//Coal_Block
		}
		if (lapis.getValue()) {
			blocks.add(21);//Lapis_ore
			blocks.add(22);//Lapis_Block
		}
		if (diamond.getValue()) {
			blocks.add(56);//Diamond_ore
			blocks.add(57);//Diamond_block
		}
		if (redstone.getValue()) {
			blocks.add(73);//RedStone_ore
			blocks.add(152);//RedStone_Block
		}

		if (emerald.getValue()) {
			blocks.add(129);//Emerald_ore
			blocks.add(133);//Emerald_Block
		}




		// TODO Auto-generated constructor stub
	}

	public static final HashSet<Integer> blockIDs = new HashSet();
	public List<Integer> blocks = new ArrayList<Integer>();
	public List<Integer> KEY_IDS = Lists.newArrayList(new Integer[] {
			Integer.valueOf(10),//瀹�鈺傜ギ
			Integer.valueOf(11),//瀹�鈺傜ギ
			Integer.valueOf(8),//濮橈拷
			Integer.valueOf(9),//濮橈拷

			Integer.valueOf(14),//闁叉垹鐔�
			Integer.valueOf(41),//闁叉垵娼�

			Integer.valueOf(15),//闁句胶鐔�
			Integer.valueOf(42),//闁句礁娼�

			Integer.valueOf(16),//閻撱倗鐔�

			Integer.valueOf(21),//閹懏娅欓弮鍓佺唵

			Integer.valueOf(152),//缁俱垻鐓堕崸锟�

			Integer.valueOf(153),//娑撳鏅惌璺ㄧ叾


			Integer.valueOf(129),//缂佸灝鐤傞惌锟�
			Integer.valueOf(133),//缂佸灝鐤傞惌鍐叉健

			Integer.valueOf(56),//闁借崵鐓堕惌锟�
			Integer.valueOf(57),//闁借崵鐓堕崸锟�

			Integer.valueOf(46),//TNT
			Integer.valueOf(48),//閼绘梻鐓�
			Integer.valueOf(52),//閸掗攱锟筋亞顑�
			Integer.valueOf(61),//閻旀梻鍊�
			Integer.valueOf(62),//閻旀梻鍊�
			Integer.valueOf(73),//缁俱垻鐓�
			Integer.valueOf(74),//缁俱垻鐓�
			Integer.valueOf(84),//CD閺堬拷
			Integer.valueOf(89),//閽�銈囩叾
			Integer.valueOf(103),//鐟楄法鎽�
			Integer.valueOf(116),//闂勫嫰鐡熼崣锟�
			Integer.valueOf(117),//闁板潡锟界姴褰�
			Integer.valueOf(118),//閸氬啫鑵戦柨锟�
			Integer.valueOf(120),//閺堫偄婀存导鐘伙拷渚�妫�
			Integer.valueOf(137),//閸涙垝鎶ら弬鐟版健
			Integer.valueOf(145),//闁句胶鐗�
			Integer.valueOf(154)//濠曞繑鏋�
	});


	public List<Integer> getBlocks() {
		return this.blocks;
	}

	public CopyOnWriteArrayList list = new CopyOnWriteArrayList<>();

	@Override
	public void onEnable() {
		blockIDs.clear();
		this.opacity = OPACITY.getValue().intValue();
		super.onEnable();
		try {
			for (Integer o : this.KEY_IDS) {
				blockIDs.add(o);
			}
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.renderGlobal.loadRenderers();
		this.list.clear();

	}

	@EventHandler
	public void onRender3D(EventRender3D e) {
		this.setColor(new Color(0xFF0042).getRGB());
		if (!ESP.getValue()) {
			return;
		}
		Iterator x = list.iterator();
		while (x.hasNext()) {
			XrayBlock x1 = (XrayBlock) x.next();
			if (x1.type.contains("Diamond") && diamond.getValue())
				RenderUtilqkl.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(55, 155, 255),
						new Color(55, 155, 255).getRGB(), 0.1f);
			else if (x1.type.contains("Iron") && iron.getValue())
				RenderUtilqkl.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(180, 180, 180),
						new Color(180, 180, 180).getRGB(), 0.1f);
			else if (x1.type.contains("Gold") && gold.getValue())
				RenderUtilqkl.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(255, 255, 120), new Color(255, 255, 120).getRGB(),
						0.1f);
			else if (x1.type.contains("Red") && redstone.getValue()) {
				RenderUtilqkl.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(255, 50, 50), new Color(255, 50, 50).getRGB(),
						0.1f);
			} else if (x1.type.contains("Emerald") && emerald.getValue()){
				RenderUtilqkl.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 255, 0), new Color(0,255,0).getRGB(),
						0.1f);
			} else if (x1.type.contains("Coal") && coal.getValue()) {
				RenderUtilqkl.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(25, 0, 50), new Color(25,0,50).getRGB(),
						0.1f);
			} else if (x1.type.contains("Lapis") && lapis.getValue()) {
				RenderUtilqkl.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 255), new Color(0,0,255).getRGB(),
						0.1f);
			}
			}

		}
	@EventHandler
	public void onRenderTracers(EventRender3D er) {
		Iterator x = list.iterator();
		if(Tracers.getValue()) {
			while (x.hasNext()) {
				XrayBlock x1 = (XrayBlock) x.next();
				double posX = x1.x - RenderManager.renderPosX;
				double posY = x1.y - RenderManager.renderPosY;
				double posZ = x1.z - RenderManager.renderPosZ;
				boolean old = this.mc.gameSettings.viewBobbing;
				RenderUtil.startDrawing();
				this.mc.gameSettings.viewBobbing = false;
				this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
				this.mc.gameSettings.viewBobbing = old;

				if (x1.type.contains("Diamond") && diamond.getValue()) {
					this.drawLine(55,188,255, posX, posY, posZ);
				}


				if (x1.type.contains("Gold") && gold.getValue()) {
					this.drawLine(255,255,120, posX, posY, posZ);
				}


				if (x1.type.contains("Iron") && iron.getValue()) {
					this.drawLine(180,180,180, posX, posY, posZ);
				}


				if (x1.type.contains("Redstone") && redstone.getValue()) {
					this.drawLine(255,50,50, posX, posY, posZ);
				}

				if (x1.type.contains("Coal") && coal.getValue()) {
					this.drawLine(0,0,0, posX, posY, posZ);
				}


				if (x1.type.contains("Lapis") && lapis.getValue()) {
					this.drawLine(0,0,255, posX, posY, posZ);
				}


				if (x1.type.contains("Emerald") && emerald.getValue()) {
					this.drawLine(0,255,0, posX, posY, posZ);
				}
				RenderUtil.stopDrawing();
			}
		}
	}
	private void drawLine(float red,float green,float blue, double x, double y, double z) {
		GL11.glEnable(2848);
		GL11.glColor3f(red / 255, green / 255, blue / 255);
		GL11.glLineWidth(1.0f);
		GL11.glBegin(1);
		GL11.glVertex3d(0.0, mc.player.getEyeHeight(), 0.0);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
		GL11.glDisable(2848);
	}
	@EventHandler
	public void onRenderTags(EventRender3D e) {
		Iterator x = list.iterator();
		if(Tags.getValue()) {
			while (x.hasNext()) {

				XrayBlock x1 = (XrayBlock) x.next();
				float[] arrd = new float[3];
				double posX = x1.x - RenderManager.renderPosX;
				double posY = x1.y - RenderManager.renderPosY;
				double posZ = x1.z - RenderManager.renderPosZ;
				boolean old = this.mc.gameSettings.viewBobbing;
				RenderUtil.startDrawing();
				this.mc.gameSettings.viewBobbing = false;
				this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
				this.mc.gameSettings.viewBobbing = old;

				if (x1.type.contains("Diamond") && diamond.getValue()) {
					arrd[0] = 0.5830f;
					arrd[1] = 1.0f;
					arrd[2] = 1.0f;
					rendertag("Diamond", posX, posY, posZ, x1.x, x1.y, x1.z,new Color(0,128,255).getRGB());
				}


				if (x1.type.contains("Gold")&&gold.getValue()) {
					arrd[0] = 0.1667f;
					arrd[1] = 1.0f;
					arrd[2] = 1.0f;
					rendertag("Gold", posX, posY, posZ, x1.x, x1.y, x1.z,new Color(255, 255, 0).getRGB());
				}


				if (x1.type.contains("Iron")&&iron.getValue()) {
					arrd[0] = 0.0676f;
					arrd[1] = 0.3224f;
					arrd[2] = 0.8392f;
					rendertag("Iron", posX, posY, posZ, x1.x, x1.y, x1.z, new Color(214,173,145).getRGB());
				}


				if (x1.type.contains("Redstone")&&redstone.getValue()) {
					arrd[0] = 0.0f;
					arrd[1] = 1.0f;
					arrd[2] = 1.0f;
					rendertag("RedStone", posX, posY, posZ, x1.x, x1.y, x1.z,new Color(255, 0, 0).getRGB());
				}

				if (x1.type.contains("Coal")&&coal.getValue()) {
					arrd[0] = 0.0f;
					arrd[1] = 0.0f;
					arrd[2] = 0.0f;
					rendertag("Coal", posX, posY, posZ, x1.x, x1.y, x1.z,new Color(50, 50, 50).getRGB());
				}


				if (x1.type.contains("Lapis")&&lapis.getValue()) {
					arrd[0] = 0.6082f;
					arrd[1] = 0.8323f;
					arrd[2] = 0.6314f;
					rendertag("Lapis", posX, posY, posZ, x1.x, x1.y, x1.z,new Color(27,74,161).getRGB());
				}


				if (x1.type.contains("Emerald")&&emerald.getValue()) {
					arrd[0] = 0.3965f;
					arrd[1] = 0.8959f;
					arrd[2] = 0.8667f;
					rendertag("Emerald", posX, posY, posZ, x1.x, x1.y, x1.z,new Color(23,221,98).getRGB());
				}


				RenderUtil.stopDrawing();
			}
		}
	}

	@EventHandler
	public void onEventBlockRenderSide(EventBlockRenderSide e) {
		if (cave.getValue()) {
			if (!(e.getSide() == EnumFacing.DOWN && e.minY > 0.0D ? true : (e.getSide() == EnumFacing.UP && e.maxY < 1.0D ? true : (e.getSide() == EnumFacing.NORTH && e.minZ > 0.0D ? true : (e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0D ? true : (e.getSide() == EnumFacing.WEST && e.minX > 0.0D ? true : (e.getSide() == EnumFacing.EAST && e.maxX < 1.0D ? true : !e.getWorld().getBlockState(e.pos).getBlock().isOpaqueCube()))))))) {
				return;
			}
		}
			if (mc.world.getBlockState(e.getPos().offset(e.getSide(), -1)).getBlock() instanceof BlockOre
					|| mc.world.getBlockState(e.getPos().offset(e.getSide(), -1))
							.getBlock() instanceof BlockRedstoneOre) {

				final float xDiff = (float) (mc.player.posX - e.pos.getX());
				final float yDiff = 0;
				final float zDiff = (float) (mc.player.posZ - e.pos.getZ());
				float dis = MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
				if (dis > 50)
					return;
				XrayBlock x = new XrayBlock(Math.round(e.pos.offset(e.getSide(), -1).getZ()),
						Math.round(e.pos.offset(e.getSide(), -1).getY()),
						Math.round(e.pos.offset(e.getSide(), -1).getX()),
						mc.world.getBlockState(e.pos.offset(e.getSide(), -1)).getBlock().getUnlocalizedName());
				if (!list.contains(x))
					list.add(x);
			}
		}

	public int getColor(int red, int green, int blue) {
		return getColor(red, green, blue, 255);
	}

	public static int getColor(int red, int green, int blue, int alpha) {
		int color = 0;
		color |= alpha << 24;
		color |= red << 16;
		color |= green << 8;
		color |= blue;
		return color;
	}

	private static float getSize(double x, double y, double z) {
		return (float) (mc.player.getDistance(x, y, z)) / 4.0f <= 2.0f ? 2.0f
				: (float) (mc.player.getDistance(x, y, z)) / 4.0f;
	}

	private static void startDrawing(double x, double y, double z, double StringX, double StringY, double StringZ) {
		float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
		double size = Config.zoomMode ? (double) (getSize(StringX, StringY, StringZ) / 10.0f) * 1.6
				: (double) (getSize(StringX, StringY, StringZ) / 10.0f) * 4.8;
		GL11.glPushMatrix();
		RenderUtilqkl.startDrawing();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glNormal3f((float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) (-mc.getRenderManager().playerViewY), (float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) mc.getRenderManager().playerViewX, (float) var10001, (float) 0.0f, (float) 0.0f);
		GL11.glScaled((double) (-0.01666666753590107 * size), (double) (-0.01666666753590107 * size),
				(double) (0.01666666753590107 * size));
	}

	private static void drawNames(String Str, int Color) {
		float xP = 2.2f;
		float width = (float) getWidth(Str) / 2.0f + xP;
		float w = width = (float) ((double) width + 2.5);
		float nw = -width - xP;
		float offset = getWidth(Str) + 4;
		RenderUtilqkl.drawBorderedRect(nw + 6.0f, -1.0f, width, 10.0f, 1.0f, new Color(20, 20, 20, 0).getRGB(),
				new Color(10, 10, 10, 200).getRGB());
		GlStateManager.disableDepth();
		drawString(Str, w - offset + 2, 0.0f, Color);
		GlStateManager.enableDepth();
	}

	private static void drawString(String text, float x, float y, int color) {
		mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
	}

	private static int getWidth(String text) {
		return mc.fontRendererObj.getStringWidth(text);
	}

	private static void stopDrawing() {
		RenderUtilqkl.stopDrawing();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	public static void rendertag(String Str, double x, double y, double z, double StringX, double StringY,
			double StringZ, int color) {
		y = (float) ((double) y + 1.55);
		startDrawing(x, y, z, StringX, StringY, StringZ);
		drawNames(Str, color);
		GL11.glColor4d((double) 1.0, (double) 1.0, (double) 1.0, (double) 1.0);
		stopDrawing();
	}

	public int getOpacity() {
		return this.opacity;
	}
}
