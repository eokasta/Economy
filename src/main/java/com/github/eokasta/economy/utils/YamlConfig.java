package com.github.eokasta.economy.utils;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Getter
public class YamlConfig {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final String filename;
    private FileConfiguration config;
    private File file;

    public YamlConfig(String filename, JavaPlugin plugin) {
        this(filename, plugin, false);
    }

    public YamlConfig(String filename, JavaPlugin plugin, boolean saveDefault) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.filename = filename;

        this.init(saveDefault);
    }

    private void init(boolean saveDefault) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), filename);

        if (saveDefault) {
            logger.info("Trying to save file from resources.");
            plugin.saveResource(filename, false);
        }

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();

                file.createNewFile();
                logger.info(filename + " was created.");
            } catch (IOException e) {
                logger.warning("Couldn't create file " + filename);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        logger.info(filename + " loaded!");
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        logger.info(filename + " was reloaded.");

    }

    public boolean delete() {
        config = null;
        return file.delete();
    }

}
