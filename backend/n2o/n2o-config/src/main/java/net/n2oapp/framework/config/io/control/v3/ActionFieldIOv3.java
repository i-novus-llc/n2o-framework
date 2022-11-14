package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oActionField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;

/**
 * Чтение/запись базовых свойств поля, содержащего действия
 */
public abstract class ActionFieldIOv3<T extends N2oActionField> extends FieldIOv3<T> implements ActionsAwareIO<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        action(e, m, p, "statuses", "dependencies");
    }
}
