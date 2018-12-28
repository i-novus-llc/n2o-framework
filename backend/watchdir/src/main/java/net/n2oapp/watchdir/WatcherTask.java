package net.n2oapp.watchdir;

/**
 * @author V. Alexeev.
 * использовать только как синглтон. 1 WatchDir = 1 WathcerTask
 */
class WatcherTask implements Runnable {

    private final WatchDir watcher;

    public WatcherTask(WatchDir watcher) {
        this.watcher = watcher;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("WATCHDIR");
        watcher.processEvents();
    }

}
