package dev.chiem.minewilddonation;

import dev.chiem.minewilddonation.commands.DonationCommand;
import dev.chiem.minewilddonation.utils.FaceAPI;
import dev.chiem.minewilddonation.utils.ProgressBar;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineWildDonation extends JavaPlugin {
    private ProgressBar progressBar;
    public boolean playerDonated;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("donation").setExecutor(new DonationCommand(this));

//        new FaceAPI(this);
        System.out.println("Enabled MineWildDonation");

    }


    @Override
    public void onDisable() {
        System.out.println("Disabled MineWildDonation");
    }


    public boolean getPlayerDonated() {
        return playerDonated;
    }

    public void setPlayerDonated(boolean b) {
        playerDonated = b;
    }
}
