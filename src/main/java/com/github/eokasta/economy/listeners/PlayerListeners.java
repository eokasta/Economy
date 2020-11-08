package com.github.eokasta.economy.listeners;

import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.dao.AccountDao;
import com.github.eokasta.economy.cache.AccountCache;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.models.Account;
import com.github.eokasta.economy.singleton.SingletonMapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PlayerListeners implements Listener {

    private final AccountDao accountDao;
    private final AccountCache accountCache;

    public PlayerListeners(EconomyPlugin plugin) {
        final EconomyManager economyManager = SingletonMapper.of(EconomyManager.class);
        this.accountDao = economyManager.getAccountDao();
        this.accountCache = economyManager.getAccountCache();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        CompletableFuture.runAsync(() -> {
            final Optional<Account> optionalAccount = accountDao.get(player.getName());

            if (optionalAccount.isPresent()) {
                final Account account = optionalAccount.get();
                accountCache.put(account.getName(), account);
            }

            else {
                final Account account = Account.builder().name(player.getName()).build();
                accountCache.put(account.getName(), account);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        CompletableFuture.runAsync(() -> accountCache.get(player.getName()).ifPresent(account -> {
            accountDao.save(account);
            accountCache.remove(account.getName());
        }));
    }

}
