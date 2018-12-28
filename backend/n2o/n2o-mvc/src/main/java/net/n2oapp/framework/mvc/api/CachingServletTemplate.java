package net.n2oapp.framework.mvc.api;

import net.n2oapp.cache.template.CacheCallback;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.framework.mvc.callback.ServletCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.function.BiFunction;

/**
 * @author iryabov
 * @since 15.12.2015
 */
public class CachingServletTemplate implements ServletTemplate {
    private static final Logger log = LoggerFactory.getLogger(CachingServletTemplate.class);

    private CacheTemplate cacheTemplate;
    private String cacheRegion;
    private BiFunction<HttpServletRequest, HttpServletResponse, Object> keyFunc;

    public CachingServletTemplate(CacheTemplate cacheTemplate, String cacheRegion) {
    }

    public CachingServletTemplate(CacheManager cacheManager, String cacheRegion) {
        setCacheManager(cacheManager);
        this.cacheRegion = cacheRegion;
    }

    public CachingServletTemplate(CacheManager cacheManager, String cacheRegion, BiFunction<HttpServletRequest, HttpServletResponse, Object> keyFunc) {
        this(cacheManager, cacheRegion);
        this.keyFunc = keyFunc;
    }

    public CachingServletTemplate(CacheTemplate cacheTemplate, String cacheRegion, BiFunction<HttpServletRequest, HttpServletResponse, Object> keyFunc) {
        this.cacheRegion = cacheRegion;
        this.cacheTemplate = cacheTemplate;
        this.keyFunc = keyFunc;
    }

    @Override
    public void doService(HttpServletRequest req, HttpServletResponse res, ServletCallback callback) throws IOException {
        res.setContentType(callback.getContentType());
        Writer cacheWriter = new CachingPrintWriter(res.getWriter());
        BufferedHttpResponse buffer = new BufferedHttpResponse(res, cacheWriter);
        try {
            cacheTemplate.execute(cacheRegion, keyFunc.apply(req, res), new CacheCallback<String>() {
                @Override
                public String doInCacheMiss() {
                    try {
                        callback.onService(req, buffer);
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new N2oException(e);
                    }
                    return ((CachingPrintWriter) buffer.getBuffer()).getCache();
                }

                @Override
                public void doInCacheHit(String s) {
                    try (Writer writer = res.getWriter()) {
                        writer.write(s);
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new N2oException(e);
                    }
                }
            });
        } catch (N2oException e) {
            if ((e.getMessage() != null) || (e.getCause() != null)) {
                log.error(e.getMessage(), e.getCause());
            }
            callback.onError(e, req, res);
        } catch (Exception | Error  e) {
            log.error(e.getMessage(), e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            callback.onError(new N2oException(e), req, res);
        } finally {
            buffer.getWriter().close();
            res.getWriter().close();
        }
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheTemplate = new CacheTemplate(cacheManager);
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    public void setKeyFunc(BiFunction<HttpServletRequest, HttpServletResponse, Object> keyFunc) {
        this.keyFunc = keyFunc;
    }
}
