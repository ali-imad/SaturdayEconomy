package com.sandvichs.saturdayEconomy.handler;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getLogger;

public class EconomyHandler {

    private static Economy economy;

    public EconomyHandler() {
    }

    public static void initEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp != null) {
            economy = rsp.getProvider();
            getLogger().info("Economy enabled.");
        } else {
            getLogger().warning("Economy not enabled.");
        }
    }

    public static boolean hasEconomy() {
        return economy != null;
    }

    // pay a player
    public static void payPlayer(Player player, double amount, boolean isVerbose) {
        // check if player is online
        if (hasEconomy() && player.isOnline()) {
            // deposit money into player's account using Vault
            if (amount > 0) {
                economy.depositPlayer(player, amount);
            }

            // send message to player
            if (isVerbose) {
                // round amount to two decimal places
                amount = (double) Math.round(amount * 100) / 100;
                player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "+" + "λ" + amount);
            }
        } else {
            // send message to console
            getLogger().info("Player is not online or economy is not enabled.");
        }
    }

    public static void withdrawPlayer(Player player, double amount, boolean isVerbose) {
        // check if player is online
        if (hasEconomy() && player.isOnline()) {
            // deposit money into player's account using Vault
            if (amount > 0) {
                economy.withdrawPlayer(player, amount);
            }

            // send message to player
            if (isVerbose) {
                player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "-" + "λ" + amount);
            }
        } else {
            // send message to console
            getLogger().info("Player is not online or economy is not enabled.");
        }
    }

    // get player's balance
    public static double getBalance(Player player) {
        // check if player is online
        if (hasEconomy() && player.isOnline()) {
            // get player's balance
            return economy.getBalance(player);
        } else {
            // send message to console
            getLogger().info("Player is not online or economy is not enabled.");
            return 0;
        }
    }
}

