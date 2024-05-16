package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Ответ на запрос проверки данных
 */
@Getter
@Setter
public class ValidationDataResponse extends N2oResponse {

    /**
     * Поле, под которым будет показан текст с ошибкой
     */
    @JsonProperty
    private String field;
    /**
     * Идентификатор валидации
     */
    @JsonProperty
    private String id;
    /**
     * Уровень важности валидации
     */
    @JsonProperty
    private String severity;
    /**
     * Текст ошибки
     */
    @JsonProperty
    private String text;
}
