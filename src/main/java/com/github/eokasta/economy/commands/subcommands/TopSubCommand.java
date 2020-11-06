package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.enums.CommandTarget;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.manager.EconomyManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SubCommandInformation(
        name = "top",
        permission = "economy.top",
        target = CommandTarget.PLAYER
)
public class TopSubCommand extends SubCommand {

    private final EconomyManager economyManager;

    public TopSubCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
        setNoPermissionMessage(String.join("\n", economyManager.getPlugin().getSettings().formatOf("no-permission")));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        economyManager.showTop(player);
    }
}
