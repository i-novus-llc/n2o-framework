package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum FieldLabelLocationEnum implements N2oEnum {
    TOP("top"),
    LEFT("left"),
    RIGHT("right");

    private final String id;
}