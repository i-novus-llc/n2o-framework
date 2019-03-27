package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * Поиск по URL подходящего контекста для компиляции метаданных.
 */
public class N2oRouter implements MetadataRouter {
    private RouteRegister register;
    private ReadCompileTerminalPipeline<?> pipeline;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    public N2oRouter(RouteRegister register,
                     ReadCompileTerminalPipeline<?> pipeline) {
        this.register = register;
        this.pipeline = pipeline;
    }

    /**
     * Поиск подходящего контекста по url и вычисление параметров запроса.
     *
     * @param url url для которого необходимо найти контекст
     * @return результат поиска
     */
    public <D extends Compiled> CompileContext<D, ?> get(String url, Class<D> compiledClass) {
        url = url != null ? url : "/";
        CompileContext<D, ?> infos = findRoutes(url, compiledClass);
        if (infos != null) {
            return infos;
        }

        tryToFindDeep(url, compiledClass);

        infos = findRoutes(url, compiledClass);
        if (infos == null)
            throw new RouteNotFoundException(url);
        return infos;
    }

    @SuppressWarnings("unchecked")
    private <D extends Compiled> CompileContext<D, ?> findRoutes(String url, Class<D> compiledClass) {
        for (Map.Entry<RouteInfoKey, CompileContext> routeEntry : register) {
            if (matchInfo(routeEntry.getKey(), url) && compiledClass.isAssignableFrom(routeEntry.getValue().getCompiledClass())) {
                return routeEntry.getValue();
            }
        }
        return null;
    }

    /**
     * Сопоставляет URL в RouteInfo с url в параметре
     *
     * @param info        Информация об URL адресе
     * @param urlMatching URL шаблон в Ant стиле
     * @return Сопоставимы или нет
     */
    private boolean matchInfo(RouteInfoKey info, String urlMatching) {
        return pathMatcher.match(info.getUrlMatching(), urlMatching);
    }

    private <D extends Compiled> void tryToFindDeep(String url, Class<D> compiledClass) {
        if (url.length() > 1) {
            String subUrl;
            int idx = url.lastIndexOf("/");
            if (idx > 0)
                subUrl = url.substring(0, idx);
            else
                subUrl = "/";

            CompileContext<D, ?> subInfo = findRoutes(subUrl, compiledClass);
            if (subInfo == null) {
                tryToFindDeep(subUrl, compiledClass);
                subInfo = findRoutes(subUrl, compiledClass);
            }
            if (subInfo != null) {
                pipeline.get(subInfo); //warm up
            }
        }
    }

}
