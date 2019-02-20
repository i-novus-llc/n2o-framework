package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;


/**
 * Исходная модель действия "клик по строке таблицы"
 */
@Getter
@Setter
public class N2oRowClick implements Source {
    private String actionId;

    private N2oAction action;
}
