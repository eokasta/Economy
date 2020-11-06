package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.enums.CommandTarget;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.manager.EconomyManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SubCommandInformation(
        name = "top",
        permission = "economy.top",
        target = CommandTarget.PLAYER
)
public class TopSubCommand extends SubCommand {

    private final EconomyManager economyManager;

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        economyManager.showTop(player);
    }
}
