package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.io.Serializable;

/**
 * Модель предустановленных параметров
 */
@Getter
@Setter
@NoArgsConstructor
public class N2oParam implements Serializable {
    /**
     * Имя параметра
     */
    private String name;

    /**
     * Значение параметра
     */
    private String value;
    /**
     * Список значений параметра
     */
    private Object valueList;

    /**
     * Идентификатор виджета на странице, на который ссылается параметр
     */
    private String refWidgetId;

    /**
     * Модель виджета, на который ссылается параметр
     */
    private ReduxModel refModel;

    /**
     * Идентификатор страницы, на которую ссылается параметр
     */
    private String refPageId;

    public N2oParam(String name, String value, String refWidgetId, ReduxModel refModel, String refPageId) {
        this.name = name;
        this.value = value;
        this.refWidgetId = refWidgetId;
        this.refModel = refModel;
        this.refPageId = refPageId;
    }
}
