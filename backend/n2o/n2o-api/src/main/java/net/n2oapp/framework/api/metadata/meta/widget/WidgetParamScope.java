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
 * Значения по умолчанию виджета, которые берутся из url
 */
@Getter
@Setter
public class WidgetParamScope {
    private String clientWidgetId;
    private Map<String, PageRoutes.Query> queryMapping = new LinkedHashMap<>();

    public WidgetParamScope(String clientWidgetId) {
        this.clientWidgetId = clientWidgetId;
    }

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
            throw new N2oException("Page already contains query mapping {0}!").addData(queryParam);
        }
        queryMapping.put(queryParam, query);
    }
}
