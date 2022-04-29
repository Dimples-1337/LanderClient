package cn.Arctic.Api.CustomUI.Functions.UI;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.Arctic.Api.CustomUI.HUDApi;
import cn.Arctic.Api.CustomUI.HUDScreen;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventChat;
import cn.Arctic.Font.me.superskidder.FontLoaders2;
import cn.Arctic.GUI.ShaderBlur;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.GetPlayerList;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.Util.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class PlayerList extends HUDApi {
    private static final String[] killMessage = new String[]{
            "was killed by ",
            "was thrown into the void by ",
            "was thrown off a cliff by ",
            "was deleted by ",
            "was purified by ",
            "was scared off an edge by ",
            "was socked by ",
            "was oinked by ",
            "was not spicy enough for ",
            " morreu para ",
            " foi jogado no void por ",
            " was slain by ",
            " foi morto por "
    };
    private final List<GetPlayerList> players = new CopyOnWriteArrayList<>();
    private GetPlayerList no1Player;
	public PlayerList() {
		super("PlayerList", 414, 413);
		this.setEnabled(true);
	}
    public void clearPlayers() {
        this.players.clear();
        this.no1Player = null;
    }

    public void onChat(String chatMessage) {
        block14:
        {
            if (this.mc.player == null) {
                return;
            }
            String playerName = null;
            try {
                for (String s : killMessage) {
                    if (chatMessage.contains(s)) {
                        playerName = chatMessage.split(s)[1];

                        if (playerName.contains("using [")) {
                            playerName = playerName.split("using \\[")[0];
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (playerName == null || playerName.isEmpty()) {
                return;
            }

            if (!this.players.isEmpty()) {
                for (GetPlayerList player : this.players) {
                    if (!player.name.equals(playerName)) continue;
                    player.kills++;
                    break block14;
                }
            }
            this.players.add(new GetPlayerList(playerName, 1));
        }
    }
	
	
	@Override
	public void onRender() {
//            RenderUtils.drawBorderedRect(this.x - 3, this.y - 3, this.x + FontLoaders2.msFont18.getStringWidth("PlayerList") + 83, this.y + this.mc.fontRendererObj.FONT_HEIGHT + 6, 1.0f, new Color(0, 0, 255).getRGB(), new Color(0, 0, 0, 0).getRGB());
//            FontLoaders2.msFont18.drawString("PlayerList X:" + this.x + " Y:" + this.y, this.x - 3, this.y - 13, -1);
        
        if (!this.mc.player.isEntityAlive()) {
            this.players.clear();
            this.no1Player = null;
        }
        float textY = this.y;
        RenderUtil.drawRect(this.x, this.y, this.x + FontLoaders2.msFont18.getStringWidth("PlayerList") + 80, this.y + this.mc.fontRendererObj.FONT_HEIGHT + 3,new Color(0, 0, 0).getRGB());
        RenderUtil.drawGradientSideways(this.x, this.y, this.x + FontLoaders2.msFont18.getStringWidth("PlayerList") + 80, this.y + this.mc.fontRendererObj.FONT_HEIGHT + 3, new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),140).getRGB(), new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue()).getRGB());
        FontLoaders2.msFont18.drawString("PlayerList", this.x + 35, this.y+1, new Color(255, 255, 255).getRGB());
        if (this.mc.player == null || this.mc.currentScreen instanceof HUDScreen) {
            return;
        }
        this.players.sort((o1, o2) -> o2.kills - o1.kills);
        for (GetPlayerList player : this.players) {
            if (player == this.players.get(0)) {
                this.no1Player = player;
            }
            RenderUtils.drawRect(this.x, textY + (float)this.mc.fontRendererObj.FONT_HEIGHT + 3.0f, this.x + FontLoaders2.msFont18.getStringWidth("PlayerList") + 80, textY + (float)this.mc.fontRendererObj.FONT_HEIGHT + 13.0f, new Color(30, 30, 35, 240).getRGB());
            if (player == this.no1Player) {
                FontLoaders2.msFont18.drawString(EnumChatFormatting.YELLOW + "★", this.x + 2, this.mc.fontRendererObj.FONT_HEIGHT + 1.5f + (int) textY, -1);
            }
            FontLoaders2.msFont18.drawString(player.name, (float)(this.x + (player == this.no1Player ? 12 : 3)), (float)this.mc.fontRendererObj.FONT_HEIGHT + 2f + textY, -1);
            String killString;
            switch (player.kills) {
                case 0:
                case 1:
                case -1:
                    killString = "kill";
                    break;
                default:
                    killString = "kills";
                    break;
            }
            FontLoaders2.msFont18.drawString(player.kills + " " + killString, (float)(this.x + FontLoaders2.msFont18.getStringWidth("PlayerList") + 83 - FontLoaders2.msFont18.getStringWidth(player.kills + killString) - 2 - (killString.equalsIgnoreCase("kill") ? 3 : 1)), (float)this.mc.fontRendererObj.FONT_HEIGHT + 2.5f + textY, -1);
            textY += 10.0f;
        }
		
	}
	
	@Override
	public void InScreenRender() {
		onRender();	
}

}