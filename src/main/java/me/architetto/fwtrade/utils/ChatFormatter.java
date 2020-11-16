package me.architetto.fwtrade.utils;

import org.bukkit.ChatColor;

public class ChatFormatter {

    public static String formatSuccessMessage(String message) {
        message = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "FW" + ChatColor.RESET + "Trade >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatErrorMessage(String message) {
        message = ChatColor.DARK_RED + "" + ChatColor.BOLD + "FW" + ChatColor.RESET + "Trade >> " + ChatColor.RESET + message;
        return message;
    }

}
