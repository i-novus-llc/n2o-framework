package net.n2oapp.framework.engine.data;

public class ThreadLocalProjectId {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String getProjectId() {
        return threadLocal.get();
    }

    public static void setProjectId(String projectId) {
        threadLocal.set(projectId);
    }
}