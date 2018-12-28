package net.n2oapp.framework.mvc.cache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Шаблон кэширования запросов по стратегии "Время жизни" (по умолчанию 1 минута).
 */
public class LifetimeClientCacheTemplate extends ClientCacheTemplate {
    private long lifetime = 1000 * 60;

    public LifetimeClientCacheTemplate() {
    }

    public LifetimeClientCacheTemplate(long lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    protected long getLastModifiedFromServer(HttpServletRequest req) {
        return new Date().getTime() - lifetime;
    }

    @Override
    protected void setLastModified(HttpServletRequest req, HttpServletResponse resp, long lastModified) {
        super.setLastModified(req, resp, new Date().getTime());
    }
}
