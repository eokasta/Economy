package com.github.eokasta.economy;

import com.github.eokasta.commandlib.CommandManager;
import com.github.eokasta.economy.commands.MoneyCommand;
import com.github.eokasta.economy.listeners.PlayerListeners;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.singleton.SingletonMapper;
import com.github.eokasta.economy.utils.YamlConfig;
import com.github.eokasta.economy.utils.provider.Settings;
import com.github.eokasta.economy.vault.EconomyImpl;
import dev.arantes.inventorymenulib.listeners.InventoryListener;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyPlugin extends JavaPlugin {

    @Getter
    private Settings settings;
    @Getter
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        InventoryListener.register(this);

        this.settings = SingletonMapper.of(Settings.class, new YamlConfig("config.yml", this, true));
        this.economyManager = SingletonMapper.of(EconomyManager.class, this);

        Bukkit.getServer().getServicesManager().register(
                Economy.class,
                new EconomyImpl(),
                this,
                ServicePriority.Highest
        );

        new PlayerListeners(this);

        final CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand(new MoneyCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        economyManager.saveAll();
        economyManager.getStorageManager().getDataSource().close();
    }
}
