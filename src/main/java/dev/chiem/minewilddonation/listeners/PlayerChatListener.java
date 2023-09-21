package dev.chiem.minewilddonation.listeners;

/**
 * Copyright (c) 2023, Oretheus
 * All rights reserved.
 * <p>
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */


import dev.chiem.minewilddonation.MineWildDonation;
import dev.chiem.minewilddonation.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class PlayerChatListener implements Listener {
    ConfigUtil config;
    MineWildDonation plugin;
    private final Set<String> playersWhoSaidGG;
    boolean taskRunning = false;

    public PlayerChatListener(MineWildDonation plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigUtil();
        this.playersWhoSaidGG = new HashSet<>();
    }

    //
    // NEED TO MAKE QUEUE SYSTEM SO THAT TWO DONATIONS
    // PRIZE THINGS DONT HAPPEN AT SAME TIME!!!!
    //

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // Check if the donation boolean is true and the message contains "gg" or "gg!"
        if (plugin.getPlayerDonated() && (event.getMessage().equalsIgnoreCase("gg") || event.getMessage().equalsIgnoreCase("gg!"))) {
            String playerName = event.getPlayer().getName();

            // Add the player to the list if not already added
            if (!playersWhoSaidGG.contains(playerName)) {
                playersWhoSaidGG.add(playerName);
            }

            if (!taskRunning) {
                startRandomSelectionTask();
            }
        }
    }

    public void startRandomSelectionTask() {
        taskRunning = true;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Select a random player from the list
            if (!playersWhoSaidGG.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(playersWhoSaidGG.size());
                String selectedPlayer = new ArrayList<>(playersWhoSaidGG).get(randomIndex);

                // Print the selected player's name
                System.out.println("Selected player: " + selectedPlayer);

                // Execute reward commands and send messages
                List<String> rewards = config.getConfig().getStringList("donation.winner_rewards");
                List<String> rewardMessage = config.getWinnerText();

                for (String reward : rewards) {
                    if (reward.contains("{player}")) {
                        // Replace {player} placeholder with the actual player name
                        reward = reward.replace("{player}", selectedPlayer);
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
                }

                for (String s : rewardMessage) {
                    if (s.contains("{player}")) {
                        s = s.replace("{player}", selectedPlayer);
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(s);
                    }
                }

                // Remove the selected player from the list
                playersWhoSaidGG.remove(selectedPlayer);
                plugin.setPlayerDonated(false);
                taskRunning = false;
            }
        }, config.getPrizeDelay() * 20L); // Convert seconds to ticks
    }
}