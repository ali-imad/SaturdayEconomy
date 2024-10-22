package com.sandvichs.saturdayEconomy.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadConfig implements CommandExecutor {
    public JavaPlugin plugin;
    public ReloadConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        this.plugin.reloadConfig();
        commandSender.sendMessage("SaturdayEconomy config reloaded!");
        return true;
    }
}
