package net.n2oapp.framework.api.metadata.global.view;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * "Исходная" модель действия для ссылки в action-id (метадействие)
 */
@Getter
@Setter
public class ActionBar implements Source, IdAware {
    private String id;
    private N2oAction[] n2oActions;
}
