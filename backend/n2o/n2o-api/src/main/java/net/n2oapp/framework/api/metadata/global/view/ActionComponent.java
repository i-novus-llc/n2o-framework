package net.n2oapp.framework.api.metadata.global.view;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.io.Serializable;

/**
 * Абстрактная модель компонента вызывающего действие
 */
@Getter
@Setter
public abstract class ActionComponent implements Serializable, IdAware, ModelAware, WidgetIdAware {
    private String id;
    private String label;
    private String icon;
    private String visible;
    private String enabled;
    private N2oAction action;
    private ReduxModel model;
    private String widgetId;
}
