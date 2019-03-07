package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

import java.util.List;

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

    public RoutingResult(List<CompileContext<?, ?>> contexts) {
        this.contexts = contexts;
    }

    public <D extends Compiled> CompileContext<D, ?> getContext(Class<D> compiledClass) {
        return (CompileContext<D, ?>) contexts.stream()
                .filter(c -> compiledClass.isAssignableFrom(c.getCompiledClass()))
                .findAny().orElse(null);
    }

    public String getUrlPattern() {
        return urlPattern;
    }
}
