package net.n2oapp.framework.config.ehcache.monitoring;

import net.n2oapp.framework.config.ehcache.monitoring.api.CacheConfig;
import net.n2oapp.properties.StaticProperties;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author V. Alexeev.
 */
public class CacheTuner {

    private final Map<String, Ehcache> ehcacheMap;
    private final Set<String> enabledStatisticEhcaches = new HashSet<>();

    public CacheTuner(CacheManager cacheManager, String monitoring) {
        List<String> monitoringList = monitoringList(monitoring);
        Collection<String> cacheNames = cacheManager.getCacheNames();
        Map<String, Ehcache> map = cacheNames.stream().map(name -> cacheManager.getCache(name).getNativeCache())
                .map(Ehcache.class::cast).peek(cache -> statisticEnableIfNeed(cache, monitoringList))
                .collect(toMap(Ehcache::getName, identity()));
        ehcacheMap = Collections.unmodifiableMap(map);
    }

    /**
     * Изменение конфигурации cache
     * @param config dto класс со всеми возможными насройками
     */
    public void configure(CacheConfig config) {
        Ehcache ehcache = cache(config.name);
        CacheConfiguration configuration = ehcache.getCacheConfiguration();
        CacheConfig prepared = prepareToConfigure(config, configuration);
        configuration.setTimeToLiveSeconds(prepared.timeToLive);
        configuration.setTimeToIdleSeconds(prepared.timeToIdle);
        if (config.isElementsSettings()) {
            configuration.setMaxEntriesLocalHeap(prepared.maxEntriesLocalHeap);
            configuration.setMaxEntriesLocalDisk(prepared.maxEntriesLocalDisk);
        } else {
            configuration.setMaxBytesLocalHeap(prepared.maxBytesLocalHeap);
            configuration.setMaxBytesLocalDisk(prepared.maxBytesLocalDisk);
        }
    }

    /**
     * Очистка кеша
     * @param name название региона кэша
     */
    public void clearCache(String name) {
        cache(name).removeAll();
    }

    /**
     * Выключение мониторинга кэша
     * @param name название региона кэша
     */
    public void disableMonitoring(String name) {
        //todo нет такого метода в 2.10
    }

    /**
     * Включение мониторинга кэша
     * @param name название региона кэша
     */
    public void enableMonitoring(String name) {
        //todo нет такого метода в 2.10
    }

    /**
     * Очистить статистику кэша
     * @param name название региона кэша
     */
    public void clearStatistic(String name) {
        //todo нет такого метода в 2.10 cache(name).getStatistics().clearStatistics();
    }

    /**
     * @return все найденные EhCache-ы в переданном CacheManager
     */
    public Collection<Ehcache> getAllCaches() {
        return ehcacheMap.values();
    }

    /**
     * @param name название региона кэша
     * @return cache
     * @throws IllegalStateException если такого кэша нет
     */
    public Ehcache cache(String name) {
        Ehcache ehcache = ehcacheMap.get(name);
        if (ehcache == null) {
            throw new IllegalStateException();
        } return ehcache;
    }

    /**
     * @return копия сета с названиями всех кэшев с включённым сбором статистики
     */
    public Set<String> getEnabledStatisticEhcaches() {
        return new HashSet<>(enabledStatisticEhcaches);
    }

    private CacheConfig prepareToConfigure(CacheConfig config, CacheConfiguration configuration) {
        CacheConfig c = new CacheConfig();
        c.name = config.name;
        c.timeToLive = config.timeToLive != null ? config.timeToLive : configuration.getTimeToLiveSeconds();
        c.timeToIdle = config.timeToIdle != null ? config.timeToIdle : configuration.getTimeToIdleSeconds();
        c.maxBytesLocalDisk = config.maxBytesLocalDisk != null ? config.maxBytesLocalDisk : configuration.getMaxBytesLocalDisk();
        c.maxBytesLocalHeap = config.maxBytesLocalHeap != null ? config.maxBytesLocalHeap : configuration.getMaxBytesLocalHeap();
        c.maxEntriesLocalDisk = config.maxEntriesLocalDisk != null ? config.maxEntriesLocalDisk : configuration.getMaxEntriesLocalDisk();
        c.maxEntriesLocalHeap = config.maxEntriesLocalHeap != null ? config.maxEntriesLocalHeap : configuration.getMaxEntriesLocalHeap();
        return c;
    }

    private List<String> monitoringList(String monitoring) {
        if (monitoring.equals("ALL")) {
            return Collections.singletonList(monitoring);
        }
        if (monitoring.equals("NONE")) {
            return Collections.emptyList();
        }
        return Arrays.asList(monitoring.replaceAll("\\s*", "").split(","));
    }

    private void statisticEnableIfNeed(Ehcache ehcache, List<String> list) {
        if (list.contains("ALL") || list.contains(ehcache.getName())) {
            //todo нет такого метода в 2.10 ehcache.setStatisticsEnabled(true);
            enabledStatisticEhcaches.add(ehcache.getName());
        }
    }

}
