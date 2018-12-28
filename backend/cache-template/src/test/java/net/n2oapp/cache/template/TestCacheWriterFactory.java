package net.n2oapp.cache.template;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.CacheWriterFactory;

import java.util.Properties;

/**
 * User: iryabov
 * Date: 30.03.13
 * Time: 11:37
 */
public class TestCacheWriterFactory extends CacheWriterFactory {
    @Override
    public CacheWriter createCacheWriter(Ehcache cache, Properties properties) {
        return new TestCacheWriter();
    }
}
