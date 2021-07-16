package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * Поиск по URL подходящего контекста для компиляции метаданных.
 */
public class N2oRouter implements MetadataRouter {
    public static final String ROOT_ROUTE = "/";
    private MetadataEnvironment environment;
    private ReadCompileTerminalPipeline<?> pipeline;
    private final PathMatcher pathMatcher = new AntPathMatcher();


    public N2oRouter(MetadataEnvironment environment,
                     ReadCompileTerminalPipeline<?> pipeline) {
        this.environment = environment;
        this.pipeline = pipeline;
    }

    /**
     * Поиск подходящего контекста по url и вычисление параметров запроса.
     *
     * @param url url для которого необходимо найти контекст
     * @return результат поиска
     */
    public <D extends Compiled> CompileContext<D, ?> get(String url, Class<D> compiledClass, Map<String, String[]> params) {
        url = url != null ? url : ROOT_ROUTE;
        CompileContext<D, ?> result = findRoute(url, compiledClass);
        if (result != null)
            return result;

        if (environment.getRouteRegister().synchronize()) {
            result = findRoute(url, compiledClass);
            if (result != null) return result;
        }

        tryToFindShallow(url, compiledClass, params);
        result = findRoute(url, compiledClass);
        if (result != null)
            return result;

        tryToFindDeep(url, params);
        result = findRoute(url, compiledClass);
        if (result == null) {
            tryToFindShallow(url, compiledClass, params);
            result = findRoute(url, compiledClass);
        }
        if (result != null)
            return result;

        throw new RouteNotFoundException(url);
    }

    /**
     * Найти контексты сборки метаданных по адресу и классу
     *
     * @param url           Адрес
     * @param compiledClass Класс собранной метаданной
     * @param <D>           Тип метаданной
     * @return Список найденных контекстов
     */
    @SuppressWarnings("unchecked")
    private <D extends Compiled> CompileContext<D, ?> findRoute(String url, Class<D> compiledClass) {
        for (Map.Entry<RouteInfoKey, CompileContext> routeEntry : environment.getRouteRegister()) {
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
     * Попытка найти маршрут собирая страницу по этому маршруту
     *
     * @param url Часть маршрута
     */
    private <D extends Compiled> void tryToFindShallow(String url, Class<D> compiledClass, Map<String, String[]> params) {
        if (Page.class == compiledClass)
            return;
        CompileContext<Page, ?> result = findRoute(url, Page.class);
        if (result != null) {
            pipeline.get(result, new N2oCompileProcessor(environment, result, result.getParams(url, params))); //warm up
        }
    }


    /**
     * Попытка найти маршрут собирая метаданные его родителей
     *
     * @param url Часть маршрута
     */
    private void tryToFindDeep(String url, Map<String, String[]> params) {
        if (url.length() > 1) {
            String subUrl;
            int idx = url.lastIndexOf(ROOT_ROUTE);
            if (idx > 0)
                subUrl = url.substring(0, idx);
            else
                subUrl = ROOT_ROUTE;

            CompileContext<Page, ?> result = findRoute(subUrl, Page.class);
            if (result == null) {
                tryToFindDeep(subUrl, params);
                result = findRoute(subUrl, Page.class);
            }
            if (result != null) {
                pipeline.get(result, new N2oCompileProcessor(environment, result, result.getParams(url, params))); //warm up
            }
        } else {
            warmUpRootRoutes();
        }
    }

    /**
     * Прогрев сборки приложения и регистрация корневых маршрутов
     */
    private void warmUpRootRoutes() {
        String applicationId = environment.getSystemProperties().getProperty("n2o.application.id", String.class);
        String welcomePageId = environment.getSystemProperties().getProperty("n2o.homepage.id", String.class);
        // необходимо чтобы зарегистрировать рутовые страницы в RouteRegister
        if (applicationId != null && !applicationId.isEmpty()) {
            pipeline.get(new ApplicationContext(applicationId));
        } else if (welcomePageId != null && !welcomePageId.isEmpty()) {
            pipeline.get(new PageContext(welcomePageId, "/"));
        }
    }

}
