package com.sandvichs.saturdayEconomy.cmd;

import com.sandvichs.saturdayEconomy.SalaryManager;
import com.sandvichs.saturdayEconomy.handler.CooldownHandler;
import com.sandvichs.saturdayEconomy.handler.EconomyHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Daily implements CommandExecutor, TabCompleter {
    private static SalaryManager sm;

    public static void setConfig(SalaryManager sm) {
        Daily.sm = sm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        int timeLeft = CooldownHandler.check(player.getName(), "daily");
        if (timeLeft == 0) {
            CooldownHandler.insert(player.getName(), "daily", CooldownHandler.SECONDS_IN_DAY,true);
            sm.payDaily(player);
        } else {
            sender.sendMessage("You can only claim your daily reward once a day.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}