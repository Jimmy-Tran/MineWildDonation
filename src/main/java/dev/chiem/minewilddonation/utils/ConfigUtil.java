package dev.chiem.minewilddonation.utils;

/**
 * Copyright (c) 2023, Oretheus
 * All rights reserved.
 * <p>
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private final File configFile;

    public ConfigUtil(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");

        // Load the configuration initially
        reloadConfig();

        // Check if the config file exists, if not, copy the default config from resources
        if (!configFile.exists()) {
            plugin.getLogger().info("Copying default config.yml from resources...");
            plugin.saveResource("config.yml", false);
        }

    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));
    }

    public String getGoalHeaderMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("donation.messages.header"));
    }

    public List<String> getGoalBodyMessages() {
        List<String> messages = config.getStringList("donation.messages.body");
        List<String> formattedMessages = new ArrayList<>();

        for (String message : messages) {
            formattedMessages.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        return formattedMessages;
    }

    public String getGoalFooterMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("donation.messages.footer"));
    }

    public int getDonationGoal() {
        return config.getInt("donation.goal", 300);
    }

    public List<String> getWinnerText() {
        List<String> messages = config.getStringList("donation.winner_text");
        List<String> formattedMessages = new ArrayList<>();

        for (String message : messages) {
            formattedMessages.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        return formattedMessages;
    }

    public List<String> getGoalMessage() {
        List<String> messages = config.getStringList("donation.goal_message");
        List<String> formattedMessages = new ArrayList<>();

        for (String message : messages) {
            formattedMessages.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        return formattedMessages;
    }

    public int getPrizeDelay() {
        return config.getInt("donation.prize_delay");
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}