package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.models.Account;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.Replacer;
import com.github.eokasta.economy.utils.Verifications;
import com.github.eokasta.economy.utils.provider.Settings;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@SubCommandInformation(
        name = "give",
        permission = "economy.admin.remove"
)
public class GiveSubCommand extends SubCommand {

    private final EconomyManager economyManager;
    private final Settings settings;

    public GiveSubCommand(EconomyPlugin plugin) {
        this.economyManager = plugin.getEconomyManager();
        this.settings = plugin.getSettings();

        setUsage(String.join("\n", settings.formatOf("give-subcommand-usage")));
        setNoPermissionMessage(String.join("\n", settings.formatOf("no-permission")));
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandLibException {
        if (args.length < 2)
            throw new CommandLibException(getUsage());

        final String player = args[0];
        Double amount = Verifications.getDouble(args[1]);
        if (amount == null || amount == 0)
            throw new CommandLibException(String.join("\n", settings.formatOf("amount-must-be-number")));

        if (amount < 0)
            amount = amount * -1;

        final Optional<Account> optionalAccount = economyManager.getAccount(player);
        if (!optionalAccount.isPresent())
            throw new CommandLibException(String.join("\n", settings.formatOf("player-no-have-account")));

        final Account account = optionalAccount.get();
        account.addCoins(amount);

        message(String.join("\n", settings.replaceOf("give-coins",
                new Replacer()
                        .add("%player%", account.getName())
                        .add("%coins%", Helper.formatBalance(account.getCoins()))
                        .add("%coinsAdded%", Helper.formatBalance(amount))))
        );

    }
}
