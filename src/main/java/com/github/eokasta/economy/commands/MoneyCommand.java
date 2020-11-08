package com.github.eokasta.economy.commands;

import com.github.eokasta.commandlib.annotations.CommandInformation;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.Command;
import com.github.eokasta.economy.commands.subcommands.*;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.models.Account;
import com.github.eokasta.economy.singleton.SingletonMapper;
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

    private final EconomyManager economyManager = SingletonMapper.of(EconomyManager.class);
    private final Settings settings = SingletonMapper.of(Settings.class);

    public MoneyCommand() {
        setNoPermissionMessage(String.join("\n", settings.formatOf("no-permission")));

        registerSubCommand(new SetSubCommand());
        registerSubCommand(new GiveSubCommand());
        registerSubCommand(new RemoveSubCommand());
        registerSubCommand(new PaySubCommand());
        registerSubCommand(new TopSubCommand());
        registerSubCommand(new HelpSubCommand());
        registerSubCommand(new ReloadSubCommand());
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
                            new Replacer().add("%coins%", economyManager.getNumberFormatter().format(account.getCoins()))))
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
                        .add("%coins%", economyManager.getNumberFormatter().format(account.getCoins()))))
        );
    }
}
