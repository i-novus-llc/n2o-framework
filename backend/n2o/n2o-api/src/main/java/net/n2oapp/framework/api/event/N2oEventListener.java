package net.n2oapp.framework.api.event;

import org.springframework.context.ApplicationListener;

/**
 * Слушатель событий с определением применимости события к слушателю
 */
@FunctionalInterface
public interface N2oEventListener<E extends N2oEvent> extends ApplicationListener<E> {

    @Override
    default void onApplicationEvent(E event) {
        if (isApply(event))
            handleEvent(event);
    }

    void handleEvent(E event);

    default boolean isApply(E event) {
        return true;
    }

}
