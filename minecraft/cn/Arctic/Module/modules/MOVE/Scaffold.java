package cn.Arctic.Module.modules.MOVE;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Event.events.Update.EventPostUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Font.FontLoaders;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.Aura;
import cn.Arctic.Module.modules.GUI.HUD;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.SafeWalkUtil;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.math.RotationUtil;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.Util.rotaions.Rotation;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module
{
	 private Option tower = new Option<Boolean>("Tower",true);
	 private Option moveTower = new Option<Boolean>("MoveTower",true);
	 private Option ab = new Option<Boolean>("Silent",true);
	 private Option sprint = new Option<Boolean>("Sprint",true);
	 private Option swing = new Option<Boolean>("Swing",false);
	 public Option<Boolean> Safewalk = new Option<Boolean>("Safewalk",false);
	    public Numbers<Double> Timer = new Numbers<Double>("Timer",1.0,0.1,2.0,0.1);
    private BlockData blockData;
    private double jumpGround;
    private int lastSlot;
    public static int timer;
    public static List<Block> blacklisted;
    public static List<Block> blacklistedBlocks;
    private static final Rotation rotation;
    public static transient float lastYaw;
    public static transient float lastPitch;
    public static transient float lastRandX;
    public static transient float lastRandY;
    public static transient float lastRandZ;
    public static transient BlockPos lastBlockPos;
    public static transient EnumFacing lastFacing;
    private int width=0;
    private TimerUtil blinktimer = new TimerUtil();
	private final LinkedList<double[]> positions = new LinkedList<double[]>();
    
    static {
        Scaffold.timer = 1;
        blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.torch, Blocks.redstone_torch, Blocks.ladder, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.chest, Blocks.torch, Blocks.web, Blocks.redstone_torch, Blocks.brewing_stand, Blocks.waterlily, Blocks.farmland, Blocks.sand, Blocks.beacon, Blocks.tnt, Blocks.ladder, Blocks.jukebox, Blocks.noteblock, Blocks.furnace, Blocks.crafting_table, Blocks.anvil, Blocks.ender_chest, Blocks.trapped_chest);
         Scaffold.blacklistedBlocks = Arrays.asList(new Block[]{Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.sand, Blocks.tnt, Blocks.ladder, Blocks.jukebox, Blocks.noteblock, Blocks.furnace, Blocks.crafting_table, Blocks.anvil, Blocks.ender_chest, Blocks.trapped_chest});
         rotation = new Rotation(999.0f, 999.0f);
        Scaffold.lastYaw = 0.0f;
        Scaffold.lastPitch = 0.0f;
        Scaffold.lastRandX = 0.0f;
        Scaffold.lastRandY = 0.0f;
        Scaffold.lastRandZ = 0.0f;
        Scaffold.lastBlockPos = null;
        Scaffold.lastFacing = null;
    }
    
    public Scaffold() {
        super("Scaffold", new String[] { "magiccarpet2", "blockplacer2", "airwalk2" }, ModuleType.Movement);
        this.jumpGround = 0.0;
        this.addValues(this.tower, this.moveTower, this.ab, this.Timer, this.swing,sprint);
    }
    
    private void fakeJump() {
        final Minecraft mc = Scaffold.mc;
        Minecraft.player.isAirBorne = true;
        final Minecraft mc2 = Scaffold.mc;
        Minecraft.player.triggerAchievement(StatList.jumpStat);
    }
    
    @Override
    public void onEnable() {
    	this.mc.timer.timerSpeed = Timer.getValue().floatValue();
        super.onEnable();
        this.lastSlot = -1;
    }
    
    @EventHandler
  	public void onRender2D(EventRender2D event) {
  		ScaledResolution res = new ScaledResolution(mc);
  		int color = new Color(0, 125, 255).getRGB();
  		if (getBlockCount() <= 100 && getBlockCount() > 64) {
  			color = new Color(255, 255, 0).getRGB();
  		} else if (getBlockCount() <= 64) {
  			color = new Color(255, 0, 0).getRGB();
  		}
  		RenderUtil.drawRoundedRect(res.getScaledWidth() / 2 - 70, 2, res.getScaledWidth() / 2 + 70, 25,
  				new Color(255, 255, 255, 225).getRGB(), new Color(255, 255, 255, 225).getRGB());
  		RenderUtil.circle(res.getScaledWidth() / 2 - 57, 12, 1.0f, color);
  		FontLoaders.NMSL16.drawString(String.valueOf(getBlockCount()) + "  Blocks left", res.getScaledWidth() / 2 - 30,
  				11, new Color(25, 25, 25).getRGB());
  	}
    @EventHandler
    public void onmove(final EventMove e) {
        if (MoveUtils.isMoving()) {
            Client.getModuleManager();
            if ((boolean)this.sprint.getValue()&&!ModuleManager.getModuleByClass(Speed.class).isEnabled()) {
                MoveUtils.setSpeed(e, 0.27);
            }
            if (!(boolean)this.sprint.getValue()&&!ModuleManager.getModuleByClass(Speed.class).isEnabled()) {
                MoveUtils.setSpeed(e, 0.22);
            }
        }
    }
    
    @EventHandler
    public void onPreMotion(final EventPreUpdate e) {
        final double x = Minecraft.player.posX;
        final double y = Minecraft.player.posY - 1.0;
        final double z = Minecraft.player.posZ;
        final Minecraft mc = Scaffold.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc2 = Scaffold.mc;
        final double y2 = Minecraft.player.posY - 1.0;
        final Minecraft mc3 = Scaffold.mc;
        final BlockPos underPos = new BlockPos(posX, y2, Minecraft.player.posZ);
        final Minecraft mc4 = Scaffold.mc;
        final Block underBlock = Minecraft.world.getBlockState(underPos).getBlock();
        BlockPos blockBelow = new BlockPos(x, y, z);
        final Minecraft mc5 = Scaffold.mc;
        final double posX2 = Minecraft.player.posX;
        final Minecraft mc6 = Scaffold.mc;
        final double y3 = Minecraft.player.posY - 1.0;
        final Minecraft mc7 = Scaffold.mc;
        this.blockData = this.getBlockData(new BlockPos(posX2, y3, Minecraft.player.posZ), Scaffold.blacklistedBlocks);
        if (this.getBlockSlot() == 0) {
            for (int i = 0; i < 36; ++i) {
                final ItemStack block = Minecraft.player.inventory.getStackInSlot(i);
                if (block != null && block.getItem() instanceof ItemBlock) {
                    Minecraft.playerController.windowClick(0, i, 0, 0, Minecraft.player);
                    Minecraft.playerController.windowClick(0, 44, 0, 0, Minecraft.player);
                }
            }
        }
        if ((boolean) this.tower.getValue()) {
        	if(mc.gameSettings.keyBindJump.pressed) {
        		synchronized (positions) {
    				positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY + (mc.player.getEyeHeight()), mc.player.posZ});
    				positions.add(new double[]{mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ});
    			}

    			blinktimer.reset();
    			this.mc.timer.timerSpeed = Timer.getValue().floatValue();
        	}
        	if(!mc.gameSettings.keyBindJump.pressed) {
        		this.mc.timer.timerSpeed = Timer.getValue().floatValue();
        	}
            if ((boolean) this.moveTower.getValue()) {
                if (Scaffold.mc.gameSettings.keyBindJump.pressed) {

                    if (this.isMoving2()) {
                        if (this.isOnGround(0.76) && !this.isOnGround(0.75) && EventMove.y > 0.23 && EventMove.y < 0.25) {

                    	
                    			Minecraft.player.setSpeed(EventMove.x = EventMove.z = Minecraft.player.motionX = Minecraft.player.motionZ = 0);

                    			

                    			if(mc.player.onGround)
                    				Minecraft.player.setPosition(Scaffold.Down(Minecraft.player.posX) + 0.5, Minecraft.player.posY,
                    					Scaffold.Down(Minecraft.player.posZ) + 0.5);

                    		}

                    		if (Scaffold.isOnGround(0.76) && !isOnGround(0.75) && EventMove.y > 0.23
                    				&& EventMove.y < 0.25) {
                    			EventMove.y +=Minecraft.player.motionY= Math.round(Minecraft.player.posY) - Minecraft.player.posY;
                    		}

                    		if (Scaffold.isOnGround(1.0E-4)) {
                    			EventMove.y +=Minecraft.player.motionY= 0.41999998688698;
                    		} else if (Minecraft.player.posY >= Math.round(Minecraft.player.posY) - 1.0E-4
                    				&& Minecraft.player.posY <= Math.round(Minecraft.player.posY) + 1.0E-4
                    				&& !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    			EventMove.y +=Minecraft.player.motionY= 0.0;
                    		
                           }
                        if (this.isOnGround(1.0E-4) && EventMove.y > 0.1 && Minecraft.player.posY >= Math.round(Minecraft.player.posY) - 1.0E-4 && Minecraft.player.posY <= Math.round(Minecraft.player.posY) + 1.0E-4) {

                        	
                			Minecraft.player.setSpeed(EventMove.x = EventMove.z = Minecraft.player.motionX = Minecraft.player.motionZ = 0);

                			

                			if(mc.player.onGround)
                				Minecraft.player.setPosition(Scaffold.Down(Minecraft.player.posX) + 0.5, Minecraft.player.posY,
                					Scaffold.Down(Minecraft.player.posZ) + 0.5);

                		}

                		if (Scaffold.isOnGround(0.76) && !isOnGround(0.75) && EventMove.y > 0.23
                				&& EventMove.y < 0.25) {
                			 EventMove.y +=Minecraft.player.motionY= Math.round(Minecraft.player.posY) - Minecraft.player.posY;
                		}

                		if (Scaffold.isOnGround(1.0E-4)) {
                		 EventMove.y +=Minecraft.player.motionY= 0.41999998688698;
                		} else if (Minecraft.player.posY >= Math.round(Minecraft.player.posY) - 1.0E-4
                				&& Minecraft.player.posY <= Math.round(Minecraft.player.posY) + 1.0E-4
                				&& !mc.gameSettings.keyBindSneak.isKeyDown()) {
                		EventMove.y +=Minecraft.player.motionY= 0.0;
                		
                       }
                    
                    		
                    
                        
                    }
                    else {
                        Minecraft.player.motionX = 0.0;
                        Minecraft.player.motionZ = 0.0;
                        Minecraft.player.jumpMovementFactor = 0.0f;
                        blockBelow = new BlockPos(x, y, z);
                        final Minecraft mc8 = Scaffold.mc;
                        if (Minecraft.world.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {
                        	
                        	EventMove.y +=Minecraft.player.motionY= 0.4196000099182129;
                            final ClientPlayerEntity thePlayer = Minecraft.player;
                            thePlayer.motionX *= 0.5;
                            final ClientPlayerEntity thePlayer2 = Minecraft.player;
                            thePlayer2.motionZ *= 0.5;
                        }
                    }
                }
            }
           if (!this.isMoving2() && Scaffold.mc.gameSettings.keyBindJump.pressed) {
      
                Minecraft.player.motionX = 0.0;
                Minecraft.player.motionZ = 0.0;
                Minecraft.player.jumpMovementFactor = 0.0f;
                blockBelow = new BlockPos(x, y, z);
                final Minecraft mc9 = Scaffold.mc;
                if (Minecraft.world.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {

            		final double n = EventPreUpdate.y % 1.0;
            		final double n2 = (double) Down(EventPreUpdate.y);

            		List<Double> list = Arrays.asList(0.41999998688698D);

            		if (n > 0.419D && n < 0.753D) {
            			EventPreUpdate.y = (n2 + (double) Double.valueOf(list.get(0)));
            		} else if (n > 0.753D) {
            			EventPreUpdate.y = (n2 + (double) Double.valueOf(list.get(1)));
            		} else {
            			EventPreUpdate.y = n2;

            			EventPreUpdate.ground = true;
            		}

            		this.mc.timer.timerSpeed = 0.65f;
            			Minecraft.player.setSpeed(0);
            			e.setPitch(90);
            			mc.player.rotationYawHead =90;
            			e.x=Minecraft.player.posX+= (((Minecraft.player.ticksExisted % 2 == 0)
            					? ThreadLocalRandom.current().nextDouble(0.099D, 0.1999D)
            					: (-ThreadLocalRandom.current().nextDouble(0.099D, 0.1999D))));
                			e.z=Minecraft.player.posZ+= (((Minecraft.player.ticksExisted % 2 != 0)
            					? ThreadLocalRandom.current().nextDouble(0.099D, 0.1999D)
            					: (-ThreadLocalRandom.current().nextDouble(0.099D, 0.1999D))));
                
                        Minecraft.player.posY+= 0.40999998688698;
                        
                    final ClientPlayerEntity thePlayer3 = Minecraft.player;
                 
                    final ClientPlayerEntity thePlayer4 = Minecraft.player;
             
                }
            }
        }
        final float[] rotations = RotationUtil.getRotationBlock(BlockData.position);
        e.setYaw(rotations[0]);
        e.setPitch(rotations[1]);
        final Minecraft mc10 = Scaffold.mc;
        Minecraft.player.rotationYawHead = rotations[0];
        final Minecraft mc11 = Scaffold.mc;
        Minecraft.player.rotationPitchHead = rotations[1];
        RendererLivingEntity.SetPitchY(rotations[1]);
    }
    @EventHandler
    public void onRender(final EventRender2D e) {
        final ItemStack block = Minecraft.player.inventory.getStackInSlot(this.getBlockSlot());
    }
    
    private void doMotionUp() {
        if (this.getBlockSlot() > 0 && (boolean)this.tower.getValue() && !PlayerUtil.isMoving2()) {
            final Minecraft mc = Scaffold.mc;
            final double posX = Minecraft.player.posX;
            final Minecraft mc2 = Scaffold.mc;
            final double y = Minecraft.player.posY - 1.0;
            final Minecraft mc3 = Scaffold.mc;
            final BlockPos blockPos0 = new BlockPos(posX, y, Minecraft.player.posZ);
            final Minecraft mc4 = Scaffold.mc;
            final Block getBlock0 = Minecraft.world.getBlockState(blockPos0).getBlock();
            final BlockData blockData0 = this.getBlockData(blockPos0, Scaffold.blacklistedBlocks);
            if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                if (PlayerUtil.isMoving()) {
                    if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75)) {
                        final Minecraft mc5 = Scaffold.mc;
                        if (EventMove.y > 0.23) {
                            final Minecraft mc6 = Scaffold.mc;
                            if (EventMove.y < 0.25) {
                                final Minecraft mc7 = Scaffold.mc;
                                final ClientPlayerEntity thePlayer = Minecraft.player;
                                final Minecraft mc8 = Scaffold.mc;
                                final double n = Math.round(Minecraft.player.posY);
                                final Minecraft mc9 = Scaffold.mc;
                                thePlayer.motionY = n - Minecraft.player.posY;
                            }
                        }
                    }
                    if (!MoveUtils.isOnGround(1.0E-4)) {
                        final Minecraft mc10 = Scaffold.mc;
                        if (EventMove.y > 0.1) {
                            final Minecraft mc11 = Scaffold.mc;
                            final double posY = Minecraft.player.posY;
                            final Minecraft mc12 = Scaffold.mc;
                            if (posY >= Math.round(Minecraft.player.posY) - 1.0E-4) {
                                final Minecraft mc13 = Scaffold.mc;
                                final double posY2 = Minecraft.player.posY;
                                final Minecraft mc14 = Scaffold.mc;
                                if (posY2 <= Math.round(Minecraft.player.posY) + 1.0E-4) {
                                    final Minecraft mc15 = Scaffold.mc;
                                    EventMove.y +=Minecraft.player.motionY= 0.0;
                                }
                            }
                        }
                    }
                }
                return;
            }
            if (PlayerUtil.isMoving2()) {
                if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75)) {
                    final Minecraft mc16 = Scaffold.mc;
                    if (EventMove.y > 0.23) {
                        final Minecraft mc17 = Scaffold.mc;
                        if (EventMove.y < 0.25) {
                            final Minecraft mc18 = Scaffold.mc;
                            final ClientPlayerEntity thePlayer2 = Minecraft.player;
                            final Minecraft mc19 = Scaffold.mc;
                            final double n2 = Math.round(Minecraft.player.posY);
                            final Minecraft mc20 = Scaffold.mc;
                            thePlayer2.motionY = n2 - Minecraft.player.posY;
                        }
                    }
                }
                if (MoveUtils.isOnGround(1.0E-4)) {
                    final Minecraft mc21 = Scaffold.mc;
                	
                    EventMove.y +=Minecraft.player.motionY= 0.4196000099182129;
                }
                else if (!MoveUtils.isOnGround(1.0E-4)) {
                    final Minecraft mc22 = Scaffold.mc;
                    final double posY3 = Minecraft.player.posY;
                    final Minecraft mc23 = Scaffold.mc;
                    if (posY3 >= Math.round(Minecraft.player.posY) - 1.0E-4) {
                        final Minecraft mc24 = Scaffold.mc;
                        final double posY4 = Minecraft.player.posY;
                        final Minecraft mc25 = Scaffold.mc;
                        if (posY4 <= Math.round(Minecraft.player.posY) + 1.0E-4) {
                            final Minecraft mc26 = Scaffold.mc;
                            EventMove.y +=Minecraft.player.motionY= 0.0;
                        }
                    }
                }
                Minecraft.player.motionX = 0.0;
                Minecraft.player.motionZ = 0.0;
                Minecraft.player.jumpMovementFactor = 0.0f;
                if (this.isAirBlock(getBlock0) && blockData0 != null) {
                	
                    EventMove.y +=Minecraft.player.motionY= 0.41993956416514;
                    final ClientPlayerEntity thePlayer3 = Minecraft.player;
                    thePlayer3.motionX *= 0.75;
                    final ClientPlayerEntity thePlayer4 = Minecraft.player;
                    thePlayer4.motionZ *= 0.75;
                }
            }
        }
    }
    
    public boolean isAirBlock(final Block block) {
        return block.getMaterial().isReplaceable() && (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
    }
    
    private Vec3 grabPosition(final BlockPos position, final EnumFacing facing) {
        final Vec3 offset = new Vec3(facing.getDirectionVec().getX() / 2.0, facing.getDirectionVec().getY() / 2.0, facing.getDirectionVec().getZ() / 2.0);
        final Vec3 point = new Vec3(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5);
        return point.add(offset);
    }
    
    private BlockData grab() {
        final EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
        final BlockPos position = new BlockPos(Minecraft.player.getPositionVector()).offset(EnumFacing.DOWN);
        if (!(Minecraft.world.getBlockState(position).getBlock() instanceof BlockAir)) {
            return null;
        }
        for (final EnumFacing offsets : EnumFacing.values()) {
            final BlockPos offset2 = position.offset(offsets);
            Minecraft.world.getBlockState(offset2);
            if (!(Minecraft.world.getBlockState(offset2).getBlock() instanceof BlockAir)) {
                return new BlockData(offset2, invert[offsets.ordinal()], this.blockData);
            }
        }
        return null;
    }
    
    public static float[] grabBlockRotations(final BlockPos pos) {
        return getVecRotation(Minecraft.player.getPositionVector().addVector(0.0, Minecraft.player.getEyeHeight(), 0.0), new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    
    public static float[] getVecRotation(final Vec3 position) {
        return getVecRotation(Minecraft.player.getPositionVector().addVector(0.0, Minecraft.player.getEyeHeight(), 0.0), position);
    }
    
    public static float[] getVecRotation(final Vec3 origin, final Vec3 position) {
        final Vec3 difference = position.subtract(origin);
        final double distance = difference.flat().lengthVector();
        final float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[] { yaw, pitch };
    }
    
    public static boolean isOnGround(final double height) {
        final Minecraft mc = Scaffold.mc;
        return !Minecraft.world.getCollidingBoundingBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public boolean isMoving2() {
        return Minecraft.player.moveForward != 0.0f || Minecraft.player.moveStrafing != 0.0f;
    }
    
    public float[] getRotationsBlock(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - Minecraft.player.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - Minecraft.player.posZ + face.getFrontOffsetZ() / 2.0;
        final double y = block.getY() + 0.5;
        final double d1 = Minecraft.player.posY + Minecraft.player.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }

    
    @EventHandler
    public void onPost(final EventPacketRecieve e) {
        Client.getModuleManager();
        if (ModuleManager.getModuleByClass(Aura.class).isEnabled() && Aura.curTarget != null) {
            return;
        }
        if (EventPacketRecieve.packet instanceof S2FPacketSetSlot) {
            this.lastSlot = ((S2FPacketSetSlot)EventPacketRecieve.packet).slot;
        }
    }
    
    @EventHandler
    public void onPost(final EventPacketSend e) {
        Client.getModuleManager();
        if (ModuleManager.getModuleByClass(Aura.class).isEnabled() && Aura.curTarget != null) {
            return;
        }
        if (EventPacketSend.packet2 instanceof C09PacketHeldItemChange) {
            this.lastSlot = ((C09PacketHeldItemChange)EventPacketSend.packet2).getSlotId();
        }
    }
    
    @EventHandler
    void onSafeWalk(final SafeWalkUtil event) {
        final Minecraft mc = Scaffold.mc;
        event.setCancelled(Minecraft.player.onGround);
    }
    
    @EventHandler
    public void onPost(final EventPostUpdate e) {
        Label_0188: {
            if (this.blockData != null) {
                this.lastSlot = this.getBlockSlot();
                Label_0082: {
                    if (this.lastSlot != -1) {
                        final Minecraft mc = Scaffold.mc;
                        if (Minecraft.player.getCurrentEquippedItem() != null) {
                            final Minecraft mc2 = Scaffold.mc;
                            if (Minecraft.player.inventory.getCurrentItem().getItem() instanceof ItemBlock) {
                                break Label_0082;
                            }
                        }
                    }
                    if (this.lastSlot == -1 || !(boolean)this.ab.getValue()) {
                        if ((boolean) this.ab.getValue()) {
                            final Minecraft mc3 = Scaffold.mc;
                            final NetHandlerPlayClient sendQueue = Minecraft.player.sendQueue;
                            final Minecraft mc4 = Scaffold.mc;
                            sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.player.inventory.currentItem));
                        }
                        break Label_0188;
                    }
                }
                if ((boolean) this.ab.getValue()) {
                    Scaffold.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(this.lastSlot));
                }
                this.placeBlock(BlockData.position, BlockData.face, this.lastSlot);
            }
        }
        if (this.invCheck()) {
            for (int i = 9; i < 36; ++i) {
                final Item item;
                if (Minecraft.player.inventoryContainer.getSlot(i).getHasStack() && (item = Minecraft.player.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock && !Scaffold.blacklisted.contains(((ItemBlock)item).getBlock()) && !((ItemBlock)item).getBlock().getLocalizedName().toLowerCase().contains("chest")) {
                    this.swap(i, 7);
                    break;
                }
            }
        }
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        Minecraft.playerController.windowClick(Minecraft.player.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.player);
    }
    
    private boolean invCheck() {
        for (int i = 36; i < 45; ++i) {
            final Item item;
            if (Minecraft.player.inventoryContainer.getSlot(i).getHasStack() && (item = Minecraft.player.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock && !Scaffold.blacklisted.contains(((ItemBlock)item).getBlock())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onDisable() {
    	net.minecraft.util.Timer.timerSpeed = 1.0f;
        this.lastSlot = -1;
        final Minecraft mc = Scaffold.mc;
        Minecraft.player.stepHeight = 0.5f;
        final Minecraft mc2 = Scaffold.mc;
        if (Minecraft.player.isSwingInProgress) {
            final Minecraft mc3 = Scaffold.mc;
            Minecraft.player.swingProgress = 0.0f;
            final Minecraft mc4 = Scaffold.mc;
            Minecraft.player.swingProgressInt = 0;
            final Minecraft mc5 = Scaffold.mc;
            Minecraft.player.isSwingInProgress = false;
        }
        if ((boolean) this.ab.getValue()) {
            final Minecraft mc6 = Scaffold.mc;
            final NetHandlerPlayClient sendQueue = Minecraft.player.sendQueue;
            final Minecraft mc7 = Scaffold.mc;
            sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.player.inventory.currentItem));
        }
    }
    
    private boolean placeBlock(final BlockPos pos, final EnumFacing facing, final int slotWithBlockInIt) {
        final Minecraft mc = Scaffold.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc2 = Scaffold.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc3 = Scaffold.mc;
        final double y = posY + Minecraft.player.getEyeHeight();
        final Minecraft mc4 = Scaffold.mc;
        final Vec3 eyesPos = new Vec3(posX, y, Minecraft.player.posZ);
        final Minecraft mc5 = Scaffold.mc;
        final ItemStack itemstack = Minecraft.player.inventory.mainInventory[slotWithBlockInIt];
        final Minecraft mc6 = Scaffold.mc;
        final PlayerControllerMP playerController = Minecraft.playerController;
        final Minecraft mc7 = Scaffold.mc;
        final ClientPlayerEntity thePlayer = Minecraft.player;
        final Minecraft mc8 = Scaffold.mc;
        if (playerController.onPlayerRightClick(thePlayer, Minecraft.world, itemstack, pos, facing, new Vec3(BlockData.position).addVector(0.5, 0.5, 0.5).add(new Vec3(BlockData.face.getDirectionVec()).scale(0.5)))) {
            if ((boolean) this.swing.getValue()) {
                final Minecraft mc9 = Scaffold.mc;
                Minecraft.player.swingItem();
            }
            else {
                final Minecraft mc10 = Scaffold.mc;
                Minecraft.player.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
            return true;
        }
        return false;
    }
    
    private BlockData getBlockData(final BlockPos pos, final List list) {
        final List<Block> blacklistedBlocks = Scaffold.blacklistedBlocks;
        final Minecraft mc = Scaffold.mc;
        if (!blacklistedBlocks.contains(Minecraft.world.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, this.blockData);
        }
        final List<Block> blacklistedBlocks2 = Scaffold.blacklistedBlocks;
        final Minecraft mc2 = Scaffold.mc;
        if (!blacklistedBlocks2.contains(Minecraft.world.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            final BlockPos add5 = pos.add(-1, 0, 0);
            if (Keyboard.isKeyDown(42) && Minecraft.player.onGround && Minecraft.player.fallDistance == 0.0f) {
                final Minecraft mc3 = Scaffold.mc;
                if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 1.0, Minecraft.player.posZ)).getBlock() == Blocks.air) {
                    final EnumFacing enumFacing = EnumFacing.DOWN;
                    return new BlockData(add5, enumFacing, this.blockData);
                }
            }
            final EnumFacing enumFacing = EnumFacing.EAST;
            return new BlockData(add5, enumFacing, this.blockData);
        }
        final List<Block> blacklistedBlocks3 = Scaffold.blacklistedBlocks;
        final Minecraft mc4 = Scaffold.mc;
        if (!blacklistedBlocks3.contains(Minecraft.world.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            final BlockPos add6 = pos.add(1, 0, 0);
            if (Keyboard.isKeyDown(42) && Minecraft.player.onGround && Minecraft.player.fallDistance == 0.0f) {
                final Minecraft mc5 = Scaffold.mc;
                if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 1.0, Minecraft.player.posZ)).getBlock() == Blocks.air) {
                    final EnumFacing enumFacing2 = EnumFacing.DOWN;
                    return new BlockData(add6, enumFacing2, this.blockData);
                }
            }
            final EnumFacing enumFacing2 = EnumFacing.WEST;
            return new BlockData(add6, enumFacing2, this.blockData);
        }
        final List<Block> blacklistedBlocks4 = Scaffold.blacklistedBlocks;
        final Minecraft mc6 = Scaffold.mc;
        if (!blacklistedBlocks4.contains(Minecraft.world.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            final BlockPos add7 = pos.add(0, 0, -1);
            if (Keyboard.isKeyDown(42) && Minecraft.player.onGround && Minecraft.player.fallDistance == 0.0f) {
                final Minecraft mc7 = Scaffold.mc;
                if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 1.0, Minecraft.player.posZ)).getBlock() == Blocks.air) {
                    final EnumFacing enumFacing3 = EnumFacing.DOWN;
                    return new BlockData(add7, enumFacing3, this.blockData);
                }
            }
            final EnumFacing enumFacing3 = EnumFacing.SOUTH;
            return new BlockData(add7, enumFacing3, this.blockData);
        }
        final List<Block> blacklistedBlocks5 = Scaffold.blacklistedBlocks;
        final Minecraft mc8 = Scaffold.mc;
        if (!blacklistedBlocks5.contains(Minecraft.world.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            final BlockPos add8 = pos.add(0, 0, 1);
            if (Keyboard.isKeyDown(42) && Minecraft.player.onGround && Minecraft.player.fallDistance == 0.0f) {
                final Minecraft mc9 = Scaffold.mc;
                if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 1.0, Minecraft.player.posZ)).getBlock() == Blocks.air) {
                    final EnumFacing enumFacing4 = EnumFacing.DOWN;
                    return new BlockData(add8, enumFacing4, this.blockData);
                }
            }
            final EnumFacing enumFacing4 = EnumFacing.NORTH;
            return new BlockData(add8, enumFacing4, this.blockData);
        }
        final BlockPos add = pos.add(-1, 0, 0);
        final List<Block> blacklistedBlocks6 = Scaffold.blacklistedBlocks;
        final Minecraft mc10 = Scaffold.mc;
        if (!blacklistedBlocks6.contains(Minecraft.world.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        final List<Block> blacklistedBlocks7 = Scaffold.blacklistedBlocks;
        final Minecraft mc11 = Scaffold.mc;
        if (!blacklistedBlocks7.contains(Minecraft.world.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        final List<Block> blacklistedBlocks8 = Scaffold.blacklistedBlocks;
        final Minecraft mc12 = Scaffold.mc;
        if (!blacklistedBlocks8.contains(Minecraft.world.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        final List<Block> blacklistedBlocks9 = Scaffold.blacklistedBlocks;
        final Minecraft mc13 = Scaffold.mc;
        if (!blacklistedBlocks9.contains(Minecraft.world.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        final BlockPos add2 = pos.add(1, 0, 0);
        final List<Block> blacklistedBlocks10 = Scaffold.blacklistedBlocks;
        final Minecraft mc14 = Scaffold.mc;
        if (!blacklistedBlocks10.contains(Minecraft.world.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        final List<Block> blacklistedBlocks11 = Scaffold.blacklistedBlocks;
        final Minecraft mc15 = Scaffold.mc;
        if (!blacklistedBlocks11.contains(Minecraft.world.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        final List<Block> blacklistedBlocks12 = Scaffold.blacklistedBlocks;
        final Minecraft mc16 = Scaffold.mc;
        if (!blacklistedBlocks12.contains(Minecraft.world.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        final List<Block> blacklistedBlocks13 = Scaffold.blacklistedBlocks;
        final Minecraft mc17 = Scaffold.mc;
        if (!blacklistedBlocks13.contains(Minecraft.world.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        final BlockPos add3 = pos.add(0, 0, -1);
        final List<Block> blacklistedBlocks14 = Scaffold.blacklistedBlocks;
        final Minecraft mc18 = Scaffold.mc;
        if (!blacklistedBlocks14.contains(Minecraft.world.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        final List<Block> blacklistedBlocks15 = Scaffold.blacklistedBlocks;
        final Minecraft mc19 = Scaffold.mc;
        if (!blacklistedBlocks15.contains(Minecraft.world.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        final List<Block> blacklistedBlocks16 = Scaffold.blacklistedBlocks;
        final Minecraft mc20 = Scaffold.mc;
        if (!blacklistedBlocks16.contains(Minecraft.world.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        final List<Block> blacklistedBlocks17 = Scaffold.blacklistedBlocks;
        final Minecraft mc21 = Scaffold.mc;
        if (!blacklistedBlocks17.contains(Minecraft.world.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        final BlockPos add4 = pos.add(0, 0, 1);
        final List<Block> blacklistedBlocks18 = Scaffold.blacklistedBlocks;
        final Minecraft mc22 = Scaffold.mc;
        if (!blacklistedBlocks18.contains(Minecraft.world.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        final List<Block> blacklistedBlocks19 = Scaffold.blacklistedBlocks;
        final Minecraft mc23 = Scaffold.mc;
        if (!blacklistedBlocks19.contains(Minecraft.world.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        final List<Block> blacklistedBlocks20 = Scaffold.blacklistedBlocks;
        final Minecraft mc24 = Scaffold.mc;
        if (!blacklistedBlocks20.contains(Minecraft.world.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        final List<Block> blacklistedBlocks21 = Scaffold.blacklistedBlocks;
        final Minecraft mc25 = Scaffold.mc;
        if (!blacklistedBlocks21.contains(Minecraft.world.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        return null;
    }
    
    private boolean canPush(final BlockPos pos) {
        final Minecraft mc = Scaffold.mc;
        final Block block = Minecraft.world.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }
    
    private int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0 && !Scaffold.blacklisted.stream().anyMatch(e -> e.equals(((ItemBlock)itemStack.getItem()).getBlock()))) {
                return i - 36;
            }
        }
        return -1;
    }
    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.blacklistedBlocks.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
    
    public static boolean isRotating() {
        return Scaffold.rotation.getYaw() != 999.0f || Scaffold.rotation.getPitch() != 999.0f;
    }
	@EventHandler
	public void onMoveTower() {

		

		if (PlayerUtil.isMoving()) {

			;

			Minecraft.player.setSpeed(EventMove.x = EventMove.z = Minecraft.player.motionX = Minecraft.player.motionZ = 0);

			;

			if(mc.player.onGround)
				Minecraft.player.setPosition(Scaffold.Down(Minecraft.player.posX) + 0.5, Minecraft.player.posY,
					Scaffold.Down(Minecraft.player.posZ) + 0.5);

		}

		if (Scaffold.isOnGround(0.76) && !isOnGround(0.75) && EventMove.y > 0.23
				&& EventMove.y < 0.25) {
			EventMove.y  = Math.round(Minecraft.player.posY) - Minecraft.player.posY;
		}

		if (Scaffold.isOnGround(1.0E-4)) {
			EventMove.y= 0.41999998688698;
		} else if (Minecraft.player.posY >= Math.round(Minecraft.player.posY) - 1.0E-4
				&& Minecraft.player.posY <= Math.round(Minecraft.player.posY) + 1.0E-4
				&& !mc.gameSettings.keyBindSneak.isKeyDown()) {
			EventMove.y +=Minecraft.player.motionY= 0.0;
		}
	}
	public void d() {



		final double n = EventPreUpdate.y % 1.0;
		final double n2 = (double) Down(EventPreUpdate.y);

		List<Double> list = Arrays.asList(0.41999998688698D, 0.7531999805212D);

		if (n > 0.419D && n < 0.753D) {
			EventPreUpdate.y = (n2 + (double) Double.valueOf(list.get(0)));
		} else if (n > 0.753D) {
			EventPreUpdate.y = (n2 + (double) Double.valueOf(list.get(1)));
		} else {
			EventPreUpdate.y = n2;

			EventPreUpdate.ground = (true);
		}

		if (!PlayerUtil.isMoving()) {

			Minecraft.player.setSpeed(0);

			Minecraft.player.motionX = Minecraft.player.motionZ = 0;

			Minecraft.player.motionX= (((Minecraft.player.ticksExisted % 2 == 0)
					? ThreadLocalRandom.current().nextDouble(0.09D, 0.0985D)
					: (-ThreadLocalRandom.current().nextDouble(0.09D, 0.0985D))));
			Minecraft.player.motionZ = (((Minecraft.player.ticksExisted % 2 != 0)
					? ThreadLocalRandom.current().nextDouble(0.09D, 0.0985D)
					: (-ThreadLocalRandom.current().nextDouble(0.09D, 0.0985D))));
		}

	}
	public static int Down(final double n) {
		final int n2 = (int) n;
		try {
			if (n < n2) {
				return n2 - 1;
			}
		} catch (IllegalArgumentException ex) {
		}
		return n2;
	}

    private static class BlockData
    {
        public static BlockPos position;
        public static EnumFacing face;
        
        public BlockData(final BlockPos position, final EnumFacing face, final BlockData blockData) {
            BlockData.position = position;
            BlockData.face = face;
        }
        
        private BlockPos getPosition() {
            return BlockData.position;
        }
        
        private EnumFacing getFacing() {
            return BlockData.face;
        }
        
        static BlockPos access$0(final BlockData var0) {
            return var0.getPosition();
        }
        
        static EnumFacing access$1(final BlockData var0) {
            return var0.getFacing();
        }
        
        static BlockPos access$2(final BlockData var0) {
            return BlockData.position;
        }
        
        static EnumFacing access$3(final BlockData var0) {
            return BlockData.face;
        }
    }
}
