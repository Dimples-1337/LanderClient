
package cn.Arctic.Module.modules.RENDER;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.FriendManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Module.modules.PLAYER.Teams;
import cn.Arctic.Util.math.Vec3f;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Option;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;


public class ESP extends Module {
    private ArrayList<Vec3f> points = new ArrayList();
    private static Map<EntityPlayer, float[][]> entities = new HashMap<EntityPlayer, float[][]>();
    public static Mode<Enum> mode = new Mode("Mode", (Enum[]) ESPMode.values(), (Enum) ESPMode.Other2D);
    private static Option<Boolean> self = new Option<Boolean>("Self", false);
    
    float h;
    public static boolean renderNameTags = true;

    public ESP() {
        super("ESP", new String[] { "outline", "wallhack" }, ModuleType.Render);
        addValues(mode,self);
        removed = true;
        
           
            
        int i = 0;
        while (i < 8) {
            points.add(new Vec3f());
            ++i;
        }
        
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        setSuffix(mode.getValue());
        setColor(new Color(125,100,233).getRGB());
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        if (h > 255) {
            h = 0;
        }
        h += 0.1;
        if (mode.getValue() == ESPMode.otherBox) {
            doBoxESP(event);
        }
        if (mode.getValue() == ESPMode.Other2D) {
            doOther2DESP(event);
        }
        if (mode.getValue() == ESPMode.New2D) {
            doCornerESP(event);
        }
    }

    private void startEnd(boolean revert) {
        if (revert) {
            GlStateManager.enableBlend();
            GL11.glEnable((int) 2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint((int) 3154, (int) 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable((int) 2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!revert);
    }

    private boolean doesntContain(EntityPlayer var0) {
        return !mc.world.playerEntities.contains(var0);
    }

    private void drawSkeleton(EventRender3D event, EntityPlayer e) {
        float[][] entPos;
        Color color = new Color(16775672);
        if ((entPos = entities.get(e)) != null && e.isEntityAlive() && !e.isDead && e != mc.player) {
            entPos = entities.get(e);
            GL11.glEnable((int) 2848);
            GL11.glPushMatrix();
            GL11.glLineWidth((float) 1.3f);
            GlStateManager.color(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 1.0f);
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * event.ticks - mc.getRenderManager().viewerPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * event.ticks - mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * event.ticks - mc.getRenderManager().viewerPosZ;
            float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
            GL11.glRotatef((float) (-xOff), (float) 0.0f, (float) 1.0f, (float) 0.0f);
            GL11.glTranslated((double) 0.0, (double) 0.0, (double) (e.isSneaking() ? -0.235 : 0.0));
            float yOff = e.isSneaking() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double) -0.125, (double) yOff, (double) 0.0);
            if (entPos[3][0] != 0.0f) {
                GL11.glRotatef((float) (entPos[3][0] * 57.295776f), (float) 1.0f, (float) 0.0f, (float) 0.0f);
            }
            if (entPos[3][1] != 0.0f) {
                GL11.glRotatef((float) (entPos[3][1] * 57.295776f), (float) 0.0f, (float) 1.0f, (float) 0.0f);
            }
            if (entPos[3][2] != 0.0f) {
                GL11.glRotatef((float) (entPos[3][2] * 57.295776f), (float) 0.0f, (float) 0.0f, (float) 1.0f);
            }
            GL11.glBegin((int) 3);
            GL11.glVertex3d((double) 0.0, (double) 0.0, (double) 0.0);
            GL11.glVertex3d((double) 0.0, (double) (-yOff), (double) 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double) 0.125, (double) yOff, (double) 0.0);
            if (entPos[4][0] != 0.0f) {
                GL11.glRotatef((float) (entPos[4][0] * 57.295776f), (float) 1.0f, (float) 0.0f, (float) 0.0f);
            }
            if (entPos[4][1] != 0.0f) {
                GL11.glRotatef((float) (entPos[4][1] * 57.295776f), (float) 0.0f, (float) 1.0f, (float) 0.0f);
            }
            if (entPos[4][2] != 0.0f) {
                GL11.glRotatef((float) (entPos[4][2] * 57.295776f), (float) 0.0f, (float) 0.0f, (float) 1.0f);
            }
            GL11.glBegin((int) 3);
            GL11.glVertex3d((double) 0.0, (double) 0.0, (double) 0.0);
            GL11.glVertex3d((double) 0.0, (double) (-yOff), (double) 0.0);
            if (entPos[2][2] != 0.0f) {
                GL11.glRotatef((float) ((-entPos[2][2]) * 57.295776f), (float) 0.0f, (float) 0.0f, (float) 1.0f);
            }
            GL11.glBegin((int) 3);
            GL11.glVertex3d((double) 0.0, (double) 0.0, (double) 0.0);
            GL11.glVertex3d((double) 0.0, (double) -0.5, (double) 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef((float) (xOff - e.rotationYawHead), (float) 0.0f, (float) 1.0f, (float) 0.0f);
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double) 0.0, (double) ((double) yOff + 0.55), (double) 0.0);
            if (entPos[0][0] != 0.0f) {
                GL11.glRotatef((float) (entPos[0][0] * 57.295776f), (float) 1.0f, (float) 0.0f, (float) 0.0f);
            }
            GL11.glBegin((int) 3);
            GL11.glVertex3d((double) 0.0, (double) 0.0, (double) 0.0);
            GL11.glVertex3d((double) 0.0, (double) 0.3, (double) 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef((float) (e.isSneaking() ? 25.0f : 0.0f), (float) 1.0f, (float) 0.0f, (float) 0.0f);
            GL11.glTranslated((double) 0.0, (double) (e.isSneaking() ? -0.16175 : 0.0),
                    (double) (e.isSneaking() ? -0.48025 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double) 0.0, (double) yOff, (double) 0.0);
            GL11.glBegin((int) 3);
            GL11.glVertex3d((double) -0.125, (double) 0.0, (double) 0.0);
            GL11.glVertex3d((double) 0.125, (double) 0.0, (double) 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslated((double) 0.0, (double) yOff, (double) 0.0);
            GL11.glBegin((int) 3);
            GL11.glVertex3d((double) 0.0, (double) 0.0, (double) 0.0);
            GL11.glVertex3d((double) 0.0, (double) 0.55, (double) 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double) 0.0, (double) ((double) yOff + 0.55), (double) 0.0);
            GL11.glBegin((int) 3);
            GL11.glVertex3d((double) -0.375, (double) 0.0, (double) 0.0);
            GL11.glVertex3d((double) 0.375, (double) 0.0, (double) 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }


    private void doCornerESP(EventRender3D e) {
        Iterator var2 = mc.world.playerEntities.iterator();
        while (var2.hasNext()) {
            EntityPlayer entity = (EntityPlayer) var2.next();
            if (entity != mc.player) {
                if (!isValid(entity) && !entity.isInvisible()) {
                    return;
                }
                if (!(!((Entity) entity).isInvisible() || entity == mc.player)) {
                    return;
                }
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                double renderPosX = mc.getRenderManager().viewerPosX;
                double renderPosY = mc.getRenderManager().viewerPosY;
                double renderPosZ = mc.getRenderManager().viewerPosZ;
                float partialTicks = e.getPartialTicks();
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks
                        - renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks
                        - renderPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks
                        - renderPosZ;
                float DISTANCE = mc.player.getDistanceToEntity(entity);
                float SCALE = 0.035F;
                SCALE /= 2.0F;
                GlStateManager.translate((float) x,
                        (float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-SCALE, -SCALE, -SCALE);
                Tessellator tesselator = Tessellator.getInstance();
                tesselator.getWorldRenderer();
                Color color;

                if (entity.hurtTime > 0) {
                    color = new Color(Color.RED.getRGB());
                } else {
                    color = new Color(Color.WHITE.getRGB());
                }
                int COLOR;
                float HEALTH = entity.getHealth();
                if (HEALTH > 20.0) {
                    COLOR = -65292;
                } else if (HEALTH >= 10.0) {
                    COLOR = -16711936;
                } else if (HEALTH >= 3.0) {
                    COLOR = -23296;
                } else {
                    COLOR = -65536;
                }
                Color gray = new Color(0, 0, 0);
                double thickness = (double) (2.0F + DISTANCE * 0.08F);
                double xLeft = -30.0D;
                double xRight = 30.0D;
                double yUp = 20.0D;
                double yDown = 130.0D;
                double size = 10.0D;
                // 闁诲骸缍婂鑽ょ磽濮樿京绠旈柛灞剧矋閸犲棝鏌熼悜姗嗘畷闂夊姊虹捄銊ユ珢闁瑰嚖鎷�?
                //drawVerticalLine(xLeft + size / 2.0D - 1, yUp + 1.0D, size / 2.0D, thickness, gray);
                //drawHorizontalLine(xLeft + 1.0D, yUp + size, size, thickness, gray);
                // 闁诲骸缍婂鑽ょ磽濮樿京绠旈柛宀�鍋為弲鎼佹煥閻曞倹瀚�?
                drawVerticalLine(xLeft + size / 2.0D +1.5, yUp, size / 2.0D, thickness-1.5, color);
                drawHorizontalLine(xLeft+1.5, yUp + size, size, thickness-1.5, color);
                // 闂備礁鎲￠悷銉╁储閺嶎偆绠旈柛灞剧矋閸犲棝鏌熼悜姗嗘畷闂夊姊虹捄銊ユ珢闁瑰嚖鎷�?
                //drawVerticalLine(xRight - size / 2.0D + 1, yUp + 1.0D, size / 2.0D, thickness, gray);
                //drawHorizontalLine(xRight - 1, yUp + size, size, thickness, gray);
                // 闂備礁鎲￠悷銉╁储閺嶎偆绠旈柛宀�鍋為弲鎼佹煥閻曞倹瀚�?
                drawVerticalLine(xRight - size / 2.0D -1.5, yUp, size / 2.0D, thickness-1.5, color);
                drawHorizontalLine(xRight-1.5, yUp + size, size, thickness-1.5, color);
                // 闁诲骸缍婂鑽ょ磽濮樿京绠旈柛娑卞灡閸犲棝鏌熼悜姗嗘畷闂夊姊虹捄銊ユ珢闁瑰嚖鎷�?
                //drawVerticalLine(xLeft + size / 2.0D - 1, yDown - 1, size / 2.0D, thickness, gray);
                //drawHorizontalLine(xLeft + 1.0D, yDown - size, size, thickness, gray);
                // 闁诲骸缍婂鑽ょ磽濮樿京绠旈柛娑樼摠閺呮悂鏌ㄩ悤鍌涘?
                drawVerticalLine(xLeft + size / 2.0D +1.5, yDown, size / 2.0D, thickness-1.5, color);
                drawHorizontalLine(xLeft+1.5, yDown - size, size, thickness-1.5, color);
                // 闂備礁鎲￠悷銉╁储閺嶎偆绠旈柛娑卞灡閸犲棝鏌熼悜姗嗘畷闂夊姊虹捄銊ユ珢闁瑰嚖鎷�?
                //drawVerticalLine(xRight - size / 2.0D + 1, yDown - 1.0D, size / 2.0D, thickness, gray);
                //drawHorizontalLine(xRight - 1.0D, yDown - size, size, thickness, gray);
                // 闂備礁鎲￠悷銉╁储閺嶎偆绠旈柛娑樼摠閺呮悂鏌ㄩ悤鍌涘?
                drawVerticalLine(xRight - size / 2.0D -1.5, yDown, size / 2.0D, thickness-1.5, color);
                drawHorizontalLine(xRight-1.5, yDown - size, size, thickness-1.5, color);
                drawBorderedRect((float) xLeft - 3.0f - DISTANCE * 0.2f, (float) yDown - (float) (yDown - yUp), (float) xLeft - 2.0f, (float) yDown, 0.15f, Color.BLACK.getRGB(), new Color(100, 100, 100).getRGB());
                drawBorderedRect((float) xLeft - 3.0f - DISTANCE * 0.2f, (float) yDown - (float) (yDown - yUp) * Math.min(1.0f, entity.getHealth() / 20.0f), (float) xLeft - 2.0f, (float) yDown, 0.15f, Color.BLACK.getRGB(), COLOR);
                drawBorderedRect((float) xLeft +62.0f - DISTANCE * 0.2f, (float) yDown - (float) (yDown - yUp), (float) xLeft + 63.0f, (float) yDown, 0.15f, Color.BLACK.getRGB(), new Color(100, 100, 100).getRGB());
                drawBorderedRect((float) xLeft +62.0f - DISTANCE * 0.2f, (float) yDown - (float) (yDown - yUp) * Math.min(1.0f, entity.getTotalArmorValue() / 20.0f), (float) xLeft +63.0f, (float) yDown, 0.15f, Color.black.getRGB(),Color.blue.getRGB());
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GL11.glDisable(3042);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glNormal3f(1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }

    }

    private void doBoxESP(EventRender3D event) {
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glEnable((int) 3042);
        GL11.glEnable((int) 2848);
        GL11.glLineWidth((float) 2.0f);
        GL11.glDisable((int) 3553);
        GL11.glDisable((int) 2929);
        GL11.glDepthMask((boolean) false);
        for (Object o : mc.world.playerEntities) {
            if (o instanceof EntityPlayer && o != mc.player) {
                EntityPlayer ent = (EntityPlayer) o;
                if (Teams.isOnSameTeam(ent)) {
                    RenderUtil.entityESPBox(ent, new Color(0, 255, 0), event);
                } else if (ent.hurtTime > 0) {
                    RenderUtil.entityESPBox(ent, new Color(255, 0, 0), event);
                } else if (ent.isInvisible()) {

                } else if(FriendManager.isFriend(ent.getName())){
                    RenderUtil.entityESPBox(ent, new Color(20 / 255.0f,
                            50 / 255.0f, 255 / 255.0f), event);
                }else{
                    RenderUtil.entityESPBox(ent, new Color(255 / 255.0f,
                            255/ 255.0f, 255 / 255.0f), event);
                }
            }
        }
        GL11.glDisable((int) 2848);
        GL11.glEnable((int) 3553);
        GL11.glEnable((int) 2929);
        GL11.glDepthMask((boolean) true);
        GL11.glDisable((int) 3042);
    }

    public static boolean isValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && entity.getHealth() >= 0.0f) {
            if (entity == mc.player && !self.getValue()) {
                return false;
            }
            return true;
        }
        return false;
    }

    private void doOther2DESP(EventRender3D e) {
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (isValid(entity)) {
                if (!entity.isEntityAlive()) continue;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                double renderPosX = mc.getRenderManager().viewerPosX;
                double renderPosY = mc.getRenderManager().viewerPosY;
                double renderPosZ = mc.getRenderManager().viewerPosZ;
                float partialTicks = e.getPartialTicks();
                mc.getRenderManager();
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - renderPosX;
                mc.getRenderManager();
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - renderPosY;
                mc.getRenderManager();
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - renderPosZ;
                float DISTANCE = mc.player.getDistanceToEntity(entity);
                Math.min(DISTANCE * 0.15f, 0.15f);
                float SCALE = 0.035f;
                SCALE /= 2.0f;
                entity.isChild();
                GlStateManager.translate((float) x,
                        (float) y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f),
                        (float) z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-SCALE, -SCALE, -SCALE);
                Tessellator tesselator = Tessellator.getInstance();
                tesselator.getWorldRenderer();
                float HEALTH = entity.getHealth();
                int COLOR;
                if (HEALTH > 20.0) {
                    COLOR = -65292;
                } else if (HEALTH >= 10.0) {
                    COLOR = -16711936;
                } else if (HEALTH >= 3.0) {
                    COLOR = -23296;
                } else {
                    COLOR = -65536;
                }
                new Color(0, 0, 0);
                double thickness = 1.5f + DISTANCE * 0.01f;
                double xLeft = -20.0;
                double xRight = 20.0;
                double yUp = 27.0;
                double yDown = 130.0;
                Color color = new Color(255, 255, 255);
                if (entity.hurtTime > 0) {
                    color = new Color(255, 0, 0);
                } else if (Teams.isOnSameTeam(entity)) {
                    color = new Color(0, 255, 0);
                } else if (entity.isInvisible()) {
                }

                drawBorderedRect((float) xLeft, (float) yUp, (float) xRight, (float) yDown, (float) thickness + 0.5f, Color.BLACK.getRGB(), 0);
                drawBorderedRect((float) xLeft, (float) yUp, (float) xRight, (float) yDown, (float) thickness, color.getRGB(), 0);

                drawBorderedRect((float) xLeft - 3.0f - DISTANCE * 0.2f, (float) yDown - (float) (yDown - yUp), (float) xLeft - 2.0f, (float) yDown, 0.15f, Color.BLACK.getRGB(), new Color(100, 100, 100).getRGB());
                drawBorderedRect((float) xLeft - 3.0f - DISTANCE * 0.2f, (float) yDown - (float) (yDown - yUp) * Math.min(1.0f, entity.getHealth() / 20.0f), (float) xLeft - 2.0f, (float) yDown, 0.15f, Color.BLACK.getRGB(), COLOR);
                //drawRect((int) ((xLeft / 2) - ((mc.fontRendererObj.getStringWidth((entity.getHeldItem() == null ? "" : entity.getHeldItem().getDisplayName()))) / 2)) - 2, (float) yDown + 8,(int) ((xLeft / 2) + ((mc.fontRendererObj.getStringWidth((entity.getHeldItem() == null ? "" : entity.getHeldItem().getDisplayName()))) / 2) + 2), (float) (yDown + 20),new Color(0,0,0,80).getRGB());
                int c;
                if (entity.getHealth() < 5) {
                    c = new Color(255,20,10).getRGB();
                } else if (entity.getHealth() < 12.5) {
                    c = new Color(0xFFF529).getRGB();
                } else {
                    c = new Color(0,255,0).getRGB();
                }
//                mc.fontRendererObj.drawStringWithShadow("" + (int)entity.getHealth()+"❤",(float) xLeft - mc.fontRendererObj.getStringWidth((int)entity.getHealth() + "") - 12,(float) yDown / 2,new Color(HUD.r.getValue().intValue(), HUD.g.getValue().intValue(), HUD.b.getValue().intValue(),HUD.h.getValue().intValue()).getRGB());
//                mc.fontRendererObj.drawStringWithShadow((entity.getHeldItem() == null ? "" : entity.getHeldItem().getDisplayName()),(int) ((xLeft / 2) - ((mc.fontRendererObj.getStringWidth((entity.getHeldItem() == null ? "" : entity.getHeldItem().getDisplayName()))) / 2)), (int) yDown + 10, new Color(0,255,0).getRGB());
//                String svar = "XYZ: " + (int)entity.posX + " " +  (int)entity.posY + " " +  (int)entity.posZ + " Faraway: " + (int)DISTANCE + "m";
//                mc.fontRendererObj.drawCenteredString(svar,(float) (xRight - mc.fontRendererObj.getStringWidth(svar)) / 100, (int) yDown + (entity.getHeldItem() == null ? 10 : 20), new Color(255,255,255).getRGB());

                int y2 = 0;
//                for (PotionEffect effect : entity.getActivePotionEffects()) {
//                    Potion potion = Potion.potionTypes[effect.getPotionID()];
//                    String PType = I18n.format(potion.getName(), new Object[0]);
//                    switch (effect.getAmplifier()) {
//                        case 1: {//
//                            PType = String.valueOf(String.valueOf(PType)) + " II";
//                            break;
//                        }
//                        case 2: {
//                            PType = String.valueOf(String.valueOf(PType)) + " III";
//                            break;
//                        }
//                        case 3: {
//                            PType = String.valueOf(String.valueOf(PType)) + " IV";
//                        }
//                    }
//                    if (effect.getDuration() < 600 && effect.getDuration() > 300) {
//                        PType = String.valueOf(String.valueOf(PType)) + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
//                    } else if (effect.getDuration() < 300) {
//                        PType = String.valueOf(String.valueOf(PType)) + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
//                    } else if (effect.getDuration() > 600) {
//                        PType = String.valueOf(String.valueOf(PType)) + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
//                    }
//                    mc.fontRendererObj.drawStringWithShadow(PType, (float) xLeft - mc.fontRendererObj.getStringWidth(PType) - 5,(float) yUp - mc.fontRendererObj.FONT_HEIGHT + y2 + 20  , potion.getLiquidColor());
//                    y2 -= 10;
//                }



                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GL11.glDisable(3042);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glNormal3f(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex2d((double) i, (double) h);
        GL11.glVertex2d((double) g, (double) h);
        GL11.glVertex2d((double) g, (double) j);
        GL11.glVertex2d((double) i, (double) j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    private void drawVerticalLine(double xPos, double yPos, double xSize, double thickness, Color color) {
        Tessellator tesselator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tesselator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xPos - xSize, yPos - thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos - xSize, yPos + thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + xSize, yPos + thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + xSize, yPos - thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        tesselator.draw();
    }

    private void drawHorizontalLine(double xPos, double yPos, double ySize, double thickness, Color color) {
        Tessellator tesselator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tesselator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xPos - thickness / 2.0D, yPos - ySize, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos - thickness / 2.0D, yPos + ySize, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + thickness / 2.0D, yPos + ySize, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        worldRenderer.pos(xPos + thickness / 2.0D, yPos - ySize, 0.0D).color((float) color.getRed() / 255.0F,
                (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
                .endVertex();
        tesselator.draw();
    }

    public boolean checkValidity1(EntityLivingBase entity) {
        if (entity == mc.player) {
            return false;
        }
        if (entity.isInvisible()) {
            return true;
        }
        return false;
    }

    public static enum ESPMode {
        otherBox, Other2D, New2D, Outline;
    }

    public static void addEntity(EntityPlayer e, ModelRenderer bipedLeftLeg, ModelRenderer bipedLeftLegwear,
                                 ModelRenderer bipedRightLeg, ModelRenderer bipedLeftArm, ModelRenderer bipedLeftArmwear,
                                 ModelRenderer bipedRightArm, ModelRenderer bipedRightArmwear, ModelRenderer bipedBody,
                                 ModelRenderer bipedBodyWear, ModelRenderer bipedHead) {
        entities.put(e,
                new float[][] { { bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ },
                        { bipedRightArm.rotateAngleX, bipedRightArm.rotateAngleY, bipedRightArm.rotateAngleZ },
                        { bipedLeftArm.rotateAngleX, bipedLeftArm.rotateAngleY, bipedLeftArm.rotateAngleZ },
                        { bipedRightLeg.rotateAngleX, bipedRightLeg.rotateAngleY, bipedRightLeg.rotateAngleZ },
                        { bipedLeftLeg.rotateAngleX, bipedLeftLeg.rotateAngleY, bipedLeftLeg.rotateAngleZ } });
    }

}
