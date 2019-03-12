package net.n2oapp.framework.api.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.NotFoundPlaceholderException;
import net.n2oapp.framework.api.PlaceHoldersResolver;
import net.n2oapp.framework.api.exception.NotFoundContextPlaceholderException;

import java.util.Map;

import static net.n2oapp.framework.api.PlaceHoldersResolver.*;

/**
 * Процессор пользовательского контекста
 */
public class ContextProcessor {

    private final PlaceHoldersResolver contextResolver = new PlaceHoldersResolver("#{", "}");
    private Context context;

    public ContextProcessor(Context context) {
        this.context = context;
    }

    /**
     * Проверить, есть ли в тексте контекст
     *
     * @param text текст
     * @return Содержит - true
     */
    public boolean hasContext(String text) {
        return contextResolver.hasPlaceHolders(text);
    }

    /**
     * Получить текст с разрешенными контекстами
     *
     * @param text текст, содержащий контекст
     * @return значение контекста
     * @throws NotFoundContextPlaceholderException значение отсутствует, но обязательно
     */
    public String resolveText(String text) {
        try {
            return contextResolver.resolve(text, replaceNullByEmpty(replaceOptional(context::get)));
        } catch (NotFoundPlaceholderException e) {
            throw new NotFoundContextPlaceholderException(e.getPlaceholder());
        }
    }

    /**
     * Получить json-валидный текст с разрешенными контекстами
     *
     * @param json текст, содержащий контекст
     * @return значение контекста
     * @throws NotFoundContextPlaceholderException значение отсутствует, но обязательно
     */
    public String resolveJson(String json, ObjectMapper objectMapper) {
        try {
            return contextResolver.resolveJson(json, replaceByJson(replaceOptional(context::get), objectMapper), objectMapper);
        } catch (NotFoundPlaceholderException e) {
            throw new NotFoundContextPlaceholderException(e.getPlaceholder());
        }
    }

    /**
     * Получить значение из контекста
     *
     * @param param выражение вида #{param?defaultValue}
     * @return значение контекста
     * @throws NotFoundContextPlaceholderException значение отсутствует, но обязательно
     */
    public Object resolve(Object param) throws NotFoundContextPlaceholderException {
        try {
            return contextResolver.resolveValue(param, replaceOptional(context::get));
        } catch (NotFoundPlaceholderException e) {
            throw new NotFoundContextPlaceholderException(e.getPlaceholder());
        }
    }

    /**
     * Установить контекст
     *
     * @param dataSet контекст
     */
    public void set(Map<String, Object> dataSet) {
        context.set(dataSet);
    }

    /**
     * Установить контекст
     *
     * @param name  имя
     * @param value значение
     */
    public void set(String name, Object value) {
        context.set(name, value);
    }

    /**
     * Получить значение контекста по имени
     *
     * @param name имя
     * @return значение
     */
    public Object get(String name) {
        return context.get(name);
    }

}
