package com.joi.xsurvival.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.joi.xsurvival.Main;

public class CommandsManager implements CommandExecutor {

	private List<Commands> cmds;
	private static CommandsManager instance;

	static {
		instance = new CommandsManager();
	}

	public static CommandsManager get() {
		return instance;
	}

	private CommandsManager() {
		cmds = new ArrayList<Commands>();
	}

	public void setup() {
		Main.get().getCommand("xs").setExecutor(this);
		cmds.add(new Join());
		cmds.add(new Create());
		cmds.add(new com.joi.xsurvival.commands.List());
		cmds.add(new Leave());
		cmds.add(new Start());
		cmds.add(new Stop());
		cmds.add(new Test());
		cmds.add(new SetSpawn());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (!cmd.getName().equalsIgnoreCase("xs")) {
			return true;
		}
		if (args.length != 0) {
			Commands c = getCommand(args[0]);
			if (c != null) {
				List<String> a = new ArrayList<String>(Arrays.asList(args));
				a.remove(0);
				args = a.toArray(new String[a.size()]);
				c.commandPreprocess(p, args);
			}
		}
		return true;
	}

	private Commands getCommand(String name) {
		for (Commands c : cmds) {
			if (c.getClass().getSimpleName().trim().equalsIgnoreCase(name.trim())) {
				return c;
			}
			String[] aliases;
			for (int length = (aliases = c.getAliases()).length, i = 0; i < length; ++i) {
				String s = aliases[i];
				if (s.trim().equalsIgnoreCase(name.trim())) {
					return c;
				}
			}
		}
		return null;
	}
}
