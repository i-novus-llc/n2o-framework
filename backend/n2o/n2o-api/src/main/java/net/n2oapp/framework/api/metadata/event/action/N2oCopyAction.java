package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;

/**
 * Событие копирования модели
 */
@Getter
@Setter
public class N2oCopyAction extends N2oAbstractAction implements N2oAction {
    private ReduxModel sourceModel;
    private String sourceDatasource;
    @Deprecated
    private String sourceWidgetId;
    private String sourceFieldId;

    private ReduxModel targetModel;
    private String targetDatasource;
    @Deprecated
    private String targetWidgetId;
    private String targetFieldId;
    private String targetClientPageId;
    private CopyMode mode;
}
