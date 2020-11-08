package com.github.eokasta.economy.manager;

import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.cache.AccountCache;
import com.github.eokasta.economy.dao.AccountDao;
import com.github.eokasta.economy.models.Account;
import com.github.eokasta.economy.singleton.SingletonMapper;
import com.github.eokasta.economy.singleton.annotation.Singleton;
import com.github.eokasta.economy.storage.StorageManager;
import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.MakeItem;
import com.github.eokasta.economy.utils.Replacer;
import com.github.eokasta.economy.utils.YamlConfig;
import com.github.eokasta.economy.utils.provider.NumberFormatter;
import com.github.eokasta.economy.utils.provider.Settings;
import dev.arantes.inventorymenulib.PaginatedGUIBuilder;
import dev.arantes.inventorymenulib.buttons.ItemButton;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Singleton(id = "economyManager")
public class EconomyManager {

    @Getter
    private final EconomyPlugin plugin;
    private final Settings settings;
    
    @Getter
    private final StorageManager storageManager;
    @Getter
    private final AccountDao accountDao;
    @Getter
    private final AccountCache accountCache;

    @Getter
    private final NumberFormatter numberFormatter;

    @Getter
    private Account[] topAccounts;
    @Getter
    private Account lastTop;

    @Getter
    private BukkitTask saveAllTask;
    @Getter
    private BukkitTask updateTopTask;

    @SneakyThrows
    public EconomyManager(EconomyPlugin plugin) {
        this.plugin = plugin;

        this.settings = SingletonMapper.of(Settings.class, new YamlConfig("config.yml", plugin, true));

        this.storageManager = new StorageManager(settings.getSQLSettings());
        this.accountDao = new AccountDao(storageManager);
        this.accountCache = new AccountCache();

        this.numberFormatter = new NumberFormatter(settings);

        initTasks();
    }

    public void initTasks() {
        if (saveAllTask != null)
            saveAllTask.cancel();

        if (updateTopTask != null)
            updateTopTask.cancel();

        this.saveAllTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAll, 10, 20 * settings.getSaveTaskDelay());
        this.updateTopTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::updateTop, 20, 20 * settings.getUpdateTopTaskDelay());
    }

    @SneakyThrows
    public Optional<Account> getAccount(String name) {
        final Optional<Account> optionalAccountCache = accountCache.get(name);

        return optionalAccountCache.isPresent()
                ? optionalAccountCache
                : CompletableFuture.supplyAsync(() -> accountDao.get(name)).get();
    }

    public void updateTop() {
        this.topAccounts = accountDao.getAllOrder(settings.getTopSettings().getInt("limit", 5));

        final Account topAccount = topAccounts[0];

        if (topAccount != null && (lastTop == null || !topAccount.getName().equals(lastTop.getName()))) {
            settings.replaceOf(
                    "top-money-announcement",
                    new Replacer()
                            .add("%player%", topAccount.getName())
                            .add("%coins%", numberFormatter.format(topAccount.getCoins()))
            ).forEach(Bukkit::broadcastMessage);

            this.lastTop = topAccount;
        }
    }

    public void showTop(Player player) {
        if (topAccounts == null)
            return;

        final ConfigurationSection section = settings.getTopSettings();
        if (section == null)
            return;

        final PaginatedGUIBuilder guiBuilder = new PaginatedGUIBuilder(Helper.format(section.getString("name")),
                String.join("", section.getStringList("shape")));
        guiBuilder.setDefaultAllCancell(true);

        final ArrayList<ItemButton> content = new ArrayList<>();

        int position = 0;
        while (topAccounts[position] != null) {
            final Account account = topAccounts[position++];
            content.add(new ItemButton(buildItem(position, account)));
        }

        guiBuilder.setContent(content);

        guiBuilder.build().show(player);
    }

    private ItemStack buildItem(int position, Account account) {
        final Replacer replacer = new Replacer();
        replacer.add("%position%", position);
        replacer.add("%player%", account.getName());
        replacer.add("%coins%", numberFormatter.format(account.getCoins()));

        final ConfigurationSection itemSettings = settings.getTopSettings().getConfigurationSection("item");

        final MakeItem makeItem = new MakeItem(account.getName());
        makeItem.setName(replacer.replace(itemSettings.getString("name")));
        for (String line : itemSettings.getStringList("lore"))
            makeItem.addLore(replacer.replace(line));

        return makeItem.build();
    }

    public void saveAll() {
        plugin.getLogger().info("Saving accounts...");
        final long before = System.currentTimeMillis();

        accountCache.getValues().forEach(account -> {
            if (!account.isModified())
                return;

            accountDao.save(account);
            account.setModified(false);

            if (Bukkit.getPlayerExact(account.getName()) == null)
                accountCache.remove(account.getName());
        });

        plugin.getLogger().info("Accounts saved in " + (System.currentTimeMillis() - before) + "ms.");
    }

}
