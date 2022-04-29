package cn.Noble.Module.modules.RENDER;

import java.awt.Color;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.Xray.EventBlockRenderSide;
import cn.Noble.Event.events.EventRender3D;
import cn.Noble.Event.events.EventTick;
import cn.Noble.Font.CFontRenderer;
import cn.Noble.Font.FontLoaders;
import cn.Noble.GUI.Xray.XrayBlock;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Colors;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.render.RenderUtil;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class Xray extends Module {
	public static final HashSet<Integer> blockIDs = new HashSet();
	private int opacity = 160;
	private Boolean tre = false;
	private static CFontRenderer font = FontLoaders.NMSL18;
	public List<Integer> KEY_IDS = Lists.newArrayList(10, 11, 8, 9, 14, 15, 16, 21, 41, 42, 46, 48, 52, 56, 57, 61, 62,
			73, 74, 84, 89, 103, 116, 117, 118, 120, 129, 133, 137, 145, 152, 153, 154);
	public Numbers<Double> KEY_OPACITY = new Numbers<Double>("Opacity", 160.0, 0.0, 255.0, 5.0);
	public static Option<Boolean> CAVEFINDER = new Option<Boolean>("CaveFinder", true);
	public static Option<Boolean> Tracers = new Option<Boolean>("Tracers", false);
	public static Option<Boolean> ESP = new Option<Boolean>("ESP", true);
	public static Option<Boolean> Dia = new Option<Boolean>("Diamond", true);
	public static Option<Boolean> Iron = new Option<Boolean>("Iron", true);
	public static Option<Boolean> Gold = new Option<Boolean>("Gold", true);
	public static Option<Boolean> Qita = new Option<Boolean>("Qita", true);
	public static Option<Boolean> Emerald = new Option<Boolean>("Emerald", true);
	public static Option<Boolean> Lapis = new Option<Boolean>("Lapis", true);
	public static Option<Boolean> RedStone = new Option<Boolean>("RedStone", true);
	public static Option<Boolean> AuthESP = new Option<Boolean>("AuthESP", false);
	public CopyOnWriteArrayList list = new CopyOnWriteArrayList();

	private static TimerUtil refresh = new TimerUtil();

	public Xray() {
		super("Xray", new String[] { "Xray" }, ModuleType.Render);
		this.addValues(KEY_OPACITY, CAVEFINDER, ESP, Tracers, AuthESP, Dia, Iron, Gold, Qita, RedStone, Emerald, Lapis);
		// settings.put(Xray.KEY_OPACITY, new Setting<>(Xray.KEY_OPACITY, 160, "Opacity
		// for blocks you want to ignore.", 5, 0, 255));
		// settings.put(Xray.CAVEFINDER, new Setting<>(CAVEFINDER, false, "Only show
		// blocks touching air."));
	}

	@Override
	public void onEnable() {
		blockIDs.clear();
		this.tre = true;
		opacity = this.KEY_OPACITY.getValue().intValue();
		try {
			for (Integer o : KEY_IDS) {
				blockIDs.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mc.renderGlobal.loadRenderers();
	}
	
	@EventHandler
	private void onTick(EventTick event) {
		if(refresh.hasReached(10000)) {
			refresh();
			Helper.sendMessage("Xray Refresh.");
			refresh.reset();
		}
	}

	private void refresh() {
		blockIDs.clear();
		list.clear();
		this.tre = true;
		opacity = this.KEY_OPACITY.getValue().intValue();
		try {
			for (Integer o : KEY_IDS) {
				blockIDs.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mc.renderGlobal.loadRenderers();
	}

	@EventHandler
	public void onEvent(EventRender3D event) {
		Iterator x = list.iterator();

		while (x.hasNext()) {
			XrayBlock x1 = (XrayBlock) x.next();
			if (Dia.getValue()) {
				if (x1.type.contains("Diamond") || x1.type.contains("钻石"))
					RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX,
							x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
							getColor(30, 144, 255), new Color(30, 144, 255).getRGB(), 1f);
			}
			if (RedStone.getValue().booleanValue()) {
				if (x1.type.contains("Redstone") || x1.type.contains("红石")) {
					RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX,
							x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
							getColor(255, 0, 0), new Color(255, 0, 0).getRGB(), 1f);
				}
			}
			if (Iron.getValue()) {
				if (x1.type.contains("Iron") || x1.type.contains("铁"))
					RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX,
							x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
							getColor(184, 134, 11), new Color(184, 134, 11).getRGB(), 1f);
				if (Gold.getValue()) {
					if (x1.type.contains("Gold") || x1.type.contains("金"))
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX,
								x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
								getColor(255, 255, 0), new Color(255, 255, 0).getRGB(), 1f);
				}
				if (Emerald.getValue()) {
					if (x1.type.contains("Emerald") || x1.type.contains("绿宝石")) {
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX,
								x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
								getColor(23, 221, 98), new Color(23, 221, 98).getRGB(), 1f);
					}
				}
				if (Lapis.getValue()) {
					if ((x1.type.contains("Lapis") || x1.type.contains("青金石"))) {
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX,
								x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
								getColor(0, 0, 255), new Color(0, 0, 255).getRGB(), 1f);
					}
				}
				if (Qita.getValue()) {
					if ((x1.type.contains("Qita") || x1.type.contains("煤炭"))) {
						RenderUtil.drawblock(x1.x - mc.getRenderManager().renderPosX,
								x1.y - mc.getRenderManager().renderPosY, x1.z - mc.getRenderManager().renderPosZ,
								Colors.getColor(228, 228, 65, 50), new Color(155, 255, 110).getRGB(), 1f);
					}
				}

			}
		}
	}

	@EventHandler
	private void on3D(EventRender3D e) {
		if (this.tre.booleanValue()) {
			on3DRender(e);
		}
	}

	private void on3DRender(EventRender3D e) {
		Iterator x = list.iterator();
		if (Tracers.getValue()) {
			while (x.hasNext()) {
				XrayBlock x1 = (XrayBlock) x.next();
				double[] arrd = new double[3];
				double posX = x1.x - mc.getRenderManager().viewerPosX;
				double posY = x1.y - mc.getRenderManager().viewerPosY;
				double posZ = x1.z - mc.getRenderManager().viewerPosZ;
				boolean old = this.mc.gameSettings.viewBobbing;
				RenderUtil.startDrawing();
				this.mc.gameSettings.viewBobbing = false;
				this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
				this.mc.gameSettings.viewBobbing = old;
				if (this.Emerald.getValue()) {
					if ((x1.type.contains("Emerald") || x1.type.contains("绿宝石"))) {
						arrd[0] = 23.0;
						arrd[1] = 221.0;
						arrd[2] = 98.0;
						this.drawLine(arrd, posX, posY, posZ);
					}
				}
				if (this.Dia.getValue()) {
					if ((x1.type.contains("Diamond") || x1.type.contains("钻石"))) {
						arrd[0] = 0.0;
						arrd[1] = 128.0;
						arrd[2] = 255.0;
						this.drawLine(arrd, posX, posY, posZ);
					}
				}
				if (Gold.getValue()) {
					if ((x1.type.contains("Gold") || x1.type.contains("金"))) {
						arrd[0] = 255.0;
						arrd[1] = 255.0;
						arrd[2] = 0.0;
						this.drawLine(arrd, posX, posY, posZ);
					}
				}

				if (this.Iron.getValue()) {
					if ((x1.type.contains("Iron") || x1.type.contains("铁"))) {
						arrd[0] = 184.0;
						arrd[1] = 134.0;
						arrd[2] = 140.0;
						this.drawLine(arrd, posX, posY, posZ);
					}
				}
				if (this.RedStone.getValue()) {
					if ((x1.type.contains("Redstone") || x1.type.contains("红石"))) {
						arrd[0] = 255.0;
						arrd[1] = 0.0;
						arrd[2] = 0.0;
						this.drawLine(arrd, posX, posY, posZ);
					}
				}
				if (this.Lapis.getValue()) {
					if ((x1.type.contains("Lapis") || x1.type.contains("青金石"))) {
						arrd[0] = 0.0;
						arrd[1] = 0.0;
						arrd[2] = 255.0;
						this.drawLine(arrd, posX, posY, posZ);
					}
				}
				RenderUtil.stopDrawing();
			}
		}
	}

	private static void drawString(String text, float x, float y, int color) {
		font.drawStringWithShadow(text, x, y, color);
	}

	private static float getSize(double x, double y, double z) {
		return (float) (mc.player.getDistance(x, y, z)) / 4.0f <= 2.0f ? 2.0f
				: (float) (mc.player.getDistance(x, y, z)) / 4.0f;
	}

	private static void startDrawing(double x, double y, double z, double StringX, double StringY, double StringZ) {
		float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
		double size = Config.zoomMode ? (double) (getSize(StringX, StringY, StringZ) / 10.0f) * 1.6
				: (double) (getSize(StringX, StringY, StringZ) / 10.0f) * 18;
		GL11.glPushMatrix();
		RenderUtil.startDrawing();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glNormal3f((float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) (-mc.getRenderManager().playerViewY), (float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) mc.getRenderManager().playerViewX, (float) var10001, (float) 0.0f, (float) 0.0f);
		GL11.glScaled((double) (-0.01666666753590107 * size), (double) (-0.01666666753590107 * size),
				(double) (0.01666666753590107 * size));
	}

	private static void stopDrawing() {
		RenderUtil.stopDrawing();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	public static int getColor(int red, int green, int blue) {
		return getColor(red, green, blue, 255);
	}

	private void drawLine(double[] color, double x, double y, double z) {

		GL11.glEnable((int) 2848);
		GL11.glColor3d(color[0], color[1], color[2]);
		GL11.glLineWidth((float) 0.5f);
		GL11.glBegin((int) 1);
		GL11.glVertex3d((double) 0.0, (double) this.mc.player.getEyeHeight(), (double) 0.0);
		GL11.glVertex3d((double) x + 0.5, (double) y + 0.5, (double) z + 0.5);
		GL11.glEnd();
		GL11.glDisable((int) 2848);

	}

	public static int getColor(int red, int green, int blue, int alpha) {
		int color = 0;
		color |= alpha << 24;
		color |= red << 16;
		color |= green << 8;
		color |= blue;
		return color;
	}

	@EventHandler
	public void onEvent(EventBlockRenderSide e) {
//		e.setCancelled(true);
		e.getState().getBlock();
		if (!CAVEFINDER.getValue() && containsID(Block.getIdFromBlock(e.getState().getBlock()))
				&& !(e.getSide() == EnumFacing.DOWN && e.minY > 0.0D ? true
						: (e.getSide() == EnumFacing.UP && e.maxY < 1.0D ? true
								: (e.getSide() == EnumFacing.NORTH && e.minZ > 0.0D ? true
										: (e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0D ? true
												: (e.getSide() == EnumFacing.WEST && e.minX > 0.0D ? true
														: (e.getSide() == EnumFacing.EAST && e.maxX < 1.0D ? true
																: !e.getWorld().getBlockState(e.pos).getBlock()
																		.isOpaqueCube()))))))) {
			e.setToRender(true);
		} else {
			if (!CAVEFINDER.getValue()) {
				e.setCancelled(true);
			}
		}
		if (ESP.getValue()) {
			if ((e.getSide() == EnumFacing.DOWN && e.minY > 0.0D ? true
					: (e.getSide() == EnumFacing.UP && e.maxY < 1.0D ? true
							: (e.getSide() == EnumFacing.NORTH && e.minZ > 0.0D ? true
									: (e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0D ? true
											: (e.getSide() == EnumFacing.WEST && e.minX > 0.0D ? true
													: (e.getSide() == EnumFacing.EAST && e.maxX < 1.0D ? true
															: !e.getWorld().getBlockState(e.pos).getBlock()
																	.isOpaqueCube()))))))) {
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
					if (AuthESP.getValue()) {
						for (EnumFacing e1 : EnumFacing.values()) {
							XrayBlock x1 = new XrayBlock(Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getZ()),
									Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getY()),
									Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getX()),
									mc.world.getBlockState(e.pos.offset(e.getSide(), -1).offset(e1, 1)).getBlock()
											.getUnlocalizedName());
							boolean b = false;
							if (x.type.contains("Diamond") && x1.type.contains("Diamond")) {
								b = true;
							} else if (x.type.contains("Iron") && x1.type.contains("Iron")) {
								b = true;
							} else if (x.type.contains("Gold") && x1.type.contains("Gold")) {
								b = true;
							} else if (x.type.contains("Red") && x1.type.contains("Red")) {
								b = true;
							} else if (x.type.contains("Coal") && x1.type.contains("Coal")) {
								b = true;
							}
							if (b) {
								if (!list.contains(x1))
									list.add(x1);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
		this.tre = false;
	}

	public boolean containsID(int id) {
		return blockIDs.contains(id);
	}

	public int getOpacity() {
		return this.opacity;
	}
}
