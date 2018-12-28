package net.n2oapp.framework.config.groovy;

import net.n2oapp.framework.api.exception.N2oException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

/**
 * Процессор groovy скриптов
 */
public class GroovyScriptProcessor {
    private static final ScriptEngineManager factory = new ScriptEngineManager();
    private static volatile ScriptEngine scriptEngine;

    /**
     * Собрать объекты, определенного класса, полученные из groovy скрипта
     * @param src Groovy скрипт
     * @param clazz Класс объектов
     * @param <T> Тип объектов
     * @return Список объектов определенного класса
     */
    public static <T> Set<T> collectFromScript(String src, Class<T> clazz) {
        Bindings bindings = getScriptEngine().createBindings();
        Object result = runScript(src, bindings);
        Set<T> collection = new HashSet<>();
        collect(collection, result, clazz);
        for (Object value : bindings.values()) {
            collect(collection, value, clazz);
        }
        return collection;
    }

    /**
     * Получить значение groovy скрипта
     * @param src Grooby скрипт
     * @param clazz Класс значения
     * @param <T> Тип значения
     * @return Значение
     */
    public static <T> T getFromScript(String src, Class<T> clazz) {
        Set<T> result = collectFromScript(src, clazz);
        if (result.isEmpty())
            throw new IllegalStateException(String.format("script does not return any object by class: '%s'", clazz));
        if (result.size() > 1)
            throw new IllegalStateException(String.format("script returned more than one object by class: '%s'", clazz));
        return result.iterator().next();
    }

    private static Object runScript(String script, Bindings bindings) {
        try {
            return getScriptEngine().eval(script, bindings);
        } catch (ScriptException e) {
            throw new N2oException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void collect(Set<T> collection, Object object, Class<T> clazz) {
        if (object == null)
            return;
        if (clazz.isAssignableFrom(object.getClass())) {
            collection.add((T) object);
        } else if (object instanceof Collection) {
            for (Object item : (Collection) object) {
                collect(collection, item, clazz);
            }
        }
    }

    private static ScriptEngine getScriptEngine() {
        if (scriptEngine == null) {
            try {
                createScriptEngine();
            } catch (ScriptException e) {
                throw new IllegalStateException(e);
            }
        }
        return scriptEngine;
    }

    private static synchronized void createScriptEngine() throws ScriptException {
        if (scriptEngine == null) {
            scriptEngine = factory.getEngineByName("Groovy");
            if (scriptEngine == null)
                throw new IllegalStateException("Need groovy-all dependency!");
        }
    }

}
