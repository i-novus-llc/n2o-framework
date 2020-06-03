package net.n2oapp.framework.api.config;

import org.apache.commons.io.IOUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Конструктор конфигурации клиента N2O приложения
 * @param <T> Тип конфигурации
 */
public interface ConfigBuilder<T extends AppConfig> {

    default ConfigBuilder<T> user(Object user) {
        return add("user", user);
    }

    default ConfigBuilder<T> menu(Object menu) {
        return add("menu", menu);
    }

    default ConfigBuilder<T> messages(ResourceBundle resourceBundle,
                                   MessageSourceAccessor messageSource) {
        return messages(resourceBundle, messageSource, LocaleContextHolder.getLocale());
    }

    default ConfigBuilder<T> messages(ResourceBundle resourceBundle,
                                   MessageSourceAccessor messageSource,
                                   Locale locale) {
        Map<String, String> messages = new LinkedHashMap<>();
        for (String key : resourceBundle.keySet()) {
            messages.put(key, messageSource.getMessage(key, locale));
        }
        return add("messages", messages);
    }

    ConfigBuilder<T> add(String property, Object value);

    ConfigBuilder<T> addAll(Map<String, Object> values);

    default void read(File file) {
        read(new FileSystemResource(file));
    }

    default void read(Resource resource) {
        try {
            read(resource.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    default void read(InputStream content) {
        try {
            read(IOUtils.toString(content, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    void read(String content);

    void write(Writer out);

    void write(File file);

    T get();

}
