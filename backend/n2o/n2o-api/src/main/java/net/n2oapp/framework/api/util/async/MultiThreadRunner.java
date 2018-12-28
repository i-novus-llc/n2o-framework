package net.n2oapp.framework.api.util.async;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Утилита для проверки потокобезопасности объекта
 * @author iryabov
 * @since 11.04.2015
 */
public class MultiThreadRunner {
    //число потоков
    private int threadMax = 50;
    //время ожидания (сек)
    private int timeout = 1;

    public MultiThreadRunner() {
    }

    /**
     * @param threadMax - число потоков
     * @param timeout - время ожидания (в секундах), после которого потоки прервутся
     */
    public MultiThreadRunner(int threadMax, int timeout) {
        this.threadMax = threadMax;
        this.timeout = timeout;
    }

    /**
     * Запуск теста в несколько потоков
     * @param callable - тело теста, должен вернуть результат assert'а
     * @return метод возвращает число ошибок (число не верных асертов)
     */
    public int run(Callable<Boolean> callable) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newCachedThreadPool();
        ArrayList<Future<Boolean>> results = new ArrayList<>();

        for(int i = 0; i < threadMax; i++) {
            results.add(executor.submit(callable));
        }

        int miscalculations = 0;
        for(Future<Boolean> result : results) {
            Boolean res = result.get();

            if (!res) {
                System.out.println("Incorrect result, expected [true], but got [false]'");
                miscalculations += 1;
            }
        }

        executor.awaitTermination(timeout, TimeUnit.SECONDS);
        executor.shutdownNow();

        System.out.println("Overall: " + miscalculations + " wrong values.");
        return miscalculations;
    }
}
