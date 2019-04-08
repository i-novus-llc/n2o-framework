package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.Page;
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
    public static final String ROOT_ROUTE = "/";
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
        CompileContext<D, ?> result = findRoute(url, compiledClass);
        if (result != null) {
            return result;
        }

        tryToFindDeep(url);

        result = findRoute(url, compiledClass);
        if (result == null)
            throw new RouteNotFoundException(url);
        return result;
    }

    /**
     * Найти контексты сборки метаданных по адресу и классу
     * @param url Адрес
     * @param compiledClass Класс собранной метаданной
     * @param <D> Тип метаданной
     * @return Список найденных контекстов
     */
    @SuppressWarnings("unchecked")
    private <D extends Compiled> CompileContext<D, ?> findRoute(String url, Class<D> compiledClass) {
        for (Map.Entry<RouteInfoKey, CompileContext> routeEntry : register) {
            if (matchInfo(routeEntry.getKey(), url) &&
                    compiledClass.isAssignableFrom(routeEntry.getValue().getCompiledClass())) {
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

    /**
     * Попытка найти маршрут собирая метаданные его родителей
     * @param url Часть маршрута
     */
    private void tryToFindDeep(String url) {
        if (url.length() > 1) {
            String subUrl;
            int idx = url.lastIndexOf(ROOT_ROUTE);
            if (idx > 0)
                subUrl = url.substring(0, idx);
            else
                subUrl = ROOT_ROUTE;

            CompileContext<Page, ?> result = findRoute(subUrl, Page.class);
            if (result == null) {
                tryToFindDeep(subUrl);
            } else {
                pipeline.get(result); //warm up
            }
        }
    }

}
