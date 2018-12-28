package net.n2oapp.framework.config.ehcache.monitoring.service;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.criteria.api.Sorting;
import net.n2oapp.framework.config.ehcache.monitoring.CacheTuner;
import net.n2oapp.framework.config.ehcache.monitoring.api.CacheCriteria;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.statistics.StatisticsGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static net.n2oapp.criteria.api.Direction.ASC;

/**
 * @author V. Alexeev.
 */
public class CacheStatisticService implements CollectionPageService<CacheCriteria, CacheStatisticService.View> {

    private static final String ENTRIES_SIZE = "%s / %s";
    private static final String BYTES_SIZE = "%s / %s %s";
    private static final Logger logger = LoggerFactory.getLogger(CacheStatisticService.class);
    private static final Comparator<View> DEFAULT_COMPARATOR = (v1, v2) -> v1.name.compareToIgnoreCase(v2.name);

    private final CacheTuner tuner;

    public CacheStatisticService(CacheTuner tuner) {
        this.tuner = tuner;
    }

    @Override
    public CollectionPage<View> getCollectionPage(CacheCriteria criteria) {
        List<View> views = tuner.getAllCaches().stream().map(CacheStatisticService::map).sorted(getComparator(criteria)).collect(toList());
        return new FilteredCollectionPage<>(views, criteria);
    }

    private Comparator<View> getComparator(final CacheCriteria criteria) {
        if (criteria.getSortings() == null || criteria.getSortings().isEmpty()) {
            return DEFAULT_COMPARATOR;
        }
        Optional<Comparator<View>> usefulness = criteria.getSortings().stream().filter(s -> s.getField().equals("usefulness")).findFirst()
                .map(s -> (Comparator<View>) (v1, v2) -> multiplier(s) * v1.usefulness.compareTo(v2.usefulness));

        Optional<Comparator<View>> name = criteria.getSortings().stream().filter(s -> s.getField().equals("name")).findFirst()
                .map(s -> (Comparator<View>) (v1, v2) -> multiplier(s) * v1.name.compareTo(v2.name));

        Optional<Comparator<View>> fullness = criteria.getSortings().stream().filter(s -> s.getField().equals("fullness")).findFirst()
                .map(s -> (Comparator<View>) (v1, v2) -> multiplier(s) * v1.fullness.compareTo(v2.fullness));

        return usefulness.orElse(name.orElse(fullness.orElse(DEFAULT_COMPARATOR)));
    }

    private int multiplier(Sorting sorting) {
        return sorting.getDirection().equals(ASC) ? 1 : -1;
    }

    public static View map(Ehcache cache) {
        View v = new View();
        v.name = cache.getName();

//        v.hasStatistic = cache.isStatisticsEnabled();
        StatisticsGateway statistics = cache.getStatistics();
        v.hits = statistics.cacheHitCount();
        v.miss = statistics.cacheMissCount();
        v.usefulness = new BigDecimal(v.miss != 0 ? (v.hits * 1D / (v.miss + v.hits)) * 100D : 0D).setScale(2, RoundingMode.HALF_UP);

        CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
        if (cacheConfiguration.getMaxEntriesLocalHeap() != 0 || cacheConfiguration.getMaxEntriesLocalDisk() != 0) {
            int size = cache.getSize();
            long max = cacheConfiguration.getMaxEntriesLocalHeap() + cacheConfiguration.getMaxEntriesLocalDisk();
            v.fullness = new BigDecimal(size * 1D / max * 100D).setScale(2, RoundingMode.HALF_UP);
            v.currentSize = String.format(ENTRIES_SIZE, size, max);
        } else if (cacheConfiguration.getMaxBytesLocalDisk() != 0 || cacheConfiguration.getMaxBytesLocalHeap() != 0) {
            long size = cache.calculateOffHeapSize() + cache.calculateOnDiskSize();
            long max = cacheConfiguration.getMaxBytesLocalDisk() + cacheConfiguration.getMaxBytesLocalHeap();
            v.fullness = new BigDecimal(size * 1D / max * 100D).setScale(2, RoundingMode.HALF_UP);
            v.currentSize = bytes2String(size, max);
        }
        v.fullnessState = v.fullness.doubleValue() < 70 ? 1 : v.fullness.doubleValue() < 90 ? 2 : 3;
        v.usefulnessState = v.usefulness.doubleValue() > 80 ? 1 : v.usefulness.doubleValue() > 50 ? 2 : 3;
        v.hasTimeEvict = cacheConfiguration.getTimeToLiveSeconds() > 0 || cacheConfiguration.getTimeToIdleSeconds() > 0;
        return v;
    }

    public static final class View {
        public String name;
        public String currentSize;
        public BigDecimal fullness;
        public int fullnessState;
        public long hits;
        public long miss;
        public BigDecimal usefulness;
        public int usefulnessState;
        public boolean hasStatistic;
        public boolean hasTimeEvict;
    }

    private static final double SPACE_KB = 1024;
    private static final double SPACE_MB = 1024 * SPACE_KB;
    private static final double SPACE_GB = 1024 * SPACE_MB;

    private static String bytes2String(long size, long max) {

        NumberFormat nf = new DecimalFormat();
        String curSize;
        String maxSize;
        String sizeName;
        nf.setMaximumFractionDigits(2);

        try {
            if ( size < SPACE_KB ) {
                curSize = nf.format(size);
                maxSize = nf.format(max);
                sizeName = "Byte(s)";
            } else if ( size < SPACE_MB ) {
                curSize = nf.format(size / SPACE_KB);
                maxSize = nf.format(max / SPACE_KB);
                sizeName = " KB";
            } else if ( size < SPACE_GB ) {
                curSize = nf.format(size / SPACE_MB);
                maxSize = nf.format(max / SPACE_MB);
                sizeName = " MB";
            } else {
                curSize = nf.format(size / SPACE_GB);
                maxSize = nf.format(max / SPACE_GB);
                sizeName = " GB";
            }
        } catch (Exception e) {
            logger.debug("Error parse size", e);
            curSize = String.valueOf(size);
            maxSize = String.valueOf(max);
            sizeName = " Byte(s)";
        }

        return String.format(BYTES_SIZE, curSize, maxSize, sizeName);
    }

}
