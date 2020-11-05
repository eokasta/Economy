package com.github.eokasta.economy.manager;

import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.dao.AccountDao;
import com.github.eokasta.economy.dao.CacheDao;
import com.github.eokasta.economy.entities.Account;
import com.github.eokasta.economy.storage.StorageManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EconomyManager {

    private static EconomyManager instance;
    public static EconomyManager getInstance() {
        if (instance == null)
            instance = new EconomyManager(JavaPlugin.getPlugin(EconomyPlugin.class));

        return instance;
    }

    @Getter
    private final EconomyPlugin plugin;
    @Getter
    private final StorageManager storageManager;
    @Getter
    private final AccountDao accountDao;
    @Getter
    private final CacheDao cacheDao;

    @SneakyThrows
    public Optional<Account> getAccount(String name) {
        final Optional<Account> optionalAccountCache = cacheDao.get(name);

        return optionalAccountCache.isPresent()
                ? optionalAccountCache
                : CompletableFuture.supplyAsync(() -> accountDao.get(name)).get();
    }

    @SneakyThrows
    public EconomyManager(EconomyPlugin plugin) {
        this.plugin = plugin;

        this.storageManager = new StorageManager(plugin.getSettings().getSQLSettings());
        this.accountDao = new AccountDao(storageManager);
        this.cacheDao = new CacheDao();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAll, 20, 20 * plugin.getSettings().getSaveTaskDelay());
    }

    public void saveAll() {
        plugin.getLogger().info("Saving accounts...");
        final long before = System.currentTimeMillis();

        cacheDao.getAll().forEach(account -> {
            if (!account.isModified())
                return;

            accountDao.save(account);
            account.setModified(false);

            if (Bukkit.getPlayerExact(account.getName()) == null)
                cacheDao.delete(account);
        });

        plugin.getLogger().info("Accounts saved in " + (System.currentTimeMillis() - before) + "ms.");
    }

}
