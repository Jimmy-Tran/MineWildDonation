package dev.chiem.minewilddonation.utils;


import com.sun.org.apache.xerces.internal.xs.StringList;
import dev.chiem.minewilddonation.MineWildDonation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import net.md_5.bungee.api.ChatColor;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;

public class FaceAPI
{
    private static final char TRANSPARENT_CHAR = ' ';
    private Color[] colors = null;
    private MineWildDonation plugin;
    private String[] lines;
    public ProgressBar progressbar;


    public FaceAPI(MineWildDonation plugin,  final BufferedImage image, final int height, final char imgChar) {
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 255, 85), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 170, 0), new Color(255, 255, 255) };
        final ChatColor[][] chatColors = this.toChatColorArray(image, height);
        this.lines = this.toImgMessage(chatColors, imgChar);
        this.progressbar = new ProgressBar();
        this.plugin = plugin;
    }

    public FaceAPI(MineWildDonation plugin, final ChatColor[][] chatColors, final char imgChar) {
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 255, 85), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 170, 0), new Color(255, 255, 255) };
        this.lines = this.toImgMessage(chatColors, imgChar);
        this.progressbar = new ProgressBar();
        this.plugin = plugin;
    }

    public FaceAPI(MineWildDonation plugin, final String... imgLines) {
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 255, 85), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 170, 0), new Color(255, 255, 255) };
        this.lines = imgLines;
        this.progressbar = new ProgressBar();
        this.plugin = plugin;
    }

    public FaceAPI appendText(final String... text) {
        for (int y = 0; y < this.lines.length; ++y) {
            if (text.length > y) {
                final StringBuilder sb = new StringBuilder();
                final String[] lines = this.lines;
                final int n = y;
                lines[n] = sb.append(lines[n]).append(" ").append(text[y]).toString();
            }
        }
        return this;
    }

    public FaceAPI appendCenteredText(final String... text) {
        for (int y = 0; y < this.lines.length; ++y) {
            if (text.length <= y) {
                return this;
            }
            final int len = 65 - this.lines[y].length();
            this.lines[y] += this.center(text[y], len);
        }
        return this;
    }

    private ChatColor[][] toChatColorArray(final BufferedImage image, final int height) {
        final double ratio = image.getHeight() / (double)image.getWidth();
        int width = (int)(height / ratio);
        if (width > 10) {
            width = 10;
        }
        final BufferedImage resized = this.resizeImage(image, (int)(height / ratio), height);
        final ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); ++x) {
            for (int y = 0; y < resized.getHeight(); ++y) {
                final int rgb = resized.getRGB(x, y);
                final ChatColor color = ChatColor.of(new Color(rgb, true));
                chatImg[x][y] = color;
            }
        }
        return chatImg;
    }

    private String[] toImgMessage(final ChatColor[][] colors, final char imgchar) {
        final String[] lines = new String[colors[0].length];
        for (int y = 0; y < colors[0].length; ++y) {
            String line = "";
            for (int x = 0; x < colors.length; ++x) {
                final ChatColor color = colors[x][y];
                line += ((color != null) ? (colors[x][y].toString() + imgchar) : Character.valueOf(' '));
            }
            lines[y] = line + ChatColor.RESET;
        }
        return lines;
    }

    private BufferedImage resizeImage(final BufferedImage originalImage, final int width, final int height) {
        final AffineTransform af = new AffineTransform();
        af.scale(width / (double)originalImage.getWidth(), height / (double)originalImage.getHeight());
        final AffineTransformOp operation = new AffineTransformOp(af, 1);
        return operation.filter(originalImage, null);
    }

    private double getDistance(final Color c1, final Color c2) {
        final double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        final double r = c1.getRed() - c2.getRed();
        final double g = c1.getGreen() - c2.getGreen();
        final int b = c1.getBlue() - c2.getBlue();
        final double weightR = 2.0 + rmean / 256.0;
        final double weightG = 4.0;
        final double weightB = 2.0 + (255.0 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    private boolean areIdentical(final Color c1, final Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
    }

    private ChatColor getClosestChatColor(final Color color) {
        if (color.getAlpha() < 128) {
            return null;
        }
        int index = 0;
        double best = -1.0;
        for (int i = 0; i < this.colors.length; ++i) {
            if (this.areIdentical(this.colors[i], color)) {
                return ChatColor.values()[i];
            }
        }
        for (int i = 0; i < this.colors.length; ++i) {
            final double distance = this.getDistance(color, this.colors[i]);
            if (distance < best || best == -1.0) {
                best = distance;
                index = i;
            }
        }
        return ChatColor.values()[index];
    }

    private String center(final String s, final int length) {
        if (s.length() > length) {
            return s.substring(0, length);
        }
        if (s.length() == length) {
            return s;
        }
        final int leftPadding = (length - s.length()) / 2;
        final StringBuilder leftBuilder = new StringBuilder();
        for (int i = 0; i < leftPadding; ++i) {
            leftBuilder.append(" ");
        }
        return leftBuilder.toString() + s;
    }

    public String[] getLines() {
        return this.lines;
    }


    public void sendToPlayer(List<String> stringslist, String playerName, int donationAmount) {
        String[] innerLines = this.lines;
        String[] outerLines = stringslist.toArray(new String[stringslist.size()]);

        int maxLength = Math.max(innerLines.length, outerLines.length);

        for (int i = 0; i < maxLength; i++) {
            String innerLine = i < innerLines.length ? innerLines[i] : "";
            String outerLine = i < outerLines.length ? outerLines[i] : "";

            String replacedMessage = outerLine
                    .replace("{player}", playerName)
                    .replace("{amount}", String.valueOf(donationAmount))
                    .replace("{progress_bar}", progressbar.getProgressBar(20, 100, 50, '|', org.bukkit.ChatColor.GREEN, org.bukkit.ChatColor.RED) + "§8]");

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(innerLine + " " + HEXColor.parse(replacedMessage));
            }
        }
    }
    private String replacePlaceholders(String input, String playerName, double donationAmount, int progressPercentage, String progressBar) {
        String output = input;

        if (output.contains("{player}")) {
            output = output.replace("{player}", playerName);
        }
        if (output.contains("${amount}")) {
            output = output.replace("${amount}", String.format("%.1f", donationAmount));
        }
        if (output.contains("{percentage}")) {
            output = output.replace("{percentage}", String.format("%d%%", progressPercentage));
        }
        if (output.contains("{progress_bar}")) {
            output = output.replace("{progress_bar}", progressBar);
        }

        return output;
    }
}