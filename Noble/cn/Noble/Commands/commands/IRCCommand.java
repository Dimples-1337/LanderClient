/*
 * Copyright HRT Staff
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package cn.Noble.Commands.commands;

import cn.Noble.Client;
import cn.Noble.Commands.Command;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.modules.WORLD.IRC;
import cn.Noble.Util.UserCheck;
import cn.Noble.Util.Chat.Helper;
import net.minecraft.util.EnumChatFormatting;





public class IRCCommand
extends Command {
    public IRCCommand() {
        super("IRC", new String[]{"irc"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if(args.length >= 1) {
            if(ModuleManager.getModuleByName("IRC").isEnabled()) {
            	String text = null;
            	for(int i = 0; i <= args.length - 1; i++) {
            		if(text == null) {
            			text = args[i];
            		}else {
            			text = text + " " + args[i];
            		}
            	}
            	if(text.contains("WATCHDOG REPORT PLAYER ") || text.contains("CRASHER PLAYER ")){
                    Helper.sendMessageWithoutPrefix("\\u00a7b警告!\\u00a77 您不可以输入违禁词");
                    return null;
                }
                if (UserCheck.isDev) {
                    IRC.sendMessage("IRC:" + EnumChatFormatting.RESET + "[" + EnumChatFormatting.YELLOW + Client.name + EnumChatFormatting.RESET + "] §4[Dev] " + Client.username + "@" + EnumChatFormatting.WHITE + "@ " + text);
                } else if (UserCheck.isPlus) {
                    IRC.sendMessage("IRC:" + EnumChatFormatting.RESET + "[" + EnumChatFormatting.YELLOW + Client.name + EnumChatFormatting.RESET + "] §a[VIP] " + Client.username + "@" + EnumChatFormatting.WHITE + "@ " + text);
                } else {
                    IRC.sendMessage("IRC:" + EnumChatFormatting.RESET + "[" + EnumChatFormatting.YELLOW + Client.name + EnumChatFormatting.RESET + "] §f[User] " + Client.username + "@" + EnumChatFormatting.WHITE + "@ " + text);
                }
            }else{
                ModuleManager.getModuleByName("IRC").setEnabled(true);
            }
        }else{
            Helper.sendMessage("\u00a7bCorrect usage:\u00a77 .irc <message>");
        }
        return null;
    }
}


