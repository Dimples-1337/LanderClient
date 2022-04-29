package cn.Noble.GUI.clickgui;

public class Box
{
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    
    public Box(final double x, final double y, final double z, final double x1, final double y1, final double z1) {
        super();
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x1;
        this.maxY = y1;
        this.maxZ = z1;
    }
}
