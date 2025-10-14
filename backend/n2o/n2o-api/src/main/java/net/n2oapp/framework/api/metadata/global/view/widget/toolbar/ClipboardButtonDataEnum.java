package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип данных для кнопки {@code <clipboard-button>}
 */
@RequiredArgsConstructor
@Getter
public enum ClipboardButtonDataEnum implements N2oEnum {
    TEXT("text"),
    HTML("html");

    private final String id;
}