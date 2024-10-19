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
            System.out.println("Player is not online or economy is not enabled.");
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
            System.out.println("Player is not online or economy is not enabled.");
        }
    }

    // give daily salary
    public void giveDailySalary(Player player) {
        // get player's balance
        double balance = economy.getBalance(player);
        // get player's salary
        double salary = 350.0;

        // get player's salary message
        String salaryMessage = "You have received your daily salary of " + salary + "λ.";
        // get player's balance message
        String balanceMessage = "Your balance is now " + balance + "λ.";

        // pay player
        payPlayer(player, salary, false);
        // send message to player
        player.sendMessage(salaryMessage);
        player.sendMessage(balanceMessage);

    }
}

