package com.sandvichs.saturdayEconomy.handler;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;

// This class will track cooldowns for the plugins.
// Two types of cooldowns will be tracked, tracked and persistent cooldowns
// Tracked cooldowns are cooldowns that are tracked for a specific player and will be frozen when the player logs out
// Persistent cooldowns are cooldowns that are tracked for a specific player and will persist even when the player logs out
public class CooldownHandler {
    HashMap<Player, HashMap<String, Date>> persistent;
    HashMap<Player, HashMap<String, Integer>> tracked;

    public CooldownHandler() {
        persistent = new HashMap<>();
        tracked = new HashMap<>();
    }

    // Inserts a cooldown for a player
    // PARAMS: player - the player to insert the cooldown for
    //         cmd - the command to insert the cooldown for
    //         seconds - the number of seconds the cooldown will last
    //         isPersistent - whether the cooldown is persistent (true) or tracked (false)
    // RETURNS: void
    public void insert(Player player, String cmd, Integer seconds, boolean isPersistent) {
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

    public void remove(Player player, String cmd) {
        if (persistent.containsKey(player)) {
            persistent.get(player).remove(cmd);
        }
        if (tracked.containsKey(player)) {
            tracked.get(player).remove(cmd);
        }
    }

}
