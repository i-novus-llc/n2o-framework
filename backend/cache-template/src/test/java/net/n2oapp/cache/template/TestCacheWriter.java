package net.n2oapp.cache.template;

import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.writebehind.operations.SingleOperationType;

import java.util.Collection;

/**
 * User: iryabov
 * Date: 30.03.13
 * Time: 11:31
 */
public class TestCacheWriter implements CacheWriter {
    @Override
    public CacheWriter clone(Ehcache cache) {
        System.out.println("writer clone:" + cache);
        return this;
    }

    @Override
    public void init() {
        System.out.println("writer init");
    }

    @Override
    public void dispose() throws CacheException {
        System.out.println("writer dispose");
    }

    @Override
    public void write(Element element) throws CacheException {
        System.out.println("writer write:" + element.getValue());
    }

    @Override
    public void writeAll(Collection<Element> elements) throws CacheException {
        for (Element element : elements) {
            write(element);
        }
    }

    @Override
    public void delete(CacheEntry entry) throws CacheException {
        System.out.println("writer delete:" + entry.getElement().getValue());
    }

    @Override
    public void deleteAll(Collection<CacheEntry> entries) throws CacheException {
        for (CacheEntry cacheEntry : entries) {
            delete(cacheEntry);
        }
    }

    @Override
    public void throwAway(Element element, SingleOperationType operationType, RuntimeException e) {
        System.out.println("writer throw:" + element);
    }
}
