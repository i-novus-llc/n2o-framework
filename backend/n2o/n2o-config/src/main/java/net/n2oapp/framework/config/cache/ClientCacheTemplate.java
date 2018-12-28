package net.n2oapp.framework.config.cache;

import net.n2oapp.cache.template.CacheCallback;
import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.cache.template.ThreeLevelCacheCallback;
import net.n2oapp.cache.template.ThreeLevelCacheTemplate;
import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import org.springframework.cache.Cache;

/**
 * Шаблон сервиса кэширования метаданных под клиента
 */
public class ClientCacheTemplate extends CacheTemplate<Object, ClientMetadata> {

    @Override
    protected ClientMetadata handleCache(Object key, CacheCallback<ClientMetadata> callback, Cache cache) {
        synchronized (key.toString().intern()) {
            //еще раз читаем, т.к. в кэш могли положить пока ждали
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                ClientMetadata value = (ClientMetadata)valueWrapper.get();
                callback.doInCacheHit(value);
                return value;
            }
            return super.handleCache(key, callback, cache);
        }
    }

}
