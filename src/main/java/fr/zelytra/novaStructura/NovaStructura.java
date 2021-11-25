package fr.zelytra.novaStructura;

import fr.zelytra.novaStructura.commands.Test;
import fr.zelytra.novaStructura.commands.structure.StructureCommand;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.logs.Logs;
import fr.zelytra.novaStructura.manager.structure.StructureManager;
import fr.zelytra.novaStructura.utils.Message;
import org.bukkit.plugin.java.JavaPlugin;

public final class NovaStructura extends JavaPlugin {

    private static NovaStructura instance;
    private static Logs logs;

    public static boolean debugMod = true;
    public static String version = "v1.0";
    public static boolean isReloading = false;

    public static StructureManager structureManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Message.startUpMessage();
        regCommands();

        isReloading = false;
        logs = new Logs();
        structureManager = new StructureManager();

        log("Started and ready to generate !", LogType.INFO);
    }

    @Override
    public void onDisable() {

    }

    private void regCommands() {
        getCommand("test").setExecutor(new Test());
        getCommand("novastruct").setExecutor(new StructureCommand());
    }

    public static void log(String msg, LogType type) {
        if (debugMod) {
            NovaStructura.getInstance().getServer().getConsoleSender().sendMessage(Message.CONSOLE_PREFIX.getMsg() + type.getColor() + msg);
        }
        logs.log(msg, type);

    }

    public static NovaStructura getInstance() {
        return instance;
    }
}
