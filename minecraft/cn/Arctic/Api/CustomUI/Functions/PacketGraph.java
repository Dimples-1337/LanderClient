package cn.Arctic.Api.CustomUI.Functions;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.Arctic.Api.CustomUI.HUDApi;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.Util.BlockObject;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.Util.render.RenderUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketGraph extends HUDApi {
	
	private List<BlockObject> clientBlocks = new CopyOnWriteArrayList<>();
	private List<BlockObject> serverBlocks = new CopyOnWriteArrayList<>();
	private TimerUtil timerUtil = new TimerUtil(), secTimerUtil = new TimerUtil();

	private static int clientPackets;
	private static int serverPackets;
	private static int secClientPackets;
	private static int secServerPackets;
	private int renderSecClientPackets;
	private int renderSecServerPackets;

	public PacketGraph() {
		super("PacketGraph", 52, 5);
	}

	public void clear() {
		clientPackets = 0;
		serverPackets = 0;
		secClientPackets = 0;
		secServerPackets = 0;
		renderSecClientPackets = 0;
		renderSecServerPackets = 0;
		clientBlocks.clear();
		serverBlocks.clear();
		timerUtil.reset();
		secTimerUtil.reset();
	}

	public static void onClientPacket() {
		clientPackets++;
		secClientPackets++;
	}

	public static void onServerPacket() {
		serverPackets++;
		secServerPackets++;
	}

	@Override
	public void onRender() {
		RenderUtils.drawBorderedRect(x - 3, y - 68, x + 153, y + 74, 1.0f, new Color(20, 220, 120, 198).getRGB(),
				new Color(130, 130, 130, 108).getRGB());

		if (timerUtil.hasReached(50)) {
			clientBlocks.forEach(blockObject -> blockObject.x--);
			clientBlocks.add(new BlockObject(x + 150, Math.min(clientPackets, 55)));
			clientPackets = 0;

			serverBlocks.forEach(blockObject -> blockObject.x--);
			serverBlocks.add(new BlockObject(x + 150, Math.min(serverPackets, 55)));
			serverPackets = 0;
			timerUtil.reset();
		}

		if (secTimerUtil.hasReached(1000)) {
			renderSecClientPackets = secClientPackets;
			renderSecServerPackets = secServerPackets;
			secClientPackets = 0;
			secServerPackets = 0;
			secTimerUtil.reset();
		}

		int graphY = y;
		for (int i = 0; i < 2; i++) {
			drawGraph(i, x, graphY);
			graphY += 68;
		}

		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		if (!clientBlocks.isEmpty()) {
			BlockObject firstBlock = clientBlocks.get(clientBlocks.size() - 1);
			FontLoaders2.msFont25.drawString("< avg: " + firstBlock.height, x * 2 + 301, y * 2 - firstBlock.height * 2 - 4,
					-1);
		}
		if (!serverBlocks.isEmpty()) {
			BlockObject firstBlock = serverBlocks.get(serverBlocks.size() - 1);
			FontLoaders2.msFont25.drawString("< avg: " + firstBlock.height, x * 2 + 301,
					(y + 68) * 2 - firstBlock.height * 2 - 4, -1);
		}
		GL11.glPopMatrix();

		clientBlocks.removeIf(block -> block.x < x);
		serverBlocks.removeIf(block -> block.x < x);
	}

	@Override
	public void InScreenRender() {
		
		RenderUtils.drawBorderedRect(x - 3, y - 68, x + 153, y + 74, 1.0f, new Color(20, 220, 120, 198).getRGB(),
				new Color(130, 130, 130, 108).getRGB());
		
		if (timerUtil.hasReached(50)) {
			clientBlocks.forEach(blockObject -> blockObject.x--);
			clientBlocks.add(new BlockObject(x + 150, Math.min(clientPackets, 55)));
			clientPackets = 0;

			serverBlocks.forEach(blockObject -> blockObject.x--);
			serverBlocks.add(new BlockObject(x + 150, Math.min(serverPackets, 55)));
			serverPackets = 0;
			timerUtil.reset();
		}

		if (secTimerUtil.hasReached(1000)) {
			renderSecClientPackets = secClientPackets;
			renderSecServerPackets = secServerPackets;
			secClientPackets = 0;
			secServerPackets = 0;
			secTimerUtil.reset();
		}

		int graphY = y;
		for (int i = 0; i < 2; i++) {
			drawGraph(i, x, graphY);
			graphY += 68;
		}

		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		if (!clientBlocks.isEmpty()) {
			BlockObject firstBlock = clientBlocks.get(clientBlocks.size() - 1);
			FontLoaders2.msFont25.drawString("< avg: " + firstBlock.height, x * 2 + 301, y * 2 - firstBlock.height * 2 - 4,
					-1);
		}
		if (!serverBlocks.isEmpty()) {
			BlockObject firstBlock = serverBlocks.get(serverBlocks.size() - 1);
			FontLoaders2.msFont25.drawString("< avg: " + firstBlock.height, x * 2 + 301,
					(y + 68) * 2 - firstBlock.height * 2 - 4, -1);
		}
		GL11.glPopMatrix();

		clientBlocks.removeIf(block -> block.x < x);
		serverBlocks.removeIf(block -> block.x < x);
		super.InScreenRender();
	}

	private void drawGraph(int mode, int x, int y) {
		boolean isClient = mode == 0;
		RenderUtils.drawRect(x, y + 0.5, x + 150, y - 60, new Color(0, 0, 0, 80).getRGB());
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		String secString = isClient ? renderSecClientPackets + " packets/sec" : renderSecServerPackets + " packets/sec";
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		FontLoaders2.msFont25.drawString(isClient ? "Outgoing packets" : "Incoming packets", x * 2, y * 2 - 129, -1);
		FontLoaders2.msFont25.drawString(secString, (x * 2) + 300 - (FontLoaders2.msFont25.getStringWidth(secString)),
				y * 2 - 129, -1);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(1.5f);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBegin(GL11.GL_LINES);
		int rainbowTicks = 0;
		List<BlockObject> list = isClient ? clientBlocks : serverBlocks;
		for (BlockObject block : list) {
			GL11.glColor4f(1 , 1, 1, 1);

			GL11.glVertex2d(block.x, y - block.height);
			try {
				BlockObject lastBlock = list.get(list.indexOf(block) + 1);
				GL11.glVertex2d(block.x + 1, y - lastBlock.height);
			} catch (Exception ignored) {
			}
			rainbowTicks += 300;
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GlStateManager.resetColor();
	}
}
