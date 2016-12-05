package data_structures;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class WorkerThread<T extends Comparable<T>> extends Thread {
    private final int id;
    private final Sorted<T> sorted;
    private final T[] itemsToAdd;
    private final T[] itemsToRemove;
    private final int workTime;
    private final boolean doDebug;
    private final CyclicBarrier barrier;

    public WorkerThread(int id, Sorted<T> list, T[] itemsToAdd,
            T[] itemsToRemove, int workTime, CyclicBarrier barrier,
            boolean debug) {
        this.sorted = list;
        this.id = id;
        this.itemsToAdd = itemsToAdd;
        this.itemsToRemove = itemsToRemove;
        this.workTime = workTime;
        this.barrier = barrier;
        this.doDebug = debug;
    }

    // TODO: Remove before submitting - Testing purposes only
    private final List<Long> addTime = new ArrayList<>();
    private final List<Long> removeTime = new ArrayList<>();
    private Long executionTime;

    @Override
    public void run() {
        // TODO: Remove before submitting - Testing purposes only
        long begin = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        // First: add my items.
        for (T t : itemsToAdd) {
            doWork();
            // TODO: Remove before submitting - Testing purposes only
            long start = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
            sorted.add(t);
            // TODO: Remove before submitting - Testing purposes only
            long end = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
            addTime.add(end-start);
        }

        // Barrier, and possibly print result.
        // To test inter-operability of add() and remove(), you can comment out
        // this barrier so that some threads may start removing while other
        // threads are still adding.
        try {
            barrier.await();
            if (this.doDebug) {
                if (this.id == 0) {
                    System.out.printf(
                            "Output after adding, before removing:\n%s\n",
                            sorted.toArrayList().toString());
                }
                barrier.await();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // Remove my items.
        for (T t : itemsToRemove) {
            doWork();
            // TODO: Remove before submitting - Testing purposes only
            long start = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
            sorted.remove(t);
            // TODO: Remove before submitting - Testing purposes only
            long end = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
            removeTime.add(end-start);
        }
        // TODO: Remove before submitting - Testing purposes only
        long endd = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        this.executionTime = endd - begin;
    }

    private void doWork() {
        if (workTime > 0) {
            // Do some work in between operations. workTime indicates the cpu
            // time to be consumed, in microseconds.
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            long start = bean.getCurrentThreadCpuTime();

            // getCurrentThreadCpuTime() returns cpu time in nano seconds.
            long end = start + workTime * 1000;
            while (bean.getCurrentThreadCpuTime() < end)
                ; // busy until we used enough cpu time.
        }
    }

    // TODO: Remove before submitting - Testing purposes only
    public List<Long> getAddTime() {
        return addTime;
    }
    // TODO: Remove before submitting - Testing purposes only
    public List<Long> getRemoveTime() {
        return removeTime;
    }
    // TODO: Remove before submitting - Testing purposes only
    public Long getExecutionTime() {
        return executionTime;
    }
}
