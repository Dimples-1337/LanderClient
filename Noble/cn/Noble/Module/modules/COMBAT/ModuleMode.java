package cn.Noble.Module.modules.COMBAT;

import java.util.ArrayList;

import cn.Noble.Module.Module;



public class ModuleMode {

    private Module module;
    private String name;
    private ArrayList<Object> modes;

    public ModuleMode(Module module, String name) {
        this.module = module;
        this.name = name;
    }

    public Module getModule() {
        return module;
    }
    public String getName() {
        return name;
    }
}