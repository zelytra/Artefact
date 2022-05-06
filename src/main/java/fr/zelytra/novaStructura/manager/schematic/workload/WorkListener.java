package fr.zelytra.novaStructura.manager.schematic.workload;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import fr.zelytra.novaStructura.NovaStructura;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WorkListener implements Listener {

    @EventHandler
    public void onMainThreadEnd(ServerTickStartEvent e) {
        NovaStructura.workloadThread.run();
    }

}
