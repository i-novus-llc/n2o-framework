package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

/**
 * Абстрактная модель пункта меню.
 */
@Getter
@Setter
public abstract class N2oAbstractButton extends N2oComponent implements IdAware {
    private String id;
    private String label;
    private String icon;
    private LabelType type;
    private String color;
    private String description;
    private String tooltipPosition;
    private String visible;
}
