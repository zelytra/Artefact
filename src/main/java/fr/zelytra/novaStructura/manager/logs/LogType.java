/*
 * Copyright (c) 2021.
 * Made by Zelytra :
 *  - Website : https://zelytra.fr
 *  - GitHub : http://github.zelytra.fr
 *
 * All right reserved
 */

package fr.zelytra.novaStructura.manager.logs;

import org.bukkit.ChatColor;

public enum LogType {
    INFO("\u001B[36m", "INFO", ChatColor.GOLD),
    WARN("\u001B[33m", "WARN", ChatColor.YELLOW),
    ERROR("\u001B[31m", "ERROR", ChatColor.RED);

    private final String consoleColor;
    private final String name;
    private final ChatColor color;

    LogType(String consoleColor, String name, ChatColor color) {

        this.consoleColor = consoleColor;
        this.name = name;
        this.color = color;

    }

    public String getConsoleColor() {
        return consoleColor;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor(){
        return color;
    }
}
