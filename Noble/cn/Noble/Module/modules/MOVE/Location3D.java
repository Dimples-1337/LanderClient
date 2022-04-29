package cn.Noble.Module.modules.MOVE;

public class Location3D
{
    double x;
    double y;
    double z;
    
    public Location3D(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void addX(final double x) {
        this.x += x;
    }
    
    public void addY(final double y) {
        this.y += y;
    }
    
    public void addZ(final double z) {
        this.z += z;
    }
    
    public void add(final double x, final double y, final double z) {
        this.y += y;
        this.x += x;
        this.z += z;
    }
    
    public double distance(final Location3D Location) {
        final double a = Math.abs(Location.getY() - this.y);
        final double ba = Math.abs(Location.getZ() - this.z);
        final double bb = Math.abs(Location.getX() - this.x);
        final double b = Math.sqrt(ba * ba + bb * bb);
        final double c = Math.sqrt(a * a + b * b);
        return c;
    }
    
    public String toPointString() {
        return "(" + this.x + " | " + this.y + " | " + this.z + ")";
    }
}