package fr.zelytra.novaStructura.manager.schematic.wordload;

import com.google.common.collect.Queues;
import fr.zelytra.novaStructura.NovaStructura;
import org.bukkit.Bukkit;

import java.util.ArrayDeque;
import java.util.List;

public class WorkloadThread implements Runnable {

    private static final int MAX_MS_PER_TICK = 30;
    private final ArrayDeque<WorkLoad> workloadDeque;

    public WorkloadThread() {
        workloadDeque = Queues.newArrayDeque();
    }

    public void addLoad(WorkLoad workload) {
        workloadDeque.add(workload);

    }

    public void addAllLoad(List<WorkLoad> workLoads){
        workloadDeque.addAll(workLoads);
    }

    @Override
    public void run() {

        Bukkit.getScheduler().runTaskTimer(NovaStructura.getInstance(), () -> {
            long stopTime = System.currentTimeMillis() + MAX_MS_PER_TICK;

            while (!workloadDeque.isEmpty() && System.currentTimeMillis() <= stopTime) {
                SetBlock block = (SetBlock) workloadDeque.poll();
                block.compute();
            }
        }, 0, 20);

    }

}
