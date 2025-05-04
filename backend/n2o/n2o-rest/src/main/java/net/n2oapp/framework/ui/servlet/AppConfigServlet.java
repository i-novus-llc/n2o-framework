package net.n2oapp.framework.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AppConfigServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AppConfigServlet.class);
    private AppConfigJsonWriter appConfigJsonWriter;
    private ExposedResourceBundleMessageSource messageSource;
    private ReadCompileBindTerminalPipeline pipeline;
    private MetadataEnvironment environment;
    private String applicationSourceId;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        // no implementation
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse res) throws IOException {
        Map<String, Object> addedValues = new HashMap<>();
        addedValues.put("menu", getMenu());
        addedValues.put("messages", getMessages());

        res.setContentType("application/json");
        res.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = res.getWriter();
        try {
            appConfigJsonWriter.writeValues(out, addedValues);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace(out);
            log.error(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    private Map<String, Object> getMenu() {
        return objectMapper.convertValue(getApplication(), Map.class);
    }

    private Application getApplication() {
        if (applicationSourceId != null && !applicationSourceId.isEmpty())
            return pipeline.get(new ApplicationContext(applicationSourceId), null);
        List<SourceInfo> apps = environment.getMetadataRegister().find(N2oApplication.class);
        if (apps == null || apps.isEmpty()) {
            return pipeline.get(new ApplicationContext("default"), null);
        }
        return pipeline.get(new ApplicationContext(apps.get(0).getId()), null);
    }

    private Map<String, String> getMessages() {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource);
        Locale locale = LocaleContextHolder.getLocale();
        Set<String> messagesBaseNames = messageSource.getBasenameSet();
        Map<String, String> map = new TreeMap<>();
        for (String baseName : messagesBaseNames) {
            Set<String> keys = messageSource.getKeys(baseName, locale);
            for (String key : keys) {
                map.put(key, accessor.getMessage(key, locale));
            }
        }
        return map;
    }

    public void setAppConfigJsonWriter(AppConfigJsonWriter appConfigJsonWriter) {
        this.appConfigJsonWriter = appConfigJsonWriter;
    }

    public AppConfigJsonWriter getAppConfigJsonWriter() {
        return appConfigJsonWriter;
    }

    public void setMessageSource(ExposedResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setPipeline(ReadCompileBindTerminalPipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void setApplicationSourceId(String applicationSourceId) {
        this.applicationSourceId = applicationSourceId;
    }

    public void setEnvironment(MetadataEnvironment environment) {
        this.environment = environment;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
