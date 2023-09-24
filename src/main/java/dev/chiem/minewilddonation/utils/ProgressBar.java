package dev.chiem.minewilddonation.utils;


import jdk.internal.joptsimple.internal.Strings;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

public final class ProgressBar {

    public String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                 ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return StringUtils.repeat("" + completedColor + symbol, progressBars)
                + StringUtils.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}