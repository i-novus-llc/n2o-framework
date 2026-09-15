package net.n2oapp.framework.mvc.n2o;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.meta.saga.AlertSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.register.route.RouteNotFoundException;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;

/**
 * Абстракция для сервлетов N2O.
 * Обеспечивает обработку ошибок и получение контекста пользователя.
 */
@Slf4j
public abstract class N2oServlet extends HttpServlet {
    public static final String USER = "user";
    protected final ObjectMapper objectMapper;
    private final ClientCacheTemplate clientCacheTemplate;
    private final AlertMessagesConstructor messagesConstructor;

    /**
     * Конструктор для сервлетов без клиентского кэша
     */
    protected N2oServlet(ObjectMapper objectMapper,
                         AlertMessagesConstructor messagesConstructor) {
        this(objectMapper, null, messagesConstructor);
    }

    /**
     * Конструктор для сервлетов с клиентским кэшем
     */
    protected N2oServlet(ObjectMapper objectMapper,
                         ClientCacheTemplate clientCacheTemplate,
                         AlertMessagesConstructor messagesConstructor) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.clientCacheTemplate = clientCacheTemplate;
        this.messagesConstructor = Objects.requireNonNull(messagesConstructor);
    }

    public UserContext getUser(HttpServletRequest req) {
        UserContext user = (UserContext) req.getAttribute(USER);
        if (user == null)
            throw new IllegalStateException("User is not initialized");
        return user;
    }

    private void setUser(HttpServletRequest req) {
        UserContext user = (UserContext) req.getAttribute(USER);
        if (user == null)
            req.setAttribute(USER, StaticUserContext.getUserContext());
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            setUser(req);
            if (clientCacheTemplate != null)
                clientCacheTemplate.execute(req, resp, this::safeDoGet);
            else
                safeDoGet(req, resp);
        } catch (Exception e) {
            sendError(req, resp, e);
        }
    }

    protected void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUser(req);
        try {
            safeDoPost(req, resp);
        } catch (Exception e) {
            sendError(req, resp, e);
        }
    }

    protected void safeDoPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    }

    @Override
    protected final void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUser(req);
        try {
            safeDoPut(req, resp);
        } catch (Exception e) {
            sendError(req, resp, e);
        }
    }

    protected void safeDoPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    }

    @Override
    protected final void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUser(req);
        try {
            safeDoDelete(req, resp);
        } catch (Exception e) {
            sendError(req, resp, e);
        }
    }

    protected void safeDoDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    }

    protected Object getRequestBody(HttpServletRequest request) {
        try {
            if (request.getReader() == null) return new DataSet();
            String body = IOUtils.toString(request.getReader()).trim();
            if (body.startsWith("[")) {
                return objectMapper.<List<DataSet>>readValue(body,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, DataSet.class)
                );
            }
            return objectMapper.readValue(body, DataSet.class);
        } catch (IOException e) {
            throw new N2oException(e);
        }
    }

    protected Map<String, String[]> getHeaders(HttpServletRequest req) {
        Map<String, String[]> result = new HashMap<>();
        Enumeration<String> iter = req.getHeaderNames();
        while (iter.hasMoreElements()) {
            String name = iter.nextElement();
            result.put(name, new String[]{req.getHeader(name)});
        }
        return result;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void sendError(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        int status = e instanceof N2oException n2oException ? n2oException.getHttpStatus() : 500;
        log.error("Error response {} {} {}{}", status, req.getMethod(), req.getRequestURI(),
                req.getQueryString() != null ? "?" + req.getQueryString() : "", e);
        if (resp.isCommitted())
            return;

        resp.setContentType("application/json");
        resp.setStatus(status);
        if (!(e instanceof RouteNotFoundException)) {
            MetaSaga meta = buildMeta(e);
            try {
                objectMapper.writeValue(resp.getWriter(), Collections.singletonMap("meta", meta));
            } catch (IOException ex) {
                log.error("Error writing response", ex);
            }
        }
    }

    private MetaSaga buildMeta(Exception exception) {
        MetaSaga meta = new MetaSaga();
        meta.setAlert(new AlertSaga());
        meta.getAlert().setMessages(messagesConstructor.constructMessages(exception));
        return meta;
    }
}