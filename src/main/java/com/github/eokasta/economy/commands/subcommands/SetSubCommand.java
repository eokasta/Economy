package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.entities.Account;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.Verifications;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@SubCommandInformation(
        name = "set",
        permission = "economy.admin.set"
)
public class SetSubCommand extends SubCommand {

    private final EconomyManager economyManager;

    public SetSubCommand(EconomyPlugin plugin) {
        this.economyManager = plugin.getEconomyManager();
        setUsage("&c/money set <player> <amount>");
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandLibException {
        if (args.length < 2)
            throw new CommandLibException(getUsage());

        final String player = args[0];
        Double amount = Verifications.getDouble(args[1]);
        if (amount == null)
            throw new CommandLibException("&cAmount must be a number.");

        if (amount < 0)
            amount = amount * -1;

        final Optional<Account> optionalAccount = economyManager.getAccount(player);
        if (!optionalAccount.isPresent())
            throw new CommandLibException("&cThis player doesn't have an account.");

        final Account account = optionalAccount.get();
        account.setCoins(amount);

        message("&a" + account.getName() + "'s coins have been set to " + Helper.formatBalance(account.getCoins()) + ".");
    }

}
