package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

/**
 * Контекст сборки метаданных
 */
public interface CompileContext<D extends Compiled, S> {
    /**
     * Получить идентификатор контекста
     *
     * @return Идентификатор контекста
     */
    String getCompiledId(BindProcessor p);

    /**
     * Получить идентификатор исходной метаданной
     *
     * @param p Процессор сборки
     * @return Идентификатор исходной метаданной
     */
    String getSourceId(BindProcessor p);

    /**
     * Маршрут c параметрами, по которому можно получить метаданную
     *
     * @param p Процессор сборки
     */
    String getRoute(BindProcessor p);

    /**
     * Получить список описаний, как можно разрешить query параметры маршрута
     *
     * @return  список описаний параметров
     */
    Map<String, ModelLink> getQueryRouteMapping();

    /**
     * Получить список описаний, как можно разрешить path параметры маршрута
     *
     * @return  список описаний параметров
     */
    Map<String, ModelLink> getPathRouteMapping();

    /**
     * Получить класс исходной метаданной
     *
     * @return Класс исходной метаданной
     */
    Class<S> getSourceClass();

    /**
     * Получить класс собранной метаданной
     *
     * @return Класс собранной метаданной
     */
    Class<D> getCompiledClass();


    /**
     * Получение данных из url, учитывая route в контексте и query параметры
     * @param url           реальный url со значениями
     * @param queryParams   query параметры
     * @return              данные из url
     */
    DataSet getParams(String url, Map<String, String[]> queryParams);
}
