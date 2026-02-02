package net.n2oapp.framework.ui.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class AppConfigServlet extends HttpServlet {
    private final AppConfigJsonWriter appConfigJsonWriter;
    private final ExposedResourceBundleMessageSource messageSource;
    private final ReadCompileBindTerminalPipeline pipeline;
    private final MetadataEnvironment environment;
    private final String applicationSourceId;
    private final ObjectMapper objectMapper;

    public AppConfigServlet(AppConfigJsonWriter appConfigJsonWriter,
                            ExposedResourceBundleMessageSource messageSource,
                            ReadCompileBindTerminalPipeline pipeline,
                            MetadataEnvironment environment,
                            String applicationSourceId,
                            ObjectMapper objectMapper) {
        this.appConfigJsonWriter = appConfigJsonWriter;
        this.messageSource = messageSource;
        this.pipeline = pipeline;
        this.environment = environment;
        this.applicationSourceId = applicationSourceId;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter out = res.getWriter()) {
            Map<String, Object> addedValues = new HashMap<>();
            addedValues.put("menu", getMenu());
            addedValues.put("messages", getMessages());
            appConfigJsonWriter.writeValues(out, addedValues);
        } catch (Exception e) {
            log.error("Error generating application config", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
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

}
