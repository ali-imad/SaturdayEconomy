package com.sandvichs.saturdayEconomy.cmd;

import com.sandvichs.saturdayEconomy.handler.EconomyHandler;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TempFly implements CommandExecutor, TabExecutor {
    public static ConfigurationSection config;
    public static Permission perms;
    private static Plugin plugin;

    public void start(Player player) {
        player.setAllowFlight(true);
        perms.playerAdd(player, "essentials.fly");

        BukkitRunnable cleanUp = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    player.sendMessage("Your temporary fly has expired!");
                    player.setAllowFlight(false);
                    perms.playerRemove(player, "essentials.fly");
                } else {
                    perms.playerRemove(String.valueOf(player.getWorld()), player, "essentials.fly");
                }
            }
        };
        cleanUp.runTaskLater(plugin, config.getInt("player.tempfly_duration") * 20L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        double cost = config.getDouble("player.tempfly_cost");
        int duration = config.getInt("player.tempfly_duration");
        if (cost == 0) {
            sender.sendMessage("Temporary fly is not enabled on this server!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("buy")) {
            if (!(EconomyHandler.getBalance(player) >= cost)) {
                sender.sendMessage("You do not have enough money to buy temporary fly!");
                return true;
            }
            if (player.getAllowFlight()) {
                sender.sendMessage("You already have flight!");
                return true;
            }
            EconomyHandler.withdrawPlayer(player, cost, true);

            start(player);
            sender.sendMessage("You have bought temporary fly and can fly! Use /fly to toggle");
            sender.sendMessage("Your temporary fly will expire in " + duration / 60 + " minutes.");
            return true;
        }
        sender.sendMessage("Usage: /tempfly buy. Duration: " + duration / 60 + " minutes. Cost: λ" + cost);
        return true;
    }

    public static void setPluginConfig(ConfigurationSection config) {
        TempFly.config = config;
    }

    public static void setPlugin(Plugin p) {
        TempFly.plugin = p;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("buy");
        }
        return new ArrayList<>();
    }
}
