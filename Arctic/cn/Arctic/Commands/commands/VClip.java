/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Commands.commands;

import cn.Arctic.Commands.Command;
import cn.Arctic.Util.Chat.Helper;
import cn.Arctic.Util.Timer.TimerUtil;

public class VClip
extends Command {
    private TimerUtil timer = new TimerUtil();

    public VClip() {
        super("Vc", new String[]{"Vclip", "clip", "verticalclip", "clip"}, "", "Teleport down a specific ammount");
    }

    @Override
    public String execute(String[] args) {

            Helper.sendMessage("> You cannot use vclip on the NEON Server.");
        
        return null;
    }
}

