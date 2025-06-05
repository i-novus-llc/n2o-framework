package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип доступности кнопки при пустой модели
 */
@RequiredArgsConstructor
@Getter
public enum DisableOnEmptyModelTypeEnum implements N2oEnum {
    AUTO("auto"),
    TRUE("true"),
    FALSE("false");

    private final String id;
}