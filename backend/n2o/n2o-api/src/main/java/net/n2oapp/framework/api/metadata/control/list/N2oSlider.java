package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент Slider (ползунок)
 */
@Getter
@Setter
@VisualComponent
public class N2oSlider extends N2oSingleListFieldAbstract {
    @VisualAttribute
    private Mode mode;
    @VisualAttribute
    private Boolean vertical;
    @VisualAttribute
    private String measure;
    @VisualAttribute
    private Integer min;
    @VisualAttribute
    private Integer max;
    @VisualAttribute
    private Integer step;

    public enum Mode {
        single, range
    }
}
