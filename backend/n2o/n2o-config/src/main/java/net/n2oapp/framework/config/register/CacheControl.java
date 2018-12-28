package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Управление данными кэша
 */
@Deprecated
public class CacheControl {
    private static final Logger logger = LoggerFactory.getLogger(CacheControl.class);
    public static final String SOURCE_CACHE = "n2o.metadata.global";
    public static final String COMPILE_CACHE = "n2o.metadata.local";
    public static final String SESSION_CACHE = "n2o.metadata.session";
    public static final String CLIENT_CACHE = "n2o.metadata.client";
    public static final String METADATA_CRITERIA = "metadata";
    public static final String CONTEXT_CRITERIA = "contextId";
    public static final String SESSION_CRITERIA = "sessionId";

    private final CacheManager cacheManager;


    public CacheControl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * удаление одной метаданной из всех кешей
     * @param id - идентификатор метаданной
     * @param metadataClass - сырой класс метаданной
     */
    public void evict(String id, Class<? extends SourceMetadata> metadataClass) {
        String metadata = Key.createSourceKey(id, metadataClass);
    }

    /**
     * удаление одной метаданной из глобального кеша
     * @param id - идентификатор метаданной
     * @param metadataClass - сырой класс метаданной
     */
    public void evictGlobal(String id, Class<? extends SourceMetadata> metadataClass) {
    }

    /**
     * Удаление одной метаданной из локального кеша
     * @param id Идентификатор метаданной
     * @param sourceClass Класс исходной метаданной
     */
    public void evictLocal(String id, Class<? extends SourceMetadata> sourceClass) {
    }

    /**
     * удаление одной пользовательской метаданной из кеша пользователей
     * @param id - идентификатор метаданной
     * @param metadataClass - сырой класс метаданной
     * @param contextId - идентификатор клиента
     */
    public void evictUser(String id, Class<? extends SourceMetadata> metadataClass, String contextId) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(METADATA_CRITERIA, Key.createSourceKey(id, metadataClass));
        criteria.put(CONTEXT_CRITERIA, contextId);
    }

    /**
     * удаление одной клиентской метаданной из клиентских кешей всех пользователей
     * @param id - идентификатор метаданной
     * @param metadataClass - сырой класс метаданной
     */
    public void evictClientAll(String id, Class<? extends SourceMetadata> metadataClass) {
        String metadata = Key.createSourceKey(id, metadataClass);
    }

    /**
     * удаление всех метаданных из локального кеша
     */
    public void evictAllLocal() {
    }

    /**
     * удаление всех клиентских метаданных из клиентского кеша
     * @param sessionId - сессия пользователя
     */
    public void evictAllClient(String sessionId) {
    }

    /**
     * удаление всех пользовательских метаданных пользователя из пользовательского кеша
     * @param contextId - идентификатор контекста пользователя
     */
    public void evictAllUser(String contextId) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put(CONTEXT_CRITERIA, contextId);
    }

    /**
     * удаление всех метаданных из всех кешей
     */
    public void evictAll() {
    }

    /**
     * Возвращает время последнего изменения метаданной в клиентском кэше
     * @param id    id метаданной
     * @param metadataClass     класс метеданной
     * @param contextId         идентификатор клиента
     * @param sessionId         идентификатор сессии
     * @return      время в миллесекундах от 1 января 1970 года.
     */
    public long getLastModifiedTimeToCurrentClient(String id, Class<? extends SourceMetadata> metadataClass, String contextId, String sessionId) {
        Cache cache = cacheManager.getCache(CacheControl.CLIENT_CACHE);
        if (cache == null) {
            logger.warn("Cannot find cache named [" + CacheControl.CLIENT_CACHE + "] in ehcache.xml");
            return new Date().getTime();
        } else {
            Cache.ValueWrapper valueWrapper = cache.get(Key.createClientKey(id, metadataClass, contextId, sessionId));
            if (valueWrapper != null && valueWrapper.get() != null) {
                return (long) valueWrapper.get();
            } else {
                long time = new Date().getTime();
                cache.put(Key.createClientKey(id, metadataClass, contextId, sessionId), time);
                return time;
            }
        }
    }

    /**
     * Ключ кэша
     */
    public static class Key implements Serializable {
        private String sessionId;
        private String contextId;
        private String metadata;

        public static String createSourceKey(String metadataId, Class<? extends SourceMetadata> metadataClass) {
            return metadataClass.getName() + "$" + metadataId;
        }

        @SuppressWarnings("unchecked")
        public static String createCompileKey(String metadataId, Class<? extends SourceMetadata> sourceClass) {
            return createSourceKey(metadataId, sourceClass);
        }

        public static Key createClientKey(String metadataId, Class<? extends SourceMetadata> metadataClass, String contextId, String sessionId) {
            String metadata = metadataClass.getName() + "$" + metadataId;
            return new Key(sessionId, contextId, metadata);
        }

        public Key(String sessionId, String contextId, String metadata) {
            this.sessionId = sessionId;
            this.contextId = contextId;
            this.metadata = metadata;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getContextId() {
            return contextId;
        }

        public String getMetadata() {
            return metadata;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return Objects.equals(sessionId, key.sessionId) &&
                    Objects.equals(contextId, key.contextId) &&
                    Objects.equals(metadata, key.metadata);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, contextId, metadata);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "sessionId='" + sessionId + '\'' +
                    ", contextId='" + contextId + '\'' +
                    ", metadata='" + metadata + '\'' +
                    '}';
        }
    }
}
