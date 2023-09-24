package dev.chiem.minewilddonation.commands;

import dev.chiem.minewilddonation.MineWildDonation;
import dev.chiem.minewilddonation.utils.FaceAPI;
import dev.chiem.minewilddonation.utils.HEXColor;
import dev.chiem.minewilddonation.utils.ImageChar;
import dev.chiem.minewilddonation.utils.ProgressBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class DonationCommand implements CommandExecutor {
    public ProgressBar progressbar;

    private MineWildDonation plugin;

    public DonationCommand(MineWildDonation plugin) {
        this.plugin = plugin;
        this.progressbar = new ProgressBar();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

            if(strings.length == 2) {
                BufferedImage imageToSend = null;
                try {
                    imageToSend = ImageIO.read(new URL("https://minotar.net/avatar/" + strings[0] + "/8.png"));
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                new FaceAPI(plugin, imageToSend, 8, ImageChar.BLOCK.getChar()).sendToPlayer(plugin.getConfig().getStringList("donation.messages"), strings[0], Integer.parseInt(strings[1]));
//                for(String string : plugin.getConfig().getStringList("donation.messages")) {
//                    String replacedMessage = string
//                            .replace("{player}", strings[0])
//                            .replace("{amount}", strings[1])
//                            .replace("{progress_bar}", progressbar.getProgressBar(20, 100, 50, '|', ChatColor.GREEN, ChatColor.RED) + "§8]");
//                    for (Player p : Bukkit.getOnlinePlayers()) {
//                        p.sendMessage(HEXColor.parse(replacedMessage));
//                    }
//
//                }
                return true;
            }
//            int percent = 20 * 100 / 100; //The first 20 is my current amount, the first 100 is just to make it bigger, the second 100 is the maximum you can get, so 100 in this case
//            double b = Math.round(percent * 10.0) / 10.0 ;
//            player.sendMessage("§e§lPERCENTAGE: " + b + "§e§l%");
        return false;
    }
}
