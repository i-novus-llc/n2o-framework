package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель STOMP-события
 */
@Getter
@Setter
public class StompEvent extends Event {

    @JsonProperty
    private String destination;
}
