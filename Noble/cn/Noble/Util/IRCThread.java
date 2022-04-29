package cn.Noble.Util;

import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.modules.WORLD.IRC;
import cn.Noble.Util.Chat.Helper;

public class IRCThread extends Thread {
//    @Override
//    public void run() {
//        IRC.connect();
//        while(true){
//            if(!ModuleManager.getModuleByClass(IRC.class).isEnabled()){
//                Helper.sendMessage("��IRC�Ͽ�����!");
//                break;
//            }
//            IRC.handleInput();
//        }
//    }
}