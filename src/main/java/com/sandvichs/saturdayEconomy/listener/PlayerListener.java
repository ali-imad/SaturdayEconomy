package com.sandvichs.saturdayEconomy.listener;

import com.sandvichs.saturdayEconomy.SalaryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// registers players when they log in
public class PlayerListener implements Listener {
    private final SalaryManager sm;
    public PlayerListener(SalaryManager sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player key = event.getPlayer();
        this.sm.registerPlayer(key);
    }
}
