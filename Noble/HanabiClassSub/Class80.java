package HanabiClassSub;

import java.util.Random;
import javax.vecmath.Vector3d;

public class Class80 {
   private boolean aac;
   private float smooth;
   private Random random;


   public Class80(boolean var1, float var2) {
      this.aac = var1;
      this.smooth = var2;
      this.random = new Random();
   }

   public Class209 calculateAngle(Vector3d var1, Vector3d var2) {
      Class209 var3 = new Class209();
      var1.x += (double)(this.aac ? this.randomFloat(-0.75F, 0.75F) : 0.0F) - var2.x;
      var1.y += (double)(this.aac ? this.randomFloat(-0.25F, 0.5F) : 0.0F) - var2.y;
      var1.z += (double)(this.aac ? this.randomFloat(-0.75F, 0.75F) : 0.0F) - var2.z;
      double var4 = Math.hypot(var1.x, var1.z);
      var3.setYaw((float)(Math.atan2(var1.z, var1.x) * 57.29577951308232D) - 90.0F);
      var3.setPitch(-((float)(Math.atan2(var1.y, var4) * 57.29577951308232D)));
      return var3.constrantAngle();
   }

   public Class209 smoothAngle(Class209 var1, Class209 var2) {
      Class209 var3 = (new Class209(var2.getYaw() - var1.getYaw(), var2.getPitch() - var1.getPitch())).constrantAngle();
      var3.setYaw(var2.getYaw() - var3.getYaw() / 100.0F * this.smooth);
      var3.setPitch(var2.getPitch() - var3.getPitch() / 100.0F * this.smooth);
      return var3.constrantAngle();
   }

   public float randomFloat(float var1, float var2) {
      return var1 + this.random.nextFloat() * (var2 - var1);
   }
}
