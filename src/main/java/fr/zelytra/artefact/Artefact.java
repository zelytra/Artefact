package fr.zelytra.artefact;

import fr.zelytra.artefact.manager.logs.LogType;
import fr.zelytra.artefact.manager.logs.Logs;
import fr.zelytra.artefact.manager.structure.StructureLoader;
import fr.zelytra.artefact.utils.Message;
import org.bukkit.plugin.java.JavaPlugin;

public final class Artefact extends JavaPlugin {

    private static Artefact instance;
    private static Logs logs;

    public static boolean debugMod = true;
    public static String version = "v1.0";
    public static boolean isReloading = false;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(Message.CONSOLE_STARTUP.getMsg());

        isReloading = false;
        logs = new Logs();
        StructureLoader.load();
        logs.log("§aArtefact started and ready to generate !",LogType.INFO);
    }

    @Override
    public void onDisable() {

    }

    private void regCommands() {

    }

    public static void log(String msg, LogType type) {
        if (debugMod) {
            Artefact.getInstance().getServer().getConsoleSender().sendMessage(Message.CONSOLE_PREFIX.getMsg() + msg);
        }
        logs.log(msg, type);

    }

    public static Artefact getInstance() {
        return instance;
    }
}
