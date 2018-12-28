package net.n2oapp.framework.config.ehcache.memory;

/**
 * @author iryabov
 * @since 20.12.2016
 */
public class MemoryModel {
    private Long usedMemory;
    private Long freeMemory;
    private Long totalMemory;
    private Short usedMemoryPercent;

    public Long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Short getUsedMemoryPercent() {
        return usedMemoryPercent;
    }

    public void setUsedMemoryPercent(Short usedMemoryPercent) {
        this.usedMemoryPercent = usedMemoryPercent;
    }

    public Long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(Long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public Long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
    }
}
