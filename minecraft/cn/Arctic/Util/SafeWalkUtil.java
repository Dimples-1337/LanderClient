package cn.Arctic.Util;

public class SafeWalkUtil
{
    public static boolean flag;
    
    static {
        SafeWalkUtil.flag = false;
    }
    
    public static void setSafe(final boolean isSafe) {
        SafeWalkUtil.flag = isSafe;
    }

	public void setCancelled(boolean onGround) {
		// TODO �Զ����ɵķ������
		
	}
}
