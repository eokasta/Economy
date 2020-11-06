package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.utils.provider.Settings;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@SubCommandInformation(
        name = "help",
        permission = "economy.help"
)
public class HelpSubCommand extends SubCommand {

    private final Settings settings;

    public HelpSubCommand(Settings settings) {
        this.settings = settings;

        setNoPermissionMessage(String.join("\n", settings.formatOf("no-permission")));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        settings.formatOf("help-message").forEach(this::message);
    }

}
