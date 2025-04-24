package net.n2oapp.framework.sandbox.engine.thread_local;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Хранение id проекта как локальной переменной потока
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadLocalProjectId {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String getProjectId() {
        return threadLocal.get();
    }

    public static void setProjectId(String projectId) {
        threadLocal.set(projectId);
    }

    public static void clear() {
        threadLocal.remove();
    }
}
