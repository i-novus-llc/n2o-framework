package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель STOMP-события
 */
@Getter
@Setter
public class StompEvent extends Event {

    private String destination;
}
