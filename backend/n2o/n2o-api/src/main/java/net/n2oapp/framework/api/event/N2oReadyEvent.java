package net.n2oapp.framework.api.event;

/**
 * Событие готовности N2O к использованию
 */
public class N2oReadyEvent extends N2oEvent {
    public N2oReadyEvent(Object source) {
        super(source);
    }
}
