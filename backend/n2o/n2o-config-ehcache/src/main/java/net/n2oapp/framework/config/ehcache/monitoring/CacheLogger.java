package net.n2oapp.framework.config.ehcache.monitoring;

import net.n2oapp.framework.config.ehcache.monitoring.service.CacheStatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author V. Alexeev.
 * Собирает снэпшоты статистики кэшев и пишит в лог.
 */
public class CacheLogger {

    private final CacheTuner cacheTuner;
    private final static Logger logger = LoggerFactory.getLogger(CacheLogger.class);


    public CacheLogger(CacheTuner cacheTuner) {
        this.cacheTuner = cacheTuner;
    }


    public void log() {
        cacheTuner.getEnabledStatisticEhcaches().stream()
                .map(cacheTuner::cache)
                .map(CacheStatisticService::map)
                .map(this::viewToLog)
                .forEach(logger::debug);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String viewToLog(CacheStatisticService.View v) {
        return new StringBuilder().append("Name:").append(v.name).append(";hits:").append(v.hits).append(";miss:").append(v.miss)
                .append(";usefulness:").append(v.usefulness).append(";size:").append(v.currentSize)
                .append(";fullness:").append(v.fullness).append(";").toString();
    }
}
