package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<CompileContext<?, ?>> infos = findRoutes(url);
        if (infos.isEmpty())
            tryToFindDeep(url);
        infos = findRoutes(url);
        if (infos.isEmpty())
            throw new RouteNotFoundException(url);
        return getContext(infos, compiledClass);
    }

    private List<CompileContext<?, ?>> findRoutes(String url) {
        List<CompileContext<?, ?>> infos = null;
        for (Map.Entry<RouteInfoKey, CompileContext> routeEntry : register) {
            if (matchInfo(routeEntry.getKey(), url)) {
                if (infos == null)
                    infos = new ArrayList<>();
                infos.add(routeEntry.getValue());
            }
        }
        return infos == null ? Collections.emptyList() : infos;
    }

    /**
     * Сопоставляет URL в RouteInfo с url в параметре
     *
     * @param info        Информация об URL адресе
     * @param urlMatching URL шаблон в Ant стиле
     * @param <D>         Тип собранной метаданной
     * @return Сопоставимы или нет
     */
    private <D extends Compiled> boolean matchInfo(RouteInfoKey info, String urlMatching) {
        return pathMatcher.match(info.getUrlMatching(), urlMatching);
    }

    private void tryToFindDeep(String url) {
        if (url.length() > 1) {
            String subUrl;
            List<CompileContext<?, ?>> subInfo;
            int idx = url.lastIndexOf("/");
            if (idx > 0)
                subUrl = url.substring(0, idx);
            else
                subUrl = "/";
            subInfo = findRoutes(subUrl);
            if (subInfo.isEmpty()) {
                tryToFindDeep(subUrl);
                subInfo = findRoutes(subUrl);
            }
            subInfo.forEach(i -> pipeline.get(i));//warm up
        }
    }

    private <D extends Compiled> CompileContext<D, ?> getContext(List<CompileContext<?, ?>> contexts, Class<D> compiledClass) {
        return (CompileContext<D, ?>) contexts.stream()
                .filter(c -> compiledClass.isAssignableFrom(c.getCompiledClass()))
                .findAny().orElse(null);
    }

}
