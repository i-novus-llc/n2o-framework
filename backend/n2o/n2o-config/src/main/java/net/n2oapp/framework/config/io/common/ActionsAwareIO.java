package net.n2oapp.framework.config.io.common;

import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;

import javax.annotation.Nullable;

/**
 *  Интерфейс чтения/записи компонентов {@link ActionsAware}
 */
public interface ActionsAwareIO<T extends ActionsAware> {

    default String actionsSequenceTag() {
        return null;
    }

    default Namespace actionsNamespace() {
        return ActionIOv2.NAMESPACE;
    }

    default void action(Element e, T m, IOProcessor p, String... ignores) {
        action(e, m, actionsSequenceTag(), actionsNamespace(), p, ignores);
    }

    static void action(Element e, ActionsAware m, @Nullable String actionsSequenceTag, Namespace actionsNamespace,
                       IOProcessor p, String... ignores) {
        p.attribute(e, "action-id", m::getActionId, m::setActionId);
        p.anyChildren(e, actionsSequenceTag, m::getActions, m::setActions, p.anyOf(N2oAction.class)
                .ignore(ignores), actionsNamespace);
    }
}
