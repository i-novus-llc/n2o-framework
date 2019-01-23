package net.n2oapp.framework.config.register.route;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.register.route.*;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public RoutingResult get(String url) {
        url = url != null ? url : "/";
        List<RouteInfoValue> infos = findRoutes(url);
        if (infos.isEmpty())
            tryToFindDeep(url);
        infos = findRoutes(url);
        if (infos.isEmpty())
            throw new RouteNotFoundException(url);
        String urlPattern = infos.get(0).getUrlPattern();
        return new RoutingResult(urlPattern, getContexts(infos), getResultData(url, urlPattern));
    }

    private List<CompileContext<?, ?>> getContexts(List<RouteInfoValue> infos) {
        return infos.stream().map(RouteInfoValue::getContext).collect(Collectors.toList());
    }

    private List<RouteInfoValue> findRoutes(String url) {
        List<RouteInfoValue> infos = null;
        for (Map.Entry<RouteInfoKey, RouteInfoValue> routeEntry : register) {
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
            List<RouteInfoValue> subInfo;
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
            subInfo.forEach(i -> pipeline.get(i.getContext()));//warm up
        }
    }

    private DataSet getResultData(String url, String urlPattern) {
        DataSet data = new DataSet();
        String[] splitUrl = url.split("/");
        String[] splitPattern = urlPattern.split("/");
        for (int i = 0; i < splitUrl.length && i < splitPattern.length; i++) {
            if (splitPattern[i].startsWith(":")) {
                data.put(splitPattern[i].substring(1), splitUrl[i]);
            }
        }
        return data;
    }

}
