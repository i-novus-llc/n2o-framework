package net.n2oapp.framework.config.ehcache.memory;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import net.n2oapp.criteria.api.Criteria;

import java.util.Collections;

/**
 * @author iryabov
 * @since 20.12.2016
 */
public class N2oMemoryService implements CollectionPageService<Criteria, MemoryModel> {

    @Override
    public CollectionPage<MemoryModel> getCollectionPage(Criteria criteria) {
        MemoryModel dto = new MemoryModel();
        Runtime runtime = Runtime.getRuntime();
        dto.setFreeMemory(runtime.freeMemory());
        dto.setTotalMemory(runtime.totalMemory());
        dto.setUsedMemory(runtime.totalMemory() - runtime.freeMemory());
        dto.setUsedMemoryPercent((short) ((runtime.totalMemory() - runtime.freeMemory()) * 100 / runtime.totalMemory()));
        return new CollectionPage<>(1, Collections.singletonList(dto), criteria);
    }
}
