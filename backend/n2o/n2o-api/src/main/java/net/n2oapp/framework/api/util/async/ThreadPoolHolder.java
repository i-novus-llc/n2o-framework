package net.n2oapp.framework.api.util.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: operhod
 * Date: 21.01.14
 * Time: 13:50
 */
public class ThreadPoolHolder {

    protected static ExecutorService pool;

    static {
        createPool();
    }

    public static ExecutorService getThreadPool() {
        if (poolIsShutdown()) createPool();
        return pool;
    }

    public static synchronized void shutdownPool() {
        pool.shutdown();
    }


    private static boolean poolIsShutdown() {
        return pool == null || pool.isShutdown();
    }

    private synchronized static void createPool() {
        pool = Executors.newFixedThreadPool(5);
    }

}
