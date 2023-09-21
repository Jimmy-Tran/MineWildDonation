package dev.chiem.minewilddonation.utils;

/**
 * Copyright (c) 2023, Oretheus
 * All rights reserved.
 * <p>
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */


import com.google.gson.Gson;
import dev.chiem.minewilddonation.MineWildDonation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DonationProgress {
    private final File dataFile;
    private double currentAmount;
    private double totalDonations;
    private ConfigUtil config;
    private MineWildDonation plugin;

    public DonationProgress(File dataFolder, ConfigUtil config, MineWildDonation plugin) {
        this.dataFile = new File(dataFolder, "donation_progress.json");
        this.config = config;
        this.plugin = plugin;
        loadProgress();
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void resetProgressBar() {
        currentAmount = 0;
        saveProgress();
    }

    public int getProgressPercentage() {
        saveProgress();
        double goal = config.getDonationGoal();
        return (int) ((currentAmount * 100.0) / goal);
    }

    public void increaseDonation(double amount) {
        double roundedAmount = Math.round(amount * 10.0) / 10.0;
        currentAmount += amount;
        totalDonations += amount;
        saveProgress();

        double goal = config.getDonationGoal();
        if (currentAmount >= goal) {
            currentAmount = currentAmount - goal;
            goalReachedCommands();
            saveProgress();
        }
    }

    private void loadProgress() {
        if (dataFile.exists()) {
            try (FileReader reader = new FileReader(dataFile)) {
                DonationProgressData data = new Gson().fromJson(reader, DonationProgressData.class);
                if (data != null) {
                    currentAmount = data.currentAmount;
                    totalDonations = data.totalDonations;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProgress() {
        DonationProgressData data = new DonationProgressData((int) currentAmount, (int) totalDonations);
        try (FileWriter writer = new FileWriter(dataFile)) {
            new Gson().toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goalReachedCommands() {
        List<String> rewards = config.getConfig().getStringList("donation.goal_rewards");

        int scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (String reward : rewards) {
                    if (reward.contains("{player}")) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            String playerName = player.getName();

                            // Replace {player} placeholder with the actual player name
                            reward = reward.replace("{player}", playerName);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
                        }
                    } else {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
                    }
                }
                List<String> goalMessage = config.getGoalMessage();
                for (String message : goalMessage) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
//                    message.replace("{player}", player.getName());
                        player.sendMessage(message);
                    }

                }
            }
        }, 40);
    }

    private static class DonationProgressData {
        private final int currentAmount;
        private final int totalDonations;

        DonationProgressData(int currentAmount, int totalDonations) {
            this.currentAmount = currentAmount;
            this.totalDonations = totalDonations;
        }
    }
}