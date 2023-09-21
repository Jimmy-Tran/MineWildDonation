package dev.chiem.minewilddonation.utils;

/**
 * Copyright (c) 2023, Oretheus
 * All rights reserved.
 * <p>
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

import jdk.internal.joptsimple.internal.Strings;
import org.bukkit.ChatColor;

/**
 * This class is util for creating progressive bars with strings
 */
public final class ProgressBar {
    private static ConfigUtil config;

    public ProgressBar(ConfigUtil config) {
        ProgressBar.config = config;
    }

    /**
     * Generate a new progress bar.
     * This method use also his partner: {@see ProgressBar#getProgressBar(int, int, char, ChatColor, ChatColor);}.
     * @param totalBars The amount of bars in the string
     * @param completedPercentage The completed percentage of the bars amount
     * @param symbol The single bar symbol
     * @param incompleteColor The incomplete color zone of the bar
     * @param completeColor The complete color zone of the bar
     * @return The bar string
     */
    public static String getProgressBar(int totalBars, short completedPercentage, char symbol, ChatColor incompleteColor, ChatColor completeColor) {
        int current = percentage(totalBars, completedPercentage);
        return getProgressBar(totalBars, current, symbol, incompleteColor, completeColor);
    }

    /**
     * Generate a new progress bar
     * @param totalBars The amount of bars in the string
     * @param completeAmount The completed amount of the bars
     * @param symbol The single bar symbol
     * @param incompleteColor The incomplete color zone of the bar
     * @param completeColor The complete color zone of the bar
     * @return The bar string
     */
    public static String getProgressBar(int totalBars, int completeAmount, char symbol, ChatColor incompleteColor, ChatColor completeColor) {
        return incompleteColor + Strings.repeat(symbol, completeAmount) + "|" + completeColor + Strings.repeat(symbol, totalBars - completeAmount);
    }


    /**
     * This algorithm calculates the part of the total with respect to the percentage.
     * @param max The total part
     * @param percentage The percentage part respect {@param max}.
     * @return A total amount part
     */
    public static int percentage(int max, short percentage) {
        return Math.round((float) (max * percentage) / config.getDonationGoal());
    }/**
     * This algorithm calculates the percentage of a part of the total.
     * @param max The total part
     * @param part The part that will become a percentage
     * @return The percentage that represent the part of the total.
     */
    public static int percentage(int max, int part) {
        return Math.round((float) (part * 100) /max);
    }
}