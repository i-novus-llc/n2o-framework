package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

/**
 * Информация для получения метаданной по url
 */
public class RouteInfoValue {
    /**
     * Шаблон URL, используемый для получения параметров запроса.
     */
    private String urlPattern;

    /**
     * Контекст сборки метаданной.
     */
    private CompileContext<? extends Compiled, ?> context;

    public RouteInfoValue(String urlPattern, CompileContext<? extends Compiled, ?> context) {
        this.urlPattern = urlPattern;
        this.context = context;
    }

    public CompileContext<? extends Compiled, ?> getContext() {
        return context;
    }

    public String getUrlPattern() {
        return urlPattern;
    }
}
