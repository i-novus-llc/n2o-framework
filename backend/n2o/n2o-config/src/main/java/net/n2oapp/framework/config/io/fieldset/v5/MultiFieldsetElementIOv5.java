package net.n2oapp.framework.config.io.fieldset.v5;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись филдсета с динамическим числом полей версии 5.0
 */
@Component
public class MultiFieldsetElementIOv5 extends FieldsetElementIOv5<N2oMultiFieldSet> {

    @Override
    public void io(Element e, N2oMultiFieldSet fs, IOProcessor p) {
        super.io(e, fs, p);
        p.attribute(e, "children-label", fs::getChildrenLabel, fs::setChildrenLabel);
        p.attribute(e, "first-children-label", fs::getFirstChildrenLabel, fs::setFirstChildrenLabel);
        p.attribute(e, "add-label", fs::getAddButtonLabel, fs::setAddButtonLabel);
        p.attribute(e, "remove-all-label", fs::getRemoveAllButtonLabel, fs::setRemoveAllButtonLabel);
        p.attributeBoolean(e, "can-remove-first", fs::getCanRemoveFirst, fs::setCanRemoveFirst);
        p.attributeBoolean(e, "can-add", fs::getCanAdd, fs::setCanAdd);
        p.attributeBoolean(e, "can-remove", fs::getCanRemove, fs::setCanRemove);
        p.attributeBoolean(e, "can-remove-all", fs::getCanRemoveAll, fs::setCanRemoveAll);
        p.attributeBoolean(e, "can-copy", fs::getCanCopy, fs::setCanCopy);
    }

    @Override
    public String getElementName() {
        return "multi-set";
    }

    @Override
    public Class<N2oMultiFieldSet> getElementClass() {
        return N2oMultiFieldSet.class;
    }
}
