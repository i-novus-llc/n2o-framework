package net.n2oapp.framework.api.metadata.meta.action.custom;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

import java.util.Map;

/**
 * Полезная нагрузка кастомного действия
 */
@Getter
@Setter
public class CustomActionPayload implements ActionPayload {
    @JsonValue
    private Map<String, Object> attributes;
}
