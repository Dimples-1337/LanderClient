package cn.Arctic.Module.modules.MOVE;

public class BlickWinkel3D
{
    public double yaw;
    public double pitch;
    
    public BlickWinkel3D(final Location3D start, final Location3D target) {
        double yawX = new Location2D(start.getX(), start.getZ()).distance(new Location2D(target.getX(), target.getZ()));
        double yawY = target.getY() - start.getY();
        final double YawrunterRechnen = new Location2D(0.0, 0.0).distance(new Location2D(yawX, yawY));
        yawX /= YawrunterRechnen;
        yawY /= YawrunterRechnen;
        final double yaw = -Math.toDegrees(Math.asin(yawY));
        double pitchX = target.getX() - start.getX();
        double pitchY = target.getZ() - start.getZ();
        final double PitchrunterRechnen = new Location2D(0.0, 0.0).distance(new Location2D(pitchX, pitchY));
        pitchX /= PitchrunterRechnen;
        pitchY /= PitchrunterRechnen;
        double pitch = Math.toDegrees(Math.asin(pitchY));
        pitch -= 90.0;
        if (start.getX() > target.getX()) {
            pitch *= -1.0;
        }
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public double getPitch() {
        return this.yaw;
    }
    
    public double getYaw() {
        return this.pitch;
    }
}

