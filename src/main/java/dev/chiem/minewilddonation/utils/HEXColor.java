package dev.chiem.minewilddonation.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HEXColor {
    private static final Pattern hexPattern = Pattern.compile("&#([0-9a-fA-F]{6})");
    private static final Pattern formattingPattern = Pattern.compile("&([0-9a-fk-or])");
    private static final Pattern stripColorPattern = Pattern.compile("(?i)&[0-9A-FK-ORX]");

    public static Collection<String> parse(final Collection<String> list) {
        return list.stream().map(HEXColor::hex).map(HEXColor::parse).collect(Collectors.toList());
    }

    public static List<String> parse(final List<String> list) {
        return list.stream().map(HEXColor::hex).map(HEXColor::parse).collect(Collectors.toList());
    }

    public static List<String> parse(final String... list) {
        return Arrays.stream(list).map(HEXColor::hex).map(HEXColor::parse).collect(Collectors.toList());
    }

    public static String parse(final String s) {
        // First, replace hex color codes
        String hexReplaced = hex(s);

        // Then, replace other color codes
        return ChatColor.translateAlternateColorCodes('&', hexReplaced);
    }


    public static String hex(String s) {
        Matcher match = hexPattern.matcher(s);
        while (match.find()) {
            String c = s.substring(match.start(), match.end());
            s = s.replace(c, ChatColor.of("#" + c.substring(2)).toString());
            match = hexPattern.matcher(s);
        }
        return s;
    }

    public static BaseComponent[] toComponent(final String string) {
        return TextComponent.fromLegacyText(parse(HEXColor.parse(string)));
    }

    public static String strip(final String s) {
        return stripColorPattern.matcher(s).replaceAll("");
    }
}
