package dev.chiem.minewilddonation;

import org.bukkit.plugin.java.JavaPlugin;

public final class MineWildDonation extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Enabled MineWildDonation");

    }

    @Override
    public void onDisable() {
        System.out.println("Disabled MineWildDonation");
    }

    public ConfigUtil getConfigUtil() {
        return config;
    }


    public boolean getPlayerDonated() {
        return playerDonated;
    }

    public void setPlayerDonated(boolean b) {
        playerDonated = b;
    }
}
