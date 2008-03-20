package com.enigmastation.testing;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;


public class MemoryMonitor extends Thread {
    boolean monitoring=false;

    public boolean isMonitoring() {
        return monitoring;
    }

    public void setMonitoring(boolean monitoring) {
        this.monitoring = monitoring;
    }

    List<MemoryUsage> heapMemorytrace=new ArrayList<MemoryUsage>();
    List<MemoryUsage> nonheapMemorytrace=new ArrayList<MemoryUsage>();
              MemoryMXBean mxBean= ManagementFactory.getMemoryMXBean();
    public void run() {
        while(!interrupted()) {
            try {
                Thread.sleep(250);
                if(monitoring) {
                    heapMemorytrace.add(mxBean.getHeapMemoryUsage());
                    nonheapMemorytrace.add(mxBean.getNonHeapMemoryUsage());
                }
            } catch (InterruptedException e) {
            }
        }
    }
    
    public void shutdown() {
        for(MemoryUsage mu:heapMemorytrace) {
            System.out.println(mu);
        }
    }
}
