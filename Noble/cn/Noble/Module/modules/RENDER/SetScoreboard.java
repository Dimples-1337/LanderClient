package cn.Noble.Module.modules.RENDER;

import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
public class SetScoreboard extends Module{

	public static Option<Boolean> hideboard = new Option<Boolean>("hideboard",false);
	public static Option<Boolean> fastbord = new Option<Boolean>("fastbord",false);
	public static Option<Boolean> Norednumber = new Option<Boolean>("Norednumber",false);
	public static Option<Boolean> noServername = new Option<Boolean>("noServername",false);
	public static Option<Boolean> noanyfont = new Option<Boolean>("noanyfont",false);
	public static Numbers<Double> X = new Numbers<Double>("X", 4.5, 0.0, 1000.0, 1.0);
	public static Numbers<Double> Y = new Numbers<Double>("Y", 4.5, -300.0, 300.0, 1.0);
	public SetScoreboard() {
		super("Scoreboard", new String[] {"SetScoreboard"},ModuleType.Render);
		this.addValues(this.X, this .Y,this.hideboard ,this.fastbord,this.Norednumber,this.noServername,this.noanyfont);
	}
	
	

}
