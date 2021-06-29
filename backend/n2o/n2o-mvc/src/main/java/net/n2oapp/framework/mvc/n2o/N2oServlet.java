package net.n2oapp.framework.mvc.n2o;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.meta.saga.AlertSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.register.route.RouteNotFoundException;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Абстракция для сервлетов N2O.
 * Обеспечивает обработку ошибок и получение контекста пользователя.
 */
public abstract class N2oServlet extends HttpServlet {
    protected static final Log logger = LogFactory.getLog(N2oServlet.class);
    public static final String USER = "user";
    protected ObjectMapper objectMapper = new ObjectMapper();
    private AlertMessageBuilder messageBuilder;
    private ClientCacheTemplate clientCacheTemplate;


    @Override
    public void init() throws ServletException {
        super.init();
        if (messageBuilder == null)
            messageBuilder = new AlertMessageBuilder(new MessageSourceAccessor(new ResourceBundleMessageSource()));
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

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void sendError(HttpServletRequest req, HttpServletResponse resp, Exception e) throws IOException {
        int status = e instanceof N2oException ? ((N2oException) e).getHttpStatus() : 500;
        logger.error("Error response " + status + " " + req.getMethod() + " " + req.getRequestURI() + (req.getQueryString() != null ? "?" + req.getQueryString() : ""), e);
        if (resp.isCommitted())
            return;//ответ уже вернулся клиенту
        //render json error
        resp.setContentType("application/json");
        resp.setStatus(status);
        if (!(e instanceof RouteNotFoundException)) {
            MetaSaga meta = buildMeta(e);
            objectMapper.writeValue(resp.getWriter(), Collections.singletonMap("meta", meta));
        }
    }

    private MetaSaga buildMeta(Exception exception) {
        MetaSaga meta = new MetaSaga();
        meta.setAlert(new AlertSaga());
        meta.getAlert().setMessages(Collections.singletonList(messageBuilder.build(exception)));
        return meta;
    }

    public AlertMessageBuilder getMessageBuilder() {
        return messageBuilder;
    }

    public void setMessageBuilder(AlertMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    public void setClientCacheTemplate(ClientCacheTemplate clientCacheTemplate) {
        this.clientCacheTemplate = clientCacheTemplate;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
