package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.enums.CommandTarget;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.models.Account;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.Replacer;
import com.github.eokasta.economy.utils.Verifications;
import com.github.eokasta.economy.utils.provider.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@SubCommandInformation(
        name = "pay",
        permission = "economy.pay",
        target = CommandTarget.PLAYER
)
public class PaySubCommand extends SubCommand {

    private final EconomyManager economyManager;
    private final Settings settings;

    public PaySubCommand(EconomyPlugin plugin) {
        this.economyManager = plugin.getEconomyManager();
        this.settings = plugin.getSettings();

        setUsage(String.join("\n", settings.formatOf("pay-subcommand-usage")));
        setNoPermissionMessage(String.join("\n", settings.formatOf("no-permission")));
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandLibException {
        final Player player = (Player) sender;

        if (args.length < 2)
            throw new CommandLibException(getUsage());

        final String target = args[0];
        if (player.getName().equalsIgnoreCase(target))
            throw new CommandLibException(String.join("\n", settings.formatOf("cannot-give-yourself")));

        Double amount = Verifications.getDouble(args[1]);
        if (amount == null || amount == 0)
            throw new CommandLibException(String.join("\n", settings.formatOf("amount-must-be-number")));

        if (amount < 0)
            amount = amount * -1;

        final Optional<Account> optionalAccountPlayer = economyManager.getAccount(player.getName());
        if (!optionalAccountPlayer.isPresent())
            throw new CommandLibException(String.join("\n", settings.formatOf("no-have-account")));

        final Account accountPlayer = optionalAccountPlayer.get();
        if (!accountPlayer.hasCoins(amount))
            throw new CommandLibException(String.join("\n", settings.formatOf("no-have-coins")));

        final Optional<Account> optionalTargetAccount = economyManager.getAccount(target);
        if (!optionalTargetAccount.isPresent())
            throw new CommandLibException(String.join("\n", settings.formatOf("player-no-have-account")));

        final Account targetAccount = optionalTargetAccount.get();

        accountPlayer.removeCoins(amount);
        targetAccount.addCoins(amount);

        message(String.join("\n", settings.replaceOf("pay-player",
                new Replacer()
                        .add("%coins%", economyManager.getNumberFormatter().format(amount))
                        .add("%target%", Helper.format(targetAccount.getName()))))
        );

        final Player targetPlayer = Bukkit.getPlayerExact(target);
        if (targetPlayer != null)
            targetPlayer.sendMessage(String.join("\n", settings.replaceOf("received-coins-player",
                    new Replacer()
                            .add("%coins%", economyManager.getNumberFormatter().format(amount))
                            .add("%player%", player.getName())))
            );
    }
}
