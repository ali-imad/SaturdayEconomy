package com.sandvichs.saturdayEconomy.handler;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;

import static org.bukkit.Bukkit.getLogger;

// This class will track cooldowns for the plugins.
// Two types of cooldowns will be tracked, tracked and persistent cooldowns
// Tracked cooldowns are cooldowns that are tracked for a specific player and will be frozen when the player logs out
// Persistent cooldowns are cooldowns that are tracked for a specific player and will persist even when the player logs out
public class CooldownHandler {
    public static final Integer SECONDS_IN_DAY = 86400;
    private static final HashMap<String, HashMap<String, Date>> persistent = new HashMap<>();
    private static final HashMap<String, HashMap<String, Integer>> tracked = new HashMap<>();

    // Checks if a player has a cooldown for a specific command, and if so, returns the seconds left on the cooldown
    // Otherwise returns 0
    // PARAMS: player - the player to check the cooldown for
    //         cmd - the command to check the cooldown for
    public static int check(String player, String cmd) {
        if (persistent.containsKey(player) && persistent.get(player).containsKey(cmd)) {
            int timeLeft = (int) ((persistent.get(player).get(cmd).getTime() - new Date().getTime()) / 1000);
            getLogger().info("Cooldown found for player " + player + " and command " + cmd + " with " + timeLeft + " seconds left");
            return Math.max(0, timeLeft);
        } else if (tracked.containsKey(player) && tracked.get(player).containsKey(cmd)) {
            getLogger().info("Cooldown found for player " + player + " and command " + cmd + " with " + tracked.get(player).get(cmd) + " seconds left");
            return Math.max(0, tracked.get(player).get(cmd));
        }
        getLogger().info("No cooldown found for player " + player + " and command " + cmd);
        return 0;
    }

    public static BukkitRunnable createTickTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                tick();
            }
        };
    }

    // Ticks down all tracked cooldowns by some value of seconds
    private static void tick() {
        for (String player : tracked.keySet()) {
            for (String cmd : tracked.get(player).keySet()) {
                int time = tracked.get(player).get(cmd);
                if (time - 1 <= 0) {
                    tracked.get(player).remove(cmd);
                } else {
                    tracked.get(player).put(cmd, time - 1);
                }
            }
        }
    }

    // Inserts a cooldown for a player
    // PARAMS: player - the name of the player to insert the cooldown for
    //         cmd - the command to insert the cooldown for
    //         seconds - the number of seconds the cooldown will last
    //         isPersistent - whether the cooldown is persistent (true) or tracked (false)
    // RETURNS: void
    public static void insert(String player, String cmd, Integer seconds, boolean isPersistent) {
        if (isPersistent) {
            if (!persistent.containsKey(player)) {
                persistent.put(player, new HashMap<>());
            }
            persistent.get(player).put(cmd, new Date(new Date().getTime() + seconds * 1000));
        } else {
            if (!tracked.containsKey(player)) {
                tracked.put(player, new HashMap<>());
            }
            tracked.get(player).put(cmd, seconds);
        }
    }

    public static void remove(String player, String cmd) {
        if (persistent.containsKey(player)) {
            persistent.get(player).remove(cmd);
        }
        if (tracked.containsKey(player)) {
            tracked.get(player).remove(cmd);
        }
    }

}
