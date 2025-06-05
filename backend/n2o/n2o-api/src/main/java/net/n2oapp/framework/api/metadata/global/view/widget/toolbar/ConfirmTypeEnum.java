package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

@RequiredArgsConstructor
@Getter
public enum ConfirmTypeEnum implements N2oEnum {
    POPOVER("popover"),
    MODAL("modal");

    private final String id;
}