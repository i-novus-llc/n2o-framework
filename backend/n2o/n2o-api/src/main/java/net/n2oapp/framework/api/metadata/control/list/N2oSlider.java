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
    private boolean vertical;
    private String measure;
    private int min;
    private int max;
    private int step;

    public enum Mode {
        single, range
    }
}
