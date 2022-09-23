package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Модель предустановленных параметров
 */
@Getter
@Setter
@NoArgsConstructor
public class N2oParam implements Source {
    /**
     * Имя параметра
     */
    private String name;

    /**
     * Значение параметра
     */
    private String value;

    /**
     * Параметр url для получения значения
     */
    private String valueParam;

    /**
     * Список значений параметра
     */
    private Object valueList;

    /**
     * Идентификатор источника данных на странице, на который ссылается параметр
     */
    private String datasourceId;

    /**
     * Модель виджета, на который ссылается параметр
     */
    private ReduxModel model;

    /**
     * Идентификатор страницы, на которую ссылается параметр
     */
    private String refPageId;


    public N2oParam(N2oParam param) {
        this.name = param.name;
        this.value = param.value;
        this.model = param.model;
        this.refPageId = param.refPageId;
        this.datasourceId = param.datasourceId;
    }

    @Deprecated
    public String getRefWidgetId() {
        return datasourceId;
    }

    @Deprecated
    public void setRefWidgetId(String refWidgetId) {
        this.datasourceId = refWidgetId;
    }
}
