package net.n2oapp.framework.api.config;

import org.apache.commons.io.IOUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Конструктор конфигурации клиента N2O приложения
 * @param <T> Тип конфигурации
 */
public interface ConfigBuilder<T extends AppConfig> {

    /**
     * Установить информацию о пользователе
     * @param user Пользователь
     * @return Конструктор
     */
    default ConfigBuilder<T> user(Object user) {
        return add("user", user);
    }

    /**
     * Установить информацию о меню
     * @param menu Меню
     * @return Конструктор
     */
    default ConfigBuilder<T> menu(Object menu) {
        return add("menu", menu);
    }

    /**
     * Установить сообщения локализации
     * @param resourceBundle Бандл ресурсов
     * @param messageSource Источник сообщений
     * @return Конструктор
     */
    default ConfigBuilder<T> messages(ResourceBundle resourceBundle,
                                   MessageSourceAccessor messageSource) {
        return messages(resourceBundle, messageSource, LocaleContextHolder.getLocale());
    }

    /**
     * Установить сообщения локализации
     * @param resourceBundle Бандл ресурсов
     * @param messageSource Исчтоник сообщений
     * @param locale Локаль
     * @return Конструктор
     */
    default ConfigBuilder<T> messages(ResourceBundle resourceBundle,
                                   MessageSourceAccessor messageSource,
                                   Locale locale) {
        Map<String, String> messages = new LinkedHashMap<>();
        for (String key : resourceBundle.keySet()) {
            messages.put(key, messageSource.getMessage(key, locale));
        }
        return add("messages", messages);
    }

    /**
     * Добавить свойство
     * @param property Имя свойтсва
     * @param value Значение свойтсва
     * @return Конструктор
     */
    ConfigBuilder<T> add(String property, Object value);

    /**
     * Добавить свойства
     * @param values Свойства
     * @return Конструктор
     */
    ConfigBuilder<T> addAll(Map<String, Object> values);

    /**
     * Прочитать конфигурацию из файла поверх существующей
     * @param file Файл
     * @return Конструктор
     */
    default ConfigBuilder<T> read(File file) {
        return read(new FileSystemResource(file));
    }

    /**
     * Прочитать конфигурацию из ресурса поверх существующей
     * @param resource Ресурс
     * @return Конструктор
     */
    default ConfigBuilder<T> read(Resource resource) {
        try {
            return read(resource.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Прочитать конфигурацию из входящего потока поверх существующей
     * @param content Входящий поток содержащий json
     * @return Конструктор
     */
    default ConfigBuilder<T> read(InputStream content) {
        try {
            return read(IOUtils.toString(content, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Прочитать конфигурацию из строки поверх существующей
     * @param content Строка содержащая json
     * @return Конструктор
     */
    ConfigBuilder<T> read(String content);

    /**
     * Записать конфигурацию
     * @param out Писатель
     */
    void write(Writer out);

    /**
     * Записать конфигурацию в файл
     * @param file Файл
     */
    void write(File file);

    /**
     * Получить конфигурацию
     * @return Конфигурация
     */
    T get();

}
