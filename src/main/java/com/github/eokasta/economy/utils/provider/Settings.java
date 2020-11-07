package com.github.eokasta.economy.utils.provider;

import com.github.eokasta.economy.utils.Helper;
import com.github.eokasta.economy.utils.Replacer;
import com.github.eokasta.economy.utils.YamlConfig;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Settings {

    private final YamlConfig file;

    public ConfigurationSection getSQLSettings() {
        return file.getConfig().getConfigurationSection("mysql");
    }

    public int getSaveTaskDelay() {
        return file.getConfig().getInt("save-task-delay", 30);
    }

    public int getUpdateTopTaskDelay() {
        return file.getConfig().getInt("update-top-task-delay", 300);
    }

    public ConfigurationSection getTopSettings() {
        return file.getConfig().getConfigurationSection("top-menu");
    }

    public List<String> getNumberFormatChars() {
        return file.getConfig().getStringList("number-format-chars");
    }

    public List<String> messageOf(String path) {
        return file.getConfig().getStringList("messages." + path);
    }

    public List<String> formatOf(String path) {
        return messageOf(path).stream().map(Helper::format).collect(Collectors.toList());
    }

    public List<String> replaceOf(String path, Replacer replacer) {
        return formatOf(path).stream().map(replacer::replace).collect(Collectors.toList());
    }

}
