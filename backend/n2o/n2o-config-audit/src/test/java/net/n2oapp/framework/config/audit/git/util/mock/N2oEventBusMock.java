package net.n2oapp.framework.config.audit.git.util.mock;

import net.n2oapp.framework.api.event.N2oEvent;
import net.n2oapp.framework.api.event.N2oEventBus;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author rgalina
 * @since 18.01.2017
 */
public class N2oEventBusMock extends N2oEventBus {

    public N2oEventBusMock() {
        super(null);
    }

    @Override
    public <E extends N2oEvent> void publish(E e) {
    }
}
