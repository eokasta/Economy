package com.github.eokasta.economy.commands;

import com.github.eokasta.commandlib.CommandManager;
import com.github.eokasta.commandlib.annotations.CommandInformation;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.Command;
import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.commands.subcommands.SetSubCommand;
import com.github.eokasta.economy.entities.Account;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.utils.Helper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandInformation(name = {"money", "coins"})
public class MoneyCommand extends Command {

    private final EconomyManager economyManager;

    public MoneyCommand(EconomyPlugin plugin) {
        this.economyManager = plugin.getEconomyManager();

        registerSubCommand(new SetSubCommand(plugin));

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
                throw new CommandLibException("&cYou don't have account.");

            final Account account = optionalAccount.get();
            message("&aYou have: &f" + Helper.formatBalance(account.getCoins()) + " &acoins.");
            return;
        }

        final String player = args[0];
        final Optional<Account> optionalAccount = economyManager.getAccount(player);
        if (!optionalAccount.isPresent())
            throw new CommandLibException("&cThis player doesn't have an account.");

        final Account account = optionalAccount.get();
        message("&a" + account.getName() + " have: &f" + Helper.formatBalance(account.getCoins()) + " &acoins.");
    }
}
