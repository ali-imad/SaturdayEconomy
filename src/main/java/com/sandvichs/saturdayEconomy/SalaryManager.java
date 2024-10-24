package com.sandvichs.saturdayEconomy;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;

import static com.gmail.nossr50.util.player.UserManager.getPlayer;
import static com.sandvichs.saturdayEconomy.handler.EconomyHandler.payPlayer;

public class SalaryManager {
    private HashMap<Player, Date> playtime = new HashMap<>();  // playtime for players
    private ConfigurationSection rates;

    public SalaryManager() {
    }

    public void setConfig(FileConfiguration config) {
        rates = config.getConfigurationSection("player");
        // refresh playtime tasks
    }

    // Get hourly salary for a player
    private double getHourlySalary(Player key) {
        double hourly = rates.getDouble("hourly");
        ;
        McMMOPlayer player = getPlayer(key);
        if (player == null) {
            return hourly;
        }
        int level = player.getPowerLevel();
        if (level == 0) {
            return hourly;
        }
        // get mcmmo level and scale
        double bonus_scale = rates.getDouble("hourly_bonus");
        // scale and round to two decimal places
        hourly += (double) Math.round((100 * level * bonus_scale)) / 100;

        return hourly;
    }

    private double getDailySalary(Player key) {
        double daily = rates.getDouble("daily");
        McMMOPlayer player = getPlayer(key);
        if (player == null) {
            return daily;
        }
        int level = player.getPowerLevel();
        if (level == 0) {
            return daily;
        }
        // get mcmmo level and scale
        double bonus_scale = rates.getDouble("daily_bonus");
        // scale and round to two decimal places
        daily += (double) Math.round((100 * level * bonus_scale)) / 100;

        return daily;
    }

    public BukkitRunnable createSalaryTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player key : playtime.keySet()) {
                    payHourly(key);
                }
            }
        };
    }

    // Register a player when they join
    public void registerPlayer(Player key) {
        Bukkit.getLogger().info("Player " + key + " has joined. Registering to pay salary.");
        playtime.put(key, new Date());
    }

    // get player session playtime in milliseconds
    private long getPlayerSessionTime(Player key) {
        Date now = new Date();
        Date last = playtime.get(key);
        return now.getTime() - last.getTime();
    }

    // Pay player salary
    public void payHourly(Player player) {
        long salaryFreq = rates.getLong("pay_freq");
        long timeToSalary = 3600000 / salaryFreq; // 1 hour in milliseconds div by salary frequency
        if (player == null || !player.isOnline()) {
            return;
        }

        if (getPlayerSessionTime(player) >= timeToSalary) {
            // round to two decimal places
            double salary = (double) Math.round(getHourlySalary(player) / salaryFreq * 100) / 100;
            Bukkit.getLogger().info("Paying salary of " + salary + " to player " + player);
            payPlayer(player, salary, false);
            playtime.put(player, new Date()); // refresh playtime
        }
    }

    public void payDaily(Player player) {
        double salary = getDailySalary(player);
        Bukkit.getLogger().info("Paying daily salary of " + salary + " to player " + player);
        payPlayer(player, salary, true);
    }

    public void deregisterPlayer(Player player) {
        Bukkit.getLogger().info("Player " + player + " has left. Deregistering from salary.");
        if (playtime.remove(player) == null) {
            Bukkit.getLogger().warning("Player " + player + " not found in playtime map.");
        }
    }
}
