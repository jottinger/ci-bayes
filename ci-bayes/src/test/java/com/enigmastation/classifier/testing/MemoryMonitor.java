package com.enigmastation.classifier.testing;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;


public class MemoryMonitor extends Thread {
    boolean monitoring = false;

    public boolean isMonitoring() {
        return monitoring;
    }

    public void setMonitoring(boolean monitoring) {
        this.monitoring = monitoring;
    }

    List<MemoryUsage> heapMemorytrace = new ArrayList<MemoryUsage>();
    List<MemoryUsage> nonheapMemorytrace = new ArrayList<MemoryUsage>();
    MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
    long maxHeap = 0;
    long maxNonheap = 0;

    public void run() {
        while (!interrupted()) {
            try {
                Thread.sleep(250);
                if (monitoring) {
                    MemoryUsage mu = mxBean.getHeapMemoryUsage();
                    if (mu.getMax() > maxHeap) {
                        maxHeap = mu.getMax();
                        System.out.println("new max heap usage: " + maxHeap);
                    }
                    heapMemorytrace.add(mu);
                    mu = mxBean.getNonHeapMemoryUsage();
                    if (mu.getMax() > maxNonheap) {
                        maxNonheap = mu.getMax();
                        System.out.println("new max nonheap usage: " + maxNonheap);
                    }
                    nonheapMemorytrace.add(mu);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void shutdown() {
        dump();
    }

    public void dump() {
        for (MemoryUsage mu : heapMemorytrace) {
            System.out.println(mu);
        }
    }
}
