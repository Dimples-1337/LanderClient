package HanabiClassSub;

public class Class209 {
   private float yaw;
   private float pitch;
  
   public Class209(float var1, float var2) {
      this.yaw = var1;
      this.pitch = var2;
   }

   public Class209() {
      this(0.0F, 0.0F);
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setYaw(float var1) {
      this.yaw = var1;
   }

   public void setPitch(float var1) {
      this.pitch = var1;
   }

   public Class209 constrantAngle() {
      this.setYaw(this.getYaw() % 360.0F);
      this.setPitch(this.getPitch() % 360.0F);

      while(this.getYaw() <= -180.0F) {
         this.setYaw(this.getYaw() + 360.0F);
      }

      while(this.getPitch() <= -180.0F) {
         this.setPitch(this.getPitch() + 360.0F);
      }

      while(this.getYaw() > 180.0F) {
         this.setYaw(this.getYaw() - 360.0F);
      }

      while(this.getPitch() > 180.0F) {
         this.setPitch(this.getPitch() - 360.0F);
      }

      return this;
   }
}
