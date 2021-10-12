package games.nautilus.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CC {

    public static String trns(@NotNull final String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> trns(final List<String> lines) {
        final List<String> toReturn = new ArrayList<>();
        for (final String line : lines) toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        return toReturn;
    }

    public static List<String> trns(final String[] lines) {
        final List<String> toReturn = new ArrayList<>();
        for (final String line : lines) if (line != null) toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        return toReturn;
    }

    public static ChatColor getLastColor(final String in) {
        char[] chars = in.toCharArray();
        ChatColor last = ChatColor.WHITE;
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == ChatColor.COLOR_CHAR) {
                if (i + 1 <= chars.length - 1) last = ChatColor.getByChar(chars[i+1]);
            }
        }
        return last;
    }

    public static Color getColor(ChatColor chatColor) {
        switch (chatColor) {
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case DARK_AQUA:
                return Color.TEAL;
            case DARK_BLUE:
                return Color.NAVY;
            case DARK_GRAY:
                return Color.SILVER;
            case GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case GREEN:
                return Color.LIME;
            case DARK_PURPLE:
                return Color.PURPLE;
            case LIGHT_PURPLE:
                return Color.FUCHSIA;
            case DARK_RED:
            case RED:
                return Color.RED;
            case GOLD:
                return Color.ORANGE;
            case YELLOW:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }
}