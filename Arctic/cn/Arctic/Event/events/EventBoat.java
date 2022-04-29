package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;

public class EventBoat  extends Event {
    public static double motionX,motionY,motionZ;
    public EventBoat(double x, double y, double z) {
        EventBoat.motionX=x;
        EventBoat.motionY=y;
        EventBoat.motionZ=z;
        this.motionX=x;
        this.motionY=y;
        this.motionZ=z;
    }
    public double getX() {
        return motionX;
    }

    public static void setX(double x) {
        EventBoat.motionX = x;
    }

    public double getY() {
        return motionY;
    }

    public void setY(double y) {
        EventBoat.motionY = y;
    }

    public double getZ() {
        return motionZ;
    }

    public static void setZ(double z) {
        EventBoat.motionZ = z;
    }
}
