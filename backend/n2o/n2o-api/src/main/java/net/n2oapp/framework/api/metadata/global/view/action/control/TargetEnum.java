package net.n2oapp.framework.api.metadata.global.view.action.control;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип сценария открытия ссылки
 */
@RequiredArgsConstructor
@Getter
@JsonSerialize(using = TargetEnumSerializer.class)
@JsonDeserialize(using = TargetEnumDeserializer.class)
public enum TargetEnum implements N2oEnum {
    SELF("self", "_self"),
    NEW_WINDOW("newWindow", "_blank"),
    APPLICATION("application", "application");

    private final String id;
    private final String value;
}