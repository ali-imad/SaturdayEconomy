package com.sandvichs.saturdayEconomy.listener;

import com.sandvichs.saturdayEconomy.handler.EconomyHandler;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

// Professions are a feature of the game that allow players to earn money by performing certain tasks.
public class ProfessionListener implements Listener {

    private FileConfiguration config;

    // This class will listen for events related to professions.
    public ProfessionListener() {
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    // Returns the money and chance of getting money for a given profession, respectively
    private double[] getMobMoneyMap(String key) {
        ConfigurationSection mobChances = this.config.getConfigurationSection("mob_chances");
        ConfigurationSection mobRewards = this.config.getConfigurationSection("mob_rewards." + key.toLowerCase().replace(" ", "_"));
        return getChanceReward(mobChances, mobRewards);
    }

    private double[] getChanceReward(ConfigurationSection mobChances, ConfigurationSection mobRewards) {
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

    private double[] getBlockMoneyMap(String key) {
        ConfigurationSection blockChances = this.config.getConfigurationSection("block_chances");
        ConfigurationSection blockRewards = this.config.getConfigurationSection("block_rewards." + key.toLowerCase().replace(" ", "_"));
        return getChanceReward(blockChances, blockRewards);
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
            EconomyHandler.payPlayer(killer, moneyMap[0], false);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        String name = e.getBlock().getType().name();

        double[] moneyMap = this.getBlockMoneyMap(name);
        if (Math.random() > moneyMap[1]) {
            return;
        }
        EconomyHandler.payPlayer(player, moneyMap[0], false);
    }
}
