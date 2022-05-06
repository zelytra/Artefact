package fr.zelytra.novaStructura.manager.schematic.workload;

import com.google.common.collect.Queues;

import java.util.ArrayDeque;
import java.util.List;

public class WorkloadThread implements Runnable {

    private static final double MAX_MS_PER_TICK = 2.5;
    private static final int MAX_NANO_PER_TICK = (int) (MAX_MS_PER_TICK * 1E6);

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
        long stopTime = System.nanoTime() + MAX_NANO_PER_TICK;

        WorkLoad nextLoad;

        while (System.nanoTime() <= stopTime && (nextLoad = this.workloadDeque.poll()) != null)
            nextLoad.compute();

    }

}
