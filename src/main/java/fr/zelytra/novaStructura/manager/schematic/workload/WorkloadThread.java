package fr.zelytra.novaStructura.manager.schematic.workload;

import com.google.common.collect.Queues;
import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import org.bukkit.Bukkit;

import java.util.ArrayDeque;
import java.util.List;

public class WorkloadThread implements Runnable {

    private static final int MAX_MS_PER_TICK = 30;
    private static long time;
    private final ArrayDeque<WorkLoad> workloadDeque;

    public WorkloadThread() {
        workloadDeque = Queues.newArrayDeque();
    }

    public void addLoad(WorkLoad workload) {
        workloadDeque.add(workload);

    }

    public void addAllLoad(List<WorkLoad> workLoads) {
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

            if (workloadDeque.size() > 50000 && ((System.currentTimeMillis() - time) / 1000.0) % 1 == 0) {
                time = System.currentTimeMillis();
                NovaStructura.log("WorkLoad currently running heavy task : §e" + workloadDeque.size() + " blocks remaining to paste", LogType.WARN);
            }
        }, 0, 2);

    }

}
