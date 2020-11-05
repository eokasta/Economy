package com.github.eokasta.economy;

import com.github.eokasta.economy.listeners.PlayerListeners;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.utils.YamlConfig;
import com.github.eokasta.economy.utils.provider.Settings;
import com.github.eokasta.economy.vault.EconomyImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyPlugin extends JavaPlugin {

    @Getter
    private Settings settings;
    @Getter
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        this.settings = new Settings(new YamlConfig("config.yml", this, true));
        this.economyManager = new EconomyManager(this);

        new EconomyImpl(this);
        new PlayerListeners(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        economyManager.saveAll();
        economyManager.getStorageManager().getDataSource().close();
    }
}
