package net.n2oapp.framework.api.metadata.meta.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Значения по умолчанию полей виджета, которые берутся из url
 */
@Getter
@Setter
public class WidgetParamScope {
    private Map<String, PageRoutes.Query> queryMapping = new LinkedHashMap<>();

    /**
     * Добавить параметр запроса
     *
     * @param queryParam Параметр в запросе
     * @param onGet      Redux действие при извлечении параметра запроса
     * @param onSet      Ссылка на Redux модель, при вставке параметра запроса
     */
    public void addQueryMapping(String queryParam, ReduxAction onGet, BindLink onSet) {
        PageRoutes.Query query = new PageRoutes.Query();
        query.setOnGet(onGet);
        query.setOnSet(onSet);
        if (queryMapping.containsKey(queryParam) && !queryMapping.get(queryParam).equals(query)) {
            throw new N2oException(String.format("Page already contains query mapping %s!", queryParam));
        }
        queryMapping.put(queryParam, query);
    }
}
