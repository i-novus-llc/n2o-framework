package net.n2oapp.framework.sandbox.cases;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Класс для тестирования медленных действий
 */
public abstract class Sleeper {
    private static final AtomicLong COUNTER = new AtomicLong(0);

    public static long sleep(long sec) {
        try {
            Thread.sleep(sec * 1000);
            return COUNTER.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    public static long sleep() {
        return sleep(2);
    }
}
