package net.n2oapp.framework.config.ehcache.monitoring.api;

/**
 * @author V. Alexeev.
 */
public class CacheConfig {

    public String name;
    public Long timeToLive;
    public Long timeToIdle;
    public Long maxEntriesLocalHeap;
    public Long maxBytesLocalHeap;
    public Long maxEntriesLocalDisk;
    public Long maxBytesLocalDisk;
    public Double percentOfTotal;
    public Boolean isOverflowToOffHeap;
    public Boolean isFrozen;

    public Long getMaxBytesLocalDisk() {
        return maxBytesLocalDisk;
    }

    public CacheConfig setMaxBytesLocalDisk(Long maxBytesLocalDisk) {
        this.maxBytesLocalDisk = maxBytesLocalDisk;
        return this;
    }

    public String getName() {
        return name;
    }

    public CacheConfig setName(String name) {
        this.name = name;
        return this;
    }

    public Long getTimeToLive() {
        return timeToLive;
    }

    public CacheConfig setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    public Long getTimeToIdle() {
        return timeToIdle;
    }

    public CacheConfig setTimeToIdle(Long timeToIdle) {
        this.timeToIdle = timeToIdle;
        return this;
    }

    public Long getMaxEntriesLocalHeap() {
        return maxEntriesLocalHeap;
    }

    public CacheConfig setMaxEntriesLocalHeap(Long maxEntriesLocalHeap) {
        this.maxEntriesLocalHeap = maxEntriesLocalHeap;
        return this;
    }

    public Long getMaxBytesLocalHeap() {
        return maxBytesLocalHeap;
    }

    public CacheConfig setMaxBytesLocalHeap(Long maxBytesLocalHeap) {
        this.maxBytesLocalHeap = maxBytesLocalHeap;
        return this;
    }

    public Long getMaxEntriesLocalDisk() {
        return maxEntriesLocalDisk;
    }

    public CacheConfig setMaxEntriesLocalDisk(Long maxEntriesLocalDisk) {
        this.maxEntriesLocalDisk = maxEntriesLocalDisk;
        return this;
    }

    public Double getPercentOfTotal() {
        return percentOfTotal;
    }

    public CacheConfig setPercentOfTotal(Double percentOfTotal) {
        this.percentOfTotal = percentOfTotal;
        return this;
    }

    public Boolean getOverflowToOffHeap() {
        return isOverflowToOffHeap;
    }

    public CacheConfig setOverflowToOffHeap(Boolean overflowToOffHeap) {
        isOverflowToOffHeap = overflowToOffHeap;
        return this;
    }

    public Boolean getFrozen() {
        return isFrozen;
    }

    public CacheConfig setFrozen(Boolean frozen) {
        isFrozen = frozen;
        return this;
    }

    public boolean isElementsSettings() {
        return maxEntriesLocalDisk != null || maxEntriesLocalHeap != null;
    }
}
