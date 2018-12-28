package net.n2oapp.framework.api.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Отправщик событий N2O
 */
public class N2oEventBus {
    private Logger logger = LoggerFactory.getLogger(N2oEventBus.class);
    private ApplicationEventPublisher publisher;

    public N2oEventBus(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Отправить событие
     * @param e Событие
     * @param <E> Тип события
     */
    @SuppressWarnings("unchecked")
    public <E extends N2oEvent> void publish(final E e) {
        long i = 0;
        if (logger.isDebugEnabled()) {
            i = System.currentTimeMillis();
        }
        publisher.publishEvent(e);
        if (logger.isDebugEnabled()) {
            logger.debug("publishing event '{}' took {} ms", e.getClass().getSimpleName(), System.currentTimeMillis() - i);
        }
    }
}
