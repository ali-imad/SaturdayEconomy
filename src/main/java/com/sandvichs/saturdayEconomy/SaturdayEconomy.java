package com.sandvichs.saturdayEconomy;

import com.sandvichs.saturdayEconomy.handler.EconomyHandler;
import com.sandvichs.saturdayEconomy.listener.PlayerListener;
import com.sandvichs.saturdayEconomy.listener.ProfessionListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaturdayEconomy extends JavaPlugin {
    private FileConfiguration config;

    @Override
    public void onEnable() {
        reloadConfig();
        ProfessionListener professionListener = new ProfessionListener(this.config);
        SalaryManager salaryManager = new SalaryManager(this.config);
        PlayerListener playerListener = new PlayerListener(salaryManager);
        // check for paying salaries every minute
        salaryManager.createSalaryTask().runTaskTimer(this, 0, 900); // 1200 ticks in a minute
        EconomyHandler.initEconomy();

        // register the professions listener
        getServer().getPluginManager().registerEvents(professionListener, this);
        getServer().getPluginManager().registerEvents(playerListener, this);

    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
