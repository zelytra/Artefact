package fr.zelytra.novaStructura.utils;

import fr.zelytra.novaStructura.NovaStructura;
import org.bukkit.Bukkit;

public enum Message {

    PLAYER_PREFIX("§8[§6Nova§eStructura§8]§r "),
    CONSOLE_PREFIX("§8[§6Nova§eStructura§8]§r ");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMsg() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }

    public static void startUpMessage() {
        Bukkit.getConsoleSender().sendMessage("§8|   §6" + "             __                        ");
        Bukkit.getConsoleSender().sendMessage("§8|   §6" + "|\\| _     _ (_ _|_ __    _ _|_    __ _ ");
        Bukkit.getConsoleSender().sendMessage("§8|   §6" + "| |(_)\\_/(_|__) |_ | |_|(_  |_|_| | (_|");
        Bukkit.getConsoleSender().sendMessage("§8|   §6");
        Bukkit.getConsoleSender().sendMessage("§8|   §6by §6Zelytra");
        Bukkit.getConsoleSender().sendMessage("§8|   " + NovaStructura.version);
        Bukkit.getConsoleSender().sendMessage("§8|   §6");
    }

}
