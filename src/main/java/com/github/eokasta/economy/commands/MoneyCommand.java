package com.github.eokasta.economy.commands;

import com.github.eokasta.commandlib.CommandManager;
import com.github.eokasta.commandlib.annotations.CommandInformation;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.Command;
import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.commands.subcommands.*;
import com.github.eokasta.economy.entities.Account;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.Replacer;
import com.github.eokasta.economy.utils.provider.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInformation(
        name = {"money", "coins"},
        permission = "economy.money"
)
public class MoneyCommand extends Command {

    private final EconomyManager economyManager;
    private final Settings settings;

    public MoneyCommand(EconomyPlugin plugin) {
        this.economyManager = plugin.getEconomyManager();
        this.settings = plugin.getSettings();

        setNoPermissionMessage(String.join("\n", settings.formatOf("no-permission")));

        registerSubCommand(new SetSubCommand(plugin));
        registerSubCommand(new GiveSubCommand(plugin));
        registerSubCommand(new RemoveSubCommand(plugin));
        registerSubCommand(new PaySubCommand(plugin));
        registerSubCommand(new TopSubCommand(plugin.getEconomyManager()));
        registerSubCommand(new HelpSubCommand(plugin.getSettings()));

        CommandManager.registerCommand(plugin, this);
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) throws CommandLibException {
        if (!(sender instanceof Player))
            throw new CommandLibException(getNotAConsoleMessage());

        if (args.length == 0) {
            final Player player = getPlayer();
            final Optional<Account> optionalAccount = economyManager.getAccount(player.getName());
            if (!optionalAccount.isPresent())
                throw new CommandLibException(String.join("\n", settings.formatOf("no-account")));

            final Account account = optionalAccount.get();
            message(String.join("\n",
                    settings.replaceOf("your-coins",
                            new Replacer().add("%coins%", Helper.formatBalance(account.getCoins()))))
            );
            return;
        }

        final String player = args[0];
        final Optional<Account> optionalAccount = economyManager.getAccount(player);
        if (!optionalAccount.isPresent())
            throw new CommandLibException(String.join("\n", settings.formatOf("player-no-have-account")));

        final Account account = optionalAccount.get();
        message(String.join("\n", settings.replaceOf("player-coins",
                new Replacer()
                        .add("%player%", account.getName())
                        .add("%coins%", Helper.formatBalance(account.getCoins()))))
        );
    }
}
