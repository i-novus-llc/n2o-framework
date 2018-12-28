package net.n2oapp.framework.api.register.route;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Результат поиска метаданных по URL.
 */
public class RoutingResult {

    /**
     * Шаблон URL, используемый для получения параметров запроса.
     */
    private String urlPattern;
    /**
     * Контексты сборки разных метаданных присоединенных к одному URL
     */
    private List<CompileContext<?, ?>> contexts;

    /**
     * Параметры запроса
     */
    private DataSet params;

    public RoutingResult(String urlPattern, List<CompileContext<?, ?>> contexts, DataSet params) {
        this.urlPattern = urlPattern;
        this.contexts = contexts;
        this.params = params;
    }

    public <D extends Compiled> CompileContext<D, ?> getContext(Class<D> compiledClass) {
        return (CompileContext<D, ?>) contexts.stream()
                .filter(c -> compiledClass.isAssignableFrom(c.getCompiledClass()))
                .findAny().orElse(null);
    }

    public DataSet getParams() {
        return params;
    }

    public String getUrlPattern() {
        return urlPattern;
    }
}
