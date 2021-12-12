package fr.zelytra.novaStructura.events;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.schematic.selector.SelectorListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    public static void regEvents(NovaStructura pl) {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new OnNewChunk(), pl);
        pm.registerEvents(new ReloadingServer(), pl);
        pm.registerEvents(new SelectorListener(), pl);



    }

}
