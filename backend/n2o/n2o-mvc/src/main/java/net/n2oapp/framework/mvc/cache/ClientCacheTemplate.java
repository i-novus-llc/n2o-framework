package net.n2oapp.framework.mvc.cache;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Шаблон для кэширования запросов на клиенте
 */
public abstract class ClientCacheTemplate {
    private static final Logger logger = LoggerFactory.getLogger(ClientCacheTemplate.class);
    private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final String LAST_MODIFIED = "Last-Modified";

    /**
     * Выполнить запрос с кэшированием "If-Modified-Since"
     *
     * @param req      Запрос
     * @param resp     Ответ
     * @param callback Функция выполняющая запрос без кэширования
     */
    public void execute(HttpServletRequest req, HttpServletResponse resp, ClientCacheCallback callback)
            throws ServletException, IOException {
        long lastModifiedFromClient = getLastModifiedFromClient(req);
        long lastModifiedFromServer = getLastModifiedFromServer(req);
        if (ifNotModifiedSince(lastModifiedFromClient, lastModifiedFromServer)) {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            if (logger.isDebugEnabled())
                logger.debug("Not Modified {}", req.getRequestURI());
        } else {
            setLastModified(resp, lastModifiedFromServer);
            callback.doGet(req, resp);
        }
    }

    protected void setLastModified(HttpServletResponse resp, long lastModified) {
        resp.addDateHeader(LAST_MODIFIED, lastModified);
    }

    protected boolean ifNotModifiedSince(long lastModifiedFromClient, long lastModifiedFromServer) {
        return lastModifiedFromServer > 0 && lastModifiedFromClient > 0 && lastModifiedFromClient / 1000 >= lastModifiedFromServer / 1000;
    }

    protected long getLastModifiedFromClient(HttpServletRequest req) {
        return req.getDateHeader(IF_MODIFIED_SINCE);
    }

    /**
     * Получить время последнего обновления данных запроса
     *
     * @param req Запрос
     * @return Время в миллесекундах
     */
    protected abstract long getLastModifiedFromServer(HttpServletRequest req);

    /**
     * Функция выполнения запроса без кэширования
     */
    @FunctionalInterface
    public interface ClientCacheCallback {
        /**
         * Выполнить запрос
         *
         * @param req  Запрос
         * @param resp Ответ
         */
        void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    }
}
