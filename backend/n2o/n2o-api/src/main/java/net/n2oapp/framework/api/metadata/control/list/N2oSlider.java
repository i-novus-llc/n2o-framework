package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент Slider (ползунок)
 */
@Getter
@Setter
public class N2oSlider extends N2oSingleListFieldAbstract {
    private Mode mode;
    private Boolean vertical;
    private String measure;
    private Integer min;
    private Integer max;
    private Integer step;

    public enum Mode {
        single, range
    }
}
