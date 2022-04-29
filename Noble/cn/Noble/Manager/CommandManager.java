/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cn.Noble.Commands.Command;
import cn.Noble.Commands.commands.*;
import cn.Noble.Event.EventBus;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventChat;
import cn.Noble.Util.Chat.Helper;

public class CommandManager
implements Manager {
	  private List<Command> commands;

	    @Override
	    public void init() {
	        this.commands = new ArrayList<Command>();
	        this.commands.add(new Command("Melody", new String[]{"test"}, "", "testing"){

	            @Override
	            public String execute(String[] args) {
	                for (Command command : CommandManager.this.commands) {
	                }
	                return null;
	            }
	        });
	        this.commands.add(new ChangeText());
	        this.commands.add(new Help());
	        this.commands.add(new Toggle());
	        this.commands.add(new Bind());
	        this.commands.add(new Xraycmd());
	        this.commands.add(new Enchant());
	        this.commands.add(new Target());
	        this.commands.add(new IRCCommand());
	        EventBus.getInstance().register(this);
	    }

	    public List<Command> getCommands() {
	        return this.commands;
	    }

	    public Optional<Command> getCommandByName(String name) {
	        return this.commands.stream().filter(c2 -> {
	            boolean isAlias = false;
	            String[] arrstring = c2.getAlias();
	            int n = arrstring.length;
	            int n2 = 0;
	            while (n2 < n) {
	                String str = arrstring[n2];
	                if (str.equalsIgnoreCase(name)) {
	                    isAlias = true;
	                    break;
	                }
	                ++n2;
	            }
	            if (!c2.getName().equalsIgnoreCase(name) && !isAlias) {
	                return false;
	            }
	            return true;
	        }).findFirst();
	    }

	    public void add(Command command) {
	        this.commands.add(command);
	    }

	    @EventHandler
	    private void onChat(EventChat e) {
	        if (e.getMessage().length() > 1 && e.getMessage().startsWith(".")) {
	            e.setCancelled(true);
	            String[] args = e.getMessage().trim().substring(1).split(" ");
	            Optional<Command> possibleCmd = this.getCommandByName(args[0]);
	            if (possibleCmd.isPresent()) {
	                String result = possibleCmd.get().execute(Arrays.copyOfRange(args, 1, args.length));
	                if (result != null && !result.isEmpty()) {
	                    Helper.sendMessage(result);
	                }
	            } else {
	                Helper.sendMessage(String.format("Command not found Try '%shelp'", "."));
	            }
	        }
	    }

}

