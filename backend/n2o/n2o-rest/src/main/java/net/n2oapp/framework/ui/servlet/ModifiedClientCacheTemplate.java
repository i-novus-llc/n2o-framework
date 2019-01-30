package net.n2oapp.framework.ui.servlet;

import net.n2oapp.framework.api.event.MetadataChangedEvent;
import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Шаблон кэширования запросов с хранением времени изменения метаданных в отдельном кэше.
 */
public class ModifiedClientCacheTemplate extends ClientCacheTemplate implements N2oEventListener<MetadataChangedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ModifiedClientCacheTemplate.class);
    private CacheManager cacheManager;
    private String cacheRegion = "n2o.client";

    public ModifiedClientCacheTemplate(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    protected long getLastModifiedFromServer(HttpServletRequest req) {
        Long lastModified = getCache().get(req.getRequestURI(), Long.class);
        return lastModified != null ? lastModified : -1;
    }

    @Override
    protected void setLastModified(HttpServletRequest req, HttpServletResponse resp, long lastModified) {
        lastModified = lastModified > 0 ? lastModified : new Date().getTime();
        super.setLastModified(req, resp, lastModified);
        getCache().put(req.getRequestURI(), lastModified);
    }

    protected Cache getCache() {
        Cache cache = cacheManager.getCache(cacheRegion);
        if (cache == null) {
            logger.warn("Cannot find cache named [" + cacheRegion + "] for CacheTemplate");
            return new NoOpCache(cacheRegion);
        }
        return cache;
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    @Override
    public void handleEvent(MetadataChangedEvent event) {
        getCache().clear();
    }
}
