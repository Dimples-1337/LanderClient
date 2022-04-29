package cn.Arctic.Commands.commands;

import cn.Arctic.Commands.Command;
import cn.Arctic.Util.Chat.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class Target
extends Command {
    public Target() {
        super("Target", new String[]{"list"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 0) {
            Helper.sendMessage(".target entityName");
        } else {
            String name = args[0];
            if (Minecraft.getMinecraft().world.getPlayerEntityByName(name) != null) {
            Entity vip = Minecraft.getMinecraft().world.getPlayerEntityByName(name);
            //Aura2.targetted = vip;
                Helper.sendMessage("Now targeting on "+args[0]);
        } else {
            //Aura2.targetted = null;
                Helper.sendMessage("No entity with the name " + "\"" + args[0] + "\"" + " currently exists.");
        }

        }
        return null;
    }
}
