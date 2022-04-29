/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Commands.commands;

import cn.Noble.Commands.Command;
import cn.Noble.Util.Chat.Helper;

public class Help
extends Command {
    public Help() {
        super("Help", new String[]{"list"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 0) {
//            Helper.sendMessage("Teleport: .tp x y z");
            Helper.sendMessage("Bind: .bind mod key");
            Helper.sendMessage("ClientName: .cn");
            Helper.sendMessage("Toggle: .t mod");
//            Helper.sendMessage("Xray: .xray add/remove <BlockID>");
            Helper.sendMessage("Help: .help");
        } else {
            Helper.sendMessage("invalid Syntax. Try .help");
        }
        return null;
    }
}

