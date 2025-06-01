package net.n2oapp.framework.api.metadata.meta.widget;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Фиксированное или относительное положение сообщения
 */

@RequiredArgsConstructor
@Getter
public enum MessagePositionEnum implements N2oEnum {
    FIXED("fixed"),         // фиксированное
    RELATIVE("relative");   // относительное

    private final String id;
}