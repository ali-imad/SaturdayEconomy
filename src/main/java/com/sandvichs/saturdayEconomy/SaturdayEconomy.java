package com.sandvichs.saturdayEconomy;

import com.sandvichs.saturdayEconomy.cmd.ReloadConfig;
import com.sandvichs.saturdayEconomy.cmd.TempFly;
import com.sandvichs.saturdayEconomy.handler.EconomyHandler;
import com.sandvichs.saturdayEconomy.listener.PlayerListener;
import com.sandvichs.saturdayEconomy.listener.ProfessionListener;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaturdayEconomy extends JavaPlugin {
    private FileConfiguration config;
    private SalaryManager salaryManager;
    private PlayerListener playerListener;
    private ProfessionListener professionListener;
    public Permission perms;

    @Override
    public void onEnable() {
        professionListener = new ProfessionListener();
        salaryManager = new SalaryManager();
        playerListener = new PlayerListener(salaryManager);
        perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        reloadConfig();
        // check for paying salaries
        salaryManager.createSalaryTask().runTaskTimer(this, 0, 900); // 1200 ticks in a minute
        getCommand("sereload").setExecutor(new ReloadConfig(this));

        // check for temp flight
        TempFly.setPlugin(this);
        TempFly.perms = perms;

        getCommand("tempfly").setExecutor(new TempFly());


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
        TempFly.setPluginConfig(this.config);
        salaryManager.setConfig(this.config);
        professionListener.setConfig(this.config);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
