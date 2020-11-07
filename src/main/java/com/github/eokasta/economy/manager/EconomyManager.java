package com.github.eokasta.economy.manager;

import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.dao.AccountDao;
import com.github.eokasta.economy.cache.AccountCache;
import com.github.eokasta.economy.models.Account;
import com.github.eokasta.economy.storage.StorageManager;
import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.MakeItem;
import dev.arantes.inventorymenulib.PaginatedGUIBuilder;
import dev.arantes.inventorymenulib.buttons.ItemButton;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
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
    private final AccountCache accountCache;

    @Getter
    private Account[] topAccounts;

    @SneakyThrows
    public EconomyManager(EconomyPlugin plugin) {
        this.plugin = plugin;

        this.storageManager = new StorageManager(plugin.getSettings().getSQLSettings());
        this.accountDao = new AccountDao(storageManager);
        this.accountCache = new AccountCache();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            saveAll();
            updateTop();
        }, 20, 20 * plugin.getSettings().getSaveTaskDelay());

    }

    @SneakyThrows
    public Optional<Account> getAccount(String name) {
        final Optional<Account> optionalAccountCache = accountCache.get(name);

        return optionalAccountCache.isPresent()
                ? optionalAccountCache
                : CompletableFuture.supplyAsync(() -> accountDao.get(name)).get();
    }

    public void updateTop() {
        this.topAccounts = accountDao.getAllOrder(5);
    }

    public void showTop(Player player) {
        if (topAccounts == null)
            return;

        final PaginatedGUIBuilder guiBuilder = new PaginatedGUIBuilder("TOP - Coins", "xxxxxxxxx" + "xx#####xx");
        guiBuilder.setDefaultAllCancell(true);

        final ArrayList<ItemButton> content = new ArrayList<>();

        int position = 0;
        while (topAccounts[position] != null) {
            final Account account = topAccounts[position++];
            content.add(new ItemButton(new MakeItem(account.getName())
                    .setName("&a#" + (position) + " " + account.getName())
                    .addLoreList("",
                            " &7Posição: &f#" + position,
                            " &7Coins: &f" + Helper.formatBalance(account.getCoins()),
                            "")
                    .build())
            );
        }

        guiBuilder.setContent(content);

        guiBuilder.build().show(player);
    }

    public void saveAll() {
        plugin.getLogger().info("Saving accounts...");
        final long before = System.currentTimeMillis();

        accountCache.getAll().forEach(account -> {
            if (!account.isModified())
                return;

            accountDao.save(account);
            account.setModified(false);

            if (Bukkit.getPlayerExact(account.getName()) == null)
                accountCache.delete(account);
        });

        plugin.getLogger().info("Accounts saved in " + (System.currentTimeMillis() - before) + "ms.");
    }

}
