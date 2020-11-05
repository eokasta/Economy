package com.github.eokasta.economy.listeners;

import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.dao.AccountDao;
import com.github.eokasta.economy.dao.CacheDao;
import com.github.eokasta.economy.entities.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PlayerListeners implements Listener {

    private final AccountDao accountDao;
    private final CacheDao cacheDao;

    public PlayerListeners(EconomyPlugin plugin) {
        this.accountDao = plugin.getEconomyManager().getAccountDao();
        this.cacheDao = plugin.getEconomyManager().getCacheDao();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        CompletableFuture.runAsync(() -> {
            final Optional<Account> account = accountDao.get(player.getName());

            if (account.isPresent())
                cacheDao.save(account.get());

            else {
                final Account account1 = Account.builder().name(player.getName()).build();
                cacheDao.save(account1);
                accountDao.save(account1);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        CompletableFuture.runAsync(() -> cacheDao.get(player.getName()).ifPresent(account -> {
            accountDao.save(account);
            cacheDao.delete(account);
        }));
    }

}
