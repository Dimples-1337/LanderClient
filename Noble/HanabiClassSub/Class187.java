package HanabiClassSub;

import cn.Noble.Event.events.EventMove;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Util.Timer.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Class187 {
   public boolean shouldslow = false;
   Minecraft mc = Minecraft.getMinecraft();
   boolean collided;
   boolean lessSlow;
   private int stage = 1;
   private int stageOG = 1;
   private double moveSpeed;
   private double lastDist;
   double less;
   double stair;
   private double speed = 0.07999999821186066D;
   private double speedvalue;
   TimerUtil timer = new TimerUtil();
   TimerUtil lastCheck = new TimerUtil();

   

   public void onPullback() {
      this.stage = -4;
   }

   public void onUpdate(EventPreUpdate var1) {
      this.lastDist = Math.sqrt((this.mc.player.posX - this.mc.player.prevPosX) * (this.mc.player.posX - this.mc.player.prevPosX) + (this.mc.player.posZ - this.mc.player.prevPosZ) * (this.mc.player.posZ - this.mc.player.prevPosZ));
   }

   private double getHypixelSpeed(int var1) {
      double var2 = defaultSpeed() + 0.028D * (double)this.getSpeedEffect() + (double)this.getSpeedEffect() / 15.0D;
      double var4 = 0.4145D + (double)this.getSpeedEffect() / 12.5D;
      double var6 = 0.4045D + (double)this.getSpeedEffect() / 12.5D;
      double var8 = (double)var1 / 500.0D * 3.0D;
      if (var1 == 0) {
         if (this.timer.hasReached(300L)) {
            this.timer.reset();
         }

         if (!this.lastCheck.hasReached(500L)) {
            if (!this.shouldslow) {
               this.shouldslow = (boolean)true;
            }
         } else if (this.shouldslow) {
            this.shouldslow = false;
         }

         var2 = 0.64D + ((double)this.getSpeedEffect() + 0.028D * (double)this.getSpeedEffect()) * 0.134D;
      } else if (var1 == 1) {
         var2 = var4;
      } else if (var1 == 2) {
         if (mc.timer.timerSpeed == 1.354F) {
         }

         var2 = var6;
      } else if (var1 >= 3) {
         if (mc.timer.timerSpeed == 1.254F) {
         }

         var2 = var6 - var8;
      }

      if (this.shouldslow || !this.lastCheck.hasReached(500L) || this.collided) {
         var2 = 0.2D;
         if (var1 == 0) {
            var2 = 0.0D;
         }
      }

      return Math.max(var2, this.shouldslow ? var2 : defaultSpeed() + 0.028D * (double)this.getSpeedEffect());
   }

   public void onMove(EventMove var1) {
      if (this.mc.player.isCollidedHorizontally) {
         this.collided = (boolean)true;
      }

      if (this.collided) {
         mc.timer.timerSpeed = 1.0F;
         this.stage = -1;
      }

      if (this.stair > 0.0D) {
         this.stair -= 0.25D;
      }

      this.less -= this.less > 1.0D ? 0.12D : 0.11D;
      if (this.less < 0.0D) {
         this.less = 0.0D;
      }

      if (!this.isInLiquid() && this.isOnGround(0.01D) && this.isMoving2()) {
         this.collided = this.mc.player.isCollidedHorizontally;
         if (this.stage >= 0 || this.collided) {
            this.stage = 0;
            double var2 = 0.4086666D + (double)this.getJumpEffect() * 0.1D;
            if (this.stair == 0.0D) {
               this.mc.player.jump();
               var1.setY(this.mc.player.motionY = var2);
            }

            ++this.less;
            if (this.less > 1.0D && !this.lessSlow) {
               this.lessSlow = (boolean)true;
            } else {
               this.lessSlow = false;
            }

            if (this.less > 1.12D) {
               this.less = 1.12D;
            }
         }
      }

      this.speed = this.getHypixelSpeed(this.stage) + 0.0331D;
      this.speed *= 0.91D;
      if (this.stair > 0.0D) {
         this.speed *= 0.7D - (double)this.getSpeedEffect() * 0.1D;
      }

      if (this.stage < 0) {
         this.speed = defaultSpeed();
      }

      if (this.lessSlow) {
         this.speed *= 0.96D;
      }

      if (this.lessSlow) {
         this.speed *= 0.95D;
      }

      if (this.isInLiquid()) {
         this.speed = 0.12D;
      }

      if (this.mc.player.moveForward != 0.0F || this.mc.player.moveStrafing != 0.0F) {
         
            this.setMotion(var1, this.speed);
         

         ++this.stage;
      }

   }

   private void setMotion(EventMove var1, double var2) {
      double var4 = (double)this.mc.player.movementInput.moveForward;
      double var6 = (double)this.mc.player.movementInput.moveStrafe;
      float var8 = this.mc.player.rotationYaw;
      if (var4 == 0.0D && var6 == 0.0D) {
         var1.setX(0.0D);
         var1.setZ(0.0D);
      } else {
         if (var4 != 0.0D) {
            if (var6 > 0.0D) {
               var8 += (float)(var4 > 0.0D ? -45 : 45);
            } else if (var6 < 0.0D) {
               var8 += (float)(var4 > 0.0D ? 45 : -45);
            }

            var6 = 0.0D;
            if (var4 > 0.0D) {
               var4 = 1.0D;
            } else if (var4 < 0.0D) {
               var4 = -1.0D;
            }
         }

         var1.setX(this.mc.player.motionX = var4 * var2 * Math.cos(Math.toRadians((double)var8 + 88.0D)) + var6 * var2 * Math.sin(Math.toRadians((double)var8 + 87.9000015258789D)));
         var1.setZ(this.mc.player.motionZ = var4 * var2 * Math.sin(Math.toRadians((double)var8 + 88.0D)) - var6 * var2 * Math.cos(Math.toRadians((double)var8 + 87.9000015258789D)));
      }

   }

   public boolean isMoving2() {
      return (boolean)(this.mc.player.moveForward == 0.0F && this.mc.player.moveStrafing == 0.0F ? false : true);
   }

   public boolean isOnGround(double var1) {
      return (boolean)(!this.mc.world.getCollidingBoundingBoxes(this.mc.player, this.mc.player.getEntityBoundingBox().offset(0.0D, -var1, 0.0D)).isEmpty() ? true : false);
   }

   public int getJumpEffect() {
      return this.mc.player.isPotionActive(Potion.jump) ? this.mc.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1 : 0;
   }

   public int getSpeedEffect() {
      return this.mc.player.isPotionActive(Potion.moveSpeed) ? this.mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
   }

   public boolean isInLiquid() {
      if (this.mc.player.isInWater()) {
         return true;
      } else {
         boolean var1 = false;
         int var2 = (int)this.mc.player.getEntityBoundingBox().minY;

         for(int var3 = MathHelper.floor_double(this.mc.player.getEntityBoundingBox().minX); var3 < MathHelper.floor_double(this.mc.player.getEntityBoundingBox().maxX) + 1; ++var3) {
            for(int var4 = MathHelper.floor_double(this.mc.player.getEntityBoundingBox().minZ); var4 < MathHelper.floor_double(this.mc.player.getEntityBoundingBox().maxZ) + 1; ++var4) {
               Block var5 = this.mc.world.getBlockState(new BlockPos(var3, var2, var4)).getBlock();
               if (var5 != null && var5.getMaterial() != Material.air) {
                  if (!(var5 instanceof BlockLiquid)) {
                     return false;
                  }

                  var1 = true;
               }
            }
         }

         return var1;
      }
   }

   public void onEnable() {
      this.lessSlow = false;
      this.moveSpeed = defaultSpeed();
      this.less = 0.0D;
      this.lastDist = 0.0D;
      this.stage = 2;
      this.stage = 1;
      this.stage = 2;
      mc.timer.timerSpeed = 1.0F;
      this.lessSlow = this.mc.player.inventory.inventoryChanged;
   }

   public static double defaultSpeed() {
      double var0 = 0.2873D;
      if (Minecraft.getMinecraft().player.isPotionActive(Potion.moveSpeed)) {
         int var2 = Minecraft.getMinecraft().player.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         var0 *= 1.0D + 0.2D * (double)(var2 + 1);
      }

      return var0;
   }

   public void onDisable() {
      mc.timer.timerSpeed = 1.0F;
   }
}
