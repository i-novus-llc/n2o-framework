package net.n2oapp.framework.config;

import net.n2oapp.framework.api.event.N2oReadyEvent;
import net.n2oapp.framework.api.event.N2oStartedEvent;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.event.N2oStoppedEvent;
import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.watchdir.WatchDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Запуск\перезапуск конфигурационных файлов n2o
 */
public class ConfigStarter {
    private static final Logger logger = LoggerFactory.getLogger(ConfigStarter.class);

    private volatile static boolean wasRunning = false;
    private final static ReadWriteLock startingLock = new ReentrantReadWriteLock();//блокировка на время старта

    private final N2oEventBus eventBus;
    private final ConfigMetadataLocker locker;
    private final WatchDir watchDir;
    private final Collection<String> configPaths;
    private final N2oApplicationBuilder applicationBuilder;

    @Value("${n2o.config.monitoring.enabled}")
    private boolean monitoringEnabled = false;
    @Value("${n2o.config.ignores}")
    private List<String> monitoringIgnores = Collections.emptyList();

    public ConfigStarter(N2oApplicationBuilder applicationBuilder,
                         N2oEventBus eventBus,
                         ConfigMetadataLocker locker,
                         WatchDir watchDir,
                         Collection<String> configPath) {
        this.eventBus = eventBus;
        this.locker = locker;
        this.applicationBuilder = applicationBuilder;
        this.watchDir = watchDir;
        this.configPaths = configPath;
    }

    public void restart() {
        if (!startingLock.writeLock().tryLock()) return;
        try {
            syncStop();
            locker.unlock();
            syncStart();
        } finally {
            startingLock.writeLock().unlock();
        }
        eventBus.publish(new N2oReadyEvent(this));
    }

    public void start() {
        if (wasRunning) return;
        if (!startingLock.writeLock().tryLock()) return;
        try {
            syncStart();
        } finally {
            startingLock.writeLock().unlock();
        }
        eventBus.publish(new N2oReadyEvent(this));
    }

    public String getConfigPath() {
        return configPaths == null || configPaths.isEmpty() ? null : configPaths.iterator().next();
    }

    private void syncStart() {
        logger.debug("N2O is starting");
        doRegisterInfo();
        startMonitoringXml();
        wasRunning = true;
        eventBus.publish(new N2oStartedEvent(this));
        ScriptProcessor.getScriptEngine();//warmup
        logger.info("N2O was started");
    }

    private void doRegisterInfo() {
        applicationBuilder.scan();
    }


    private void startMonitoringXml() {
        boolean watchEnabled = monitoringEnabled;
        if (!watchEnabled)
            return;
        //получить путь к папке
        if (configPaths == null || configPaths.isEmpty()) {
            logger.info("Monitoring did not start: path is null");
            return;
        }
        logger.info("Start monitoring path: " + configPaths);

        //Запустить мониторинг файлов
        for (String path : configPaths) {
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
        }
        MetadataEnvironment environment = applicationBuilder.getEnvironment();
        watchDir.setListener(new XMLChangeListener(configPaths,
                environment.getMetadataRegister(),
                environment.getSourceTypeRegister(),
                eventBus));
        for (String path : configPaths) {
            watchDir.addPath(path);
            monitoringIgnores.forEach((skip -> watchDir.skipOn(PathUtil.concatAbsoluteAndLocalPath(path, skip))));
        }
        watchDir.start();
    }

    private void stopMonitoringXml() {
        watchDir.stop();
    }

    public void stop() {
        if (!wasRunning) return;
        if (!startingLock.writeLock().tryLock()) return;
        try {
            syncStop();
        } finally {
            startingLock.writeLock().unlock();
        }
    }

    private void syncStop() {
        logger.debug("N2O is stopping");
        wasRunning = false;
        stopMonitoringXml();
        eventBus.publish(new N2oStoppedEvent(this));
        logger.info("N2O was stopped");
    }

    /**
     * Ждать пока стартует конфигурация
     */
    public static void waitUntilStarted() {
        startingLock.readLock().lock();
        startingLock.readLock().unlock();
    }

    public void setMonitoringEnabled(boolean monitoringEnabled) {
        this.monitoringEnabled = monitoringEnabled;
    }
}
