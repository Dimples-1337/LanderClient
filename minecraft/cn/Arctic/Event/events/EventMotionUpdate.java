package cn.Arctic.Event.events;


import cn.Arctic.Event.Event;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class EventMotionUpdate extends Event {
   private boolean isPre;
   private float yaw;
   private float pitch;
   private double y;
   private boolean onground;
   private boolean alwaysSend;
  public Packet packet;

   public void fire(double y, float yaw, float pitch, boolean ground) {
      this.isPre = true;
      this.yaw = yaw;
      this.pitch = pitch;
      this.y = y;
      this.onground = ground;
      super.fire();
   }
   public Packet<?> getPacket() {
       return this.packet;
   }
   public void fire() {
      this.isPre = false;
      super.fire();
   }

   public boolean isPre() {
      return this.isPre;
   }

   public boolean isPost() {
      return !this.isPre;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public double getY() {
      return this.y;
   }

   public boolean isOnground() {
      return this.onground;
   }

   public boolean shouldAlwaysSend() {
      return this.alwaysSend;
   }

   public EventMotionUpdate setYaw(float yaw) {
      this.yaw = yaw;
      return this;
   }

   public EventMotionUpdate setPitch(float pitch) {
      this.pitch = pitch;
      return this;
   }

   public EventMotionUpdate setY(double y) {
      this.y = y;
      return this;
   }

   public EventMotionUpdate setGround(boolean ground) {
      this.onground = ground;
      return this;
   }

   public EventMotionUpdate setAlwaysSend(boolean alwaysSend) {
      this.alwaysSend = alwaysSend;
      return this;
   }
}
