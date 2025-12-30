package net.n2oapp.framework.api.metadata.meta.action.validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.ValidateBreakOnEnum;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

import java.util.List;

@Getter
@Setter
public class ValidateActionPayload implements ActionPayload {
    /**
     * Идентификатор источника данных (datasource)
     */
    @JsonProperty
    private String id;

    /**
     * Модель (например: resolve, edit, filter)
     */
    @JsonProperty
    private ReduxModelEnum model;

    /**
     * Порог прерывания по уровню сообщения (например: danger, warning)
     */
    @JsonProperty
    private ValidateBreakOnEnum breakOn;

    /**
     * Список валидируемых полей (поддерживает индексацию и вложенность)
     */
    @JsonProperty
    private List<String> fields;
}