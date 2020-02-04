package net.n2oapp.framework.api.metadata.global.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.io.Serializable;

/**
 * Модель предустановленных параметров
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
