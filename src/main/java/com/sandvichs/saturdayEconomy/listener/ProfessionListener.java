package com.sandvichs.saturdayEconomy.listener;

import com.sandvichs.saturdayEconomy.handler.EconomyHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

// Professions are a feature of the game that allow players to earn money by performing certain tasks.
public class ProfessionListener implements Listener {

    private final FileConfiguration config;

    // This class will listen for events related to professions.
    public ProfessionListener(FileConfiguration config) {
        this.config = config;
    }

    // Returns the money and chance of getting money for a given profession, respectively
    private double[] getMobMoneyMap(String key) {
        ConfigurationSection mobChances = this.config.getConfigurationSection("mob_chances");
        ConfigurationSection mobRewards = this.config.getConfigurationSection("mob_rewards." + key.toLowerCase().replace(" ", "_"));
        if (mobRewards == null || mobChances == null) {
            return new double[]{0.,0.};
        }
        double amount = mobRewards.getDouble("amount");
        String chanceLevel = mobRewards.getString("rate");
        if (chanceLevel == null) {
            chanceLevel = "occasional";
        }
        double chance = mobChances.getDouble(chanceLevel);
        return new double[]{amount, chance};
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        String name = e.getEntity().getName();

        if (killer != null && e.getEntity() instanceof Monster monster) {
            double[] moneyMap = this.getMobMoneyMap(name);
            if (Math.random() > moneyMap[1]) {
                return;
            }
            EconomyHandler.payPlayer(killer, moneyMap[0], true);
        }
    }
}
