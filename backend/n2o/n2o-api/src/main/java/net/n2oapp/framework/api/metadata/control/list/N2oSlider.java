package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Компонент Slider (ползунок)
 */
@Getter
@Setter
public class N2oSlider extends N2oSingleListFieldAbstract {
    private ModeEnum mode;
    private Boolean vertical;
    private String measure;
    private Integer min;
    private Integer max;
    private Integer step;

    @RequiredArgsConstructor
    @Getter
    public enum ModeEnum implements N2oEnum {
        SINGLE("single"),
        RANGE("range");

        private final String id;
    }
}
