package cn.Arctic.Module.modules.MOVE;

public class Location2D
{
    double x;
    double y;
    
    public Location2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double distance(final Location2D Location) {
        final double a = Math.abs(this.x - Location.getX());
        final double b = Math.abs(this.y - Location.getY());
        final double c = Math.sqrt(a * a + b * b);
        return c;
    }
    
    public String getPointString() {
        return "(" + this.x + " | " + this.y + ")";
    }
}