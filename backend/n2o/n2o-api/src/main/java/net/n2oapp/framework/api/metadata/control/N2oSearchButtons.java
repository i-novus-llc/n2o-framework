package net.n2oapp.framework.api.metadata.control;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент SearchButtons (кнопки фильтра)
 */
@Getter
@Setter
@VisualComponent
public class N2oSearchButtons extends N2oStandardField {
    @VisualAttribute
    private String searchLabel;
    @VisualAttribute
    private String resetLabel;
    @VisualAttribute
    private String clearIgnore;
    private Boolean fetchOnClear;
}
