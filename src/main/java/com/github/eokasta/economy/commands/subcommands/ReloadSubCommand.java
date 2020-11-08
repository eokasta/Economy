package com.github.eokasta.economy.commands.subcommands;

import com.github.eokasta.commandlib.annotations.SubCommandInformation;
import com.github.eokasta.commandlib.providers.SubCommand;
import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.singleton.SingletonMapper;
import com.github.eokasta.economy.utils.provider.Settings;
import org.bukkit.command.CommandSender;

@SubCommandInformation(
        name = "reload",
        permission = "economy.admin.reload"
)
public class ReloadSubCommand extends SubCommand {

    private final EconomyManager economyManager = SingletonMapper.of(EconomyManager.class);
    private final Settings settings = SingletonMapper.of(Settings.class);

    public ReloadSubCommand() {
        setNoPermissionMessage(String.join("\n", settings.formatOf("no-permission")));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        settings.getFile().reload();
        settings.formatOf("reloaded-messages").forEach(this::message);
        economyManager.initTasks();
    }
}
