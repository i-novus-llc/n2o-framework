package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.io.Serializable;

/**
 * Модель предустановленных параметров
 */
@Getter
@Setter
public class N2oParam implements Serializable {
    private String name;
    private String value;
    private String refWidgetId;
    private ReduxModel refModel;
}
