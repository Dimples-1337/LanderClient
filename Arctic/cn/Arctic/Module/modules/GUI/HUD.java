/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.GUI;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import cn.Arctic.Client;
import cn.Arctic.Api.CustomUI.Functions.PacketGraph;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Font.CFontRenderer;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.ShaderBlur;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.BlockObject;
import cn.Arctic.Util.Colors;
import cn.Arctic.Util.PaletteUtil;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.math.MathUtil;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.Util.render.RenderUtils;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import viamcp.ViaMCP;
import viamcp.protocols.ProtocolCollection;

public class HUD
extends Module {
	
	public static Option<Boolean> rainbow = new Option<Boolean>("Rainbow", true);
    public static Numbers<Double> r = new Numbers<Double>("R", 50d, 0.0, 255.0, 5.0);
    public static Numbers<Double> g = new Numbers<Double>("G", 120d, 0.0, 255.0, 5.0);
    public static Numbers<Double> b = new Numbers<Double>("B", 255d, 0.0, 255.0, 5.0);
    public static Numbers<Double> h = new Numbers<Double>("h", 255d, 0.0, 255.0, 5.0);
    public static Numbers<Double> slow = new Numbers<Double>("slow", 6.0,0.1,20.0,1.0);
    public static Option<Boolean> hudFade = new Option<Boolean>("Fade",true);
	public static Option<Boolean> logo = new Option<Boolean>("logo", true);
	public static Option<Boolean> smooth = new Option<Boolean>("smooth", true);
    public static Mode<Enum> widgetmode = new Mode("Widget", (Enum[]) WidgetMode.values(),
			(Enum) WidgetMode.None);
    public static boolean useFont;
   
    int posX = 3;
    int posY = 3;
    int x;
	int y;
	public static float hue;
    public static boolean shouldMove;
    public static Option<Boolean> blackhotbar = new Option<Boolean>("BlackHotBar", true);
    public static Option<Boolean> background = new Option<Boolean>("background",false);
    public static Option<Boolean> bk = new Option<Boolean>("bk",false);

    float width = 0;
    int ychat;
    private Mode<Enum> mode = new Mode("Mode", (Enum[])HUDMode.values(), (Enum)HUDMode.Lander);
    public HUD() {
        super("HUD", new String[]{"gui"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.setEnabled(true);
        this.setRemoved(true);
        this.addValues(this.mode,this.logo,this.widgetmode,smooth,this.hudFade, this.rainbow, this.r, this.g, this.b,slow,h,bk,background);
       
           
            
         //閿熺嫛绛规嫹ArrayList閿熸枻鎷烽敓鏂ゆ嫹绀�
    }



	@EventHandler
	public void Render2d(final EventRender2D e) {
//		ShaderBlur.blurAreaBoarder(200, 200, 60, 60, 100);
////		RenderUtils.drawLoadingCircle(100, 100);
////		RenderUtil.drawRect(200F, 200D, 260D, 260D, new Color(200, 200, 200, 50).getRGB());
//		
//		FontLoaders.CNMD45.drawString("B", 222, 206, Colors.YELLOW.c);
//		FontLoaders.CNMD45.drawString("B", 206, 206, Colors.WHITE.c);
//		FontLoaders.CNMD45.drawString("M", 215, 227, Colors.AQUA.c);
//		FontLoaders.CNMD45.drawString("C", 227, 227, Colors.AQUA.c);
		
		Client.instance.drawNotifications();

		hue += (float) 6.0 / 5.0f;
		if (hue > 255.0f) {
			hue = 0.0f;
		}
	}

	@EventHandler
	public void onPacketGraphServer(EventPacketRecieve event) {
		if (event.getPacket() instanceof Packet) {
			PacketGraph.onServerPacket();
		}
	}

	@EventHandler
	public void onPacketGraphClient(EventPacketSend event) {
		if (event.getPacket() instanceof Packet) {
			PacketGraph.onClientPacket();
		}
	}

    
	@EventHandler
    private void renderHud(EventRender2D event) {
        int rainbowTick=0;
        rainbowTick+=100;
        int rainbow2 = RenderUtil.astolfoRainbow(1,10,rainbowTick);
        if(logo.getValue()) {
            Color rainbow = new Color(Color.HSBtoRGB((float)((double)this.mc.player.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
            	HUD.shouldMove = true;
            	CFontRenderer ffff = FontLoaders.NMSL16;
            	String text = Client.name+" | "+ getRemoteIp() +  " | " + (Minecraft.getDebugFPS()) + "FPS"+ " | "+ mc.timer.timerSpeed + " Gamespeed"+" | "+new SimpleDateFormat("HH:mm:ss").format(new Date())  + " Time";
            	int width = Math.max(100, ffff.getStringWidth(text) + 10);
        		ShaderBlur.blurAreaBoarder(2, 5, 0 + width, 18,10);
            	Gui.drawRect(2, 5, 2 + width, 22, new Color(0,0,0,80).getRGB());
            	Gui.drawRect(2, 5, 2 + width, 6.5, rainbow2);
            	FontLoaders2.msFont16.drawCenteredString(text, width / 2 +2, (float) ((22 + 6.5) / 2 - ffff.getStringHeight(text) / 2), new Color(255,255,255).getRGB());
        }
        else if(!logo.getValue()){
        	RenderUtils.drawImage(new ResourceLocation("Melody/icon/Lander.png"),-40,-5, 160, 50);
        }

        if (!this.mc.gameSettings.showDebugInfo) {
            String name;
            ArrayList<Module> sorted = new ArrayList<Module>();
            Client.instance.getModuleManager();
            
            //閿熸枻鎷烽敓鏂ゆ嫹鑳ら敓鏂ゆ嫹閿熸枻鎷烽敓缁烇拷 ArrayList閿熸枻鎷�
            for (Module m : ModuleManager.getModules()) {
                if (!m.isEnabled() || m.wasRemoved()) continue;  //m.wasRemoved() 閿熸枻鎷烽敓鑴氱鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鎴鎷�
                sorted.add(m);   //閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鎾呮嫹閿熸枻鎷烽敓锟�
                
            } 
            sorted.sort((o1, o2) -> FontLoaders.NMSL18.getStringWidth(o2.getName() + (o2.getSuffix() != "" ? " " : "") + o2.getSuffix()) - FontLoaders.NMSL18.getStringWidth(o1.getName() + (o1.getSuffix() != "" ? " " : "") + o1.getSuffix()));
            int y = 0;
            int color=0;
                for (Module m : sorted) {
                	int nextIndex = sorted.indexOf(m) +1;
        			Module nextModule = null;
        			if (sorted.size() > nextIndex) {
        				nextModule = this.getNextEnabledModule(sorted, nextIndex);
        			}
        			 ScaledResolution sr = new ScaledResolution(mc);
                    name = m.getSuffix().isEmpty() ? m.getName() : String.format("%s%s", m.getName(), m.getSuffix());
                    float x = RenderUtil.width() - FontLoaders.NMSL18.getStringWidth(name);
                    Color rainbow = new Color(Color.HSBtoRGB((float) ((double) mc.player.ticksExisted / 50.0 + Math.sin((double) (rainbowTick + (y - 4) / 12) / 50.0 * 1.6)) % 1.0f, 0.3f, 1));
                    switch (mode.getModeAsString()){
	                    case "Lander":{
	                    	 if (hudFade.getValue()) {
	                             color = PaletteUtil.fade(new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue()), 10000, sorted.indexOf(nextModule) * 300 + 5000).getRGB();
	                         } else {
	                             color = new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB();
	                         }
	                    	if (m.isEnabled()) {
	                            m.setRemoved(false);
	                            if (mc.player.ticksExisted >= 30) {
	                                m.setAnimx(Math.min(m.getAnimx() + FontLoaders.NMSL18.getStringWidth(name) / 12, FontLoaders.NMSL18.getStringWidth(name)));
	                            } else {
	                                m.setAnimx(FontLoaders.NMSL18.getStringWidth(name));
	                            }
	                        } else {
	                            if (m.getAnimx() <= 0) {
	                                m.setRemoved(true);
	                            } else {
	                                if (mc.player.ticksExisted >= 30) {
	                                    m.setAnimx(Math.max(m.getAnimx() - FontLoaders.NMSL18.getStringWidth(name) / 12, 0));
	                                } else {
	                                    m.setAnimx(0);
	                                }
	                            }
	                        }
	                    	if (background.getValue()) {
	                            Gui.drawRect(x-4, y, sr.getScaledWidth(), y + 10, new Color(20,20,20,90).getRGB());
	                        }
	                    	  if (bk.getValue()) {
	                    		  //shu
	                    		  RenderUtil.drawBorderedRect((int) (x - 5), y , (int) (x - 5),
	  	        						y + 11, 0.1f, this.rainbow.getValue() != false ? rainbow.getRGB() : new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB(), this.rainbow.getValue() != false ? rainbow.getRGB() : new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
	  	                    	if (nextModule != null) {
	  								if (this.getNextEnabledModule(sorted, nextIndex).getSuffix() != "") {
	  									//shangheng
	  									Gui.drawRect(x - 5d, y + 11d, RenderUtil.width()
	  											- FontLoaders.NMSL18.getStringWidth(this.getNextEnabledModule(sorted, nextIndex).getName()
	  													+ this.getNextEnabledModule(sorted, nextIndex).getSuffix())
	  											- 5d, y + 10d,
	  											this.rainbow.getValue() != false ? rainbow.getRGB() : new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
	  								} else {
	  									//xiaheng
	  									Gui.drawRect(x - 5d, y + 11d,
	  											RenderUtil.width() - FontLoaders.NMSL18.getStringWidth(
	  													this.getNextEnabledModule(sorted, nextIndex).getName()) - 5d,
	  											y + 10d,
	  											this.rainbow.getValue() != false ? rainbow.getRGB() : new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
	  								}
	  							} else {
	  								Gui.drawRect(x - 5d, y + 11d, RenderUtil.width(), y + 10d,
	  										this.rainbow.getValue() != false ? rainbow.getRGB() : new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
	  							}
	                          }
	                    	 FontLoaders.NMSL18.drawStringWithShadow(name, x-2 , y+2, this.rainbow.getValue() != false ? rainbow.getRGB() : new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
	                         break;
	                    }
                    }

                    if (++rainbowTick > 1000) {
                        rainbowTick = 0;
                    }
                    y += 10;
                }
                //閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟�
                    for (int slot = 3, xOffset = 0; slot >= 0; slot--) {
               			ItemStack stack = mc.player.inventory.armorItemInSlot(slot);
               			GuiIngame gi = new GuiIngame(mc);
               			if (stack != null) {
               				mc.getRenderItem().renderItemIntoGUI(stack, RenderUtil.width() / 2 + 6 - xOffset, RenderUtil.height() - 55);
               				GL11.glDisable(GL11.GL_DEPTH_TEST);
               				GL11.glScalef(0.5F, 0.5F, 0.5F);
               				GL11.glScalef(2F, 2F, 2F);
               				GL11.glEnable(GL11.GL_DEPTH_TEST);
               				xOffset -= 18;
               			}
               		}
                }
        ScaledResolution sr = new ScaledResolution(mc);
        double xDist = mc.player.posX - mc.player.lastTickPosX;
        double zDist = mc.player.posZ - mc.player.lastTickPosZ;
        double moveSpeed = Math.sqrt(xDist * xDist + zDist * zDist) * 2;
        double speed = MathUtil.round(moveSpeed * 10, 2);
        int alph = 255;
        String version = ProtocolCollection.getProtocolById(ViaMCP.getInstance().getVersion()).getName();
        FontLoaders.NMSL16.drawStringWithShadow( EnumChatFormatting.GRAY+"Build-"+ EnumChatFormatting.WHITE+Client.version+EnumChatFormatting.GRAY+" Via:"+EnumChatFormatting.GREEN+version, RenderUtil.width()-FontLoaders.NMSL16.getStringWidth("Build-"+Client.version+EnumChatFormatting.GRAY+" Via:"+EnumChatFormatting.GREEN+version)-2,RenderUtil.height()-10, new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
        FontLoaders.NMSL16.drawStringWithShadow( EnumChatFormatting.GRAY+"FPS:" +  EnumChatFormatting.WHITE+mc.getDebugFPS()+ EnumChatFormatting.GRAY+"  XYZ:"+ EnumChatFormatting.WHITE+MathHelper.floor_double(this.mc.player.posX) + " " + MathHelper.floor_double(this.mc.player.posY) + " " + MathHelper.floor_double(this.mc.player.posZ)+ EnumChatFormatting.GRAY+"  Block/s:"+ EnumChatFormatting.WHITE+speed, new ScaledResolution(this.mc).getScaledWidth()/250-this.width, new ScaledResolution(this.mc).getScaledHeight()-9,new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
        
        this.drawPotionStatus(new ScaledResolution(this.mc));
        
        if (widgetmode.getValue() == WidgetMode.Widget_9) {
			int width = 0;
			int height = 0;
			switch (widgetmode.getValue().toString()) {
			case "Widget_9": {
				width = 505;
				height = 512;
				break;
			}
			}
			width *= 0.25;
			height *= 0.25;
//        	if(blackhotbar.getValue()) {
//        		RenderUtil.drawCustomImage(RenderUtil.width() - 100 - width,RenderUtil.height() - height - (mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 22),width,height,new ResourceLocation("Thunder/widget/" + widgetmode.getValue().toString() + ".png"));
//        	}else {
			RenderUtil.drawCustomImage(RenderUtil.width() / 2 + 300 - width,
					RenderUtil.height() - height - (mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0), width,
					height, new ResourceLocation("Lander1/Widget_9.png" ));
        	}
		}


    


	  private Module getNextEnabledModule(List<Module> modules, int startingIndex) {
	        int modulesSize = modules.size();
	        for (int i = startingIndex; i < modulesSize; ++i) {
	            Module module = modules.get(i);
	            if (!module.isEnabled())
	                continue;
	            return module;
	        }
	        return null;
	    }
	public static String getRemoteIp() {
        String serverIp = "Singleplayer";

        if (mc.world.isRemote) {
            final ServerData serverData = mc.getCurrentServerData();
            if(serverData != null)
                serverIp = serverData.serverIP;
        }

        return serverIp;
    }
	


    

	private void drawPotionStatus(ScaledResolution sr) {
		List<PotionEffect> potions = new ArrayList<>();
		for (Object o : mc.player.getActivePotionEffects())
			potions.add((PotionEffect) o);
		potions.sort(Comparator.comparingDouble(effect -> -mc.fontRendererObj
				.getStringWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

		float pY = (mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)) ? -15 : -2;
		for (PotionEffect effect : potions) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String name = I18n.format(potion.getName());
			String PType = "";
			if (effect.getAmplifier() == 1) {
				name = name + " II";
			} else if (effect.getAmplifier() == 2) {
				name = name + " III";
			} else if (effect.getAmplifier() == 3) {
				name = name + " IV";
			}
			if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
				PType = PType + "\2476 " + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				PType = PType + "\247c " + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				PType = PType + "\2477 " + Potion.getDurationString(effect);
			}
			mc.fontRendererObj.drawStringWithShadow(name,
					sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(name + PType),
					sr.getScaledHeight() - 25 + pY, potion.getLiquidColor());
			mc.fontRendererObj.drawStringWithShadow(PType,
					sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType), sr.getScaledHeight() - 25 + pY, -1);
			pY -= 9;
		}
	}
    
    

    static enum HUDMode {
        Lander;
    }
    static enum WidgetMode {
		None, Widget_9;
	}

}

