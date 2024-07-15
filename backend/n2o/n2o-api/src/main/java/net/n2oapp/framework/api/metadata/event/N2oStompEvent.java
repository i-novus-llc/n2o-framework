package net.n2oapp.framework.api.metadata.event;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.N2oAction;

/**
 * Исходная модель события, приходящего через STOMP протокол
 */
@Getter
@Setter
public class N2oStompEvent extends N2oAbstractEvent {
    private String destination;
    private N2oAction action;
}
