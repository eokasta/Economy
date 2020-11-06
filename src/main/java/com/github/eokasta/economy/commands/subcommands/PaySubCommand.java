package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.enums.CommandTarget;
import com.github.eokasta.commandlib.exceptions.CommandLibException;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.EconomyPlugin;
import com.github.eokasta.economy.entities.Account;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.Verifications;
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

    public PaySubCommand(EconomyPlugin plugin) {
        this.economyManager = plugin.getEconomyManager();
        setUsage("&c/money pay <player> <amount>");
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandLibException {
        final Player player = (Player) sender;

        if (args.length < 2)
            throw new CommandLibException(getUsage());

        final String target = args[0];
        if (player.getName().equalsIgnoreCase(target))
            throw new CommandLibException("&cYou cannot give yourself money.");

        Double amount = Verifications.getDouble(args[1]);
        if (amount == null || amount == 0)
            throw new CommandLibException("&cAmount must be a number.");

        if (amount < 0)
            amount = amount * -1;

        final Optional<Account> optionalAccountPlayer = economyManager.getAccount(player.getName());
        if (!optionalAccountPlayer.isPresent())
            throw new CommandLibException("&cYou don't have an account.");

        final Account accountPlayer = optionalAccountPlayer.get();
        if (!accountPlayer.hasCoins(amount))
            throw new CommandLibException("&cYou don't have that coins.");

        final Optional<Account> optionalTargetAccount = economyManager.getAccount(target);
        if (!optionalTargetAccount.isPresent())
            throw new CommandLibException("&cThis player doesn't exist.");

        final Account targetAccount = optionalTargetAccount.get();

        accountPlayer.removeCoins(amount);
        targetAccount.addCoins(amount);

        message("&aYou have sent &f%s&a coins to &f%s&a.", Helper.formatBalance(amount), targetAccount.getName());

        final Player targetPlayer = Bukkit.getPlayerExact(target);
        if (targetPlayer != null)
            targetPlayer.sendMessage(Helper.format("&aYou have received &f%s&a coins from &f%s&a.", Helper.formatBalance(amount), player.getName()));
    }
}
