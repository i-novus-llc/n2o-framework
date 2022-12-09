package net.n2oapp.framework.config.io.event;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.N2oOnChangeEvent;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись события изменения модели данных
 */
@Component
public class OnChangeEventIO extends AbstractEventIO<N2oOnChangeEvent> implements ActionsAwareIO<N2oOnChangeEvent> {
    @Override
    public Class<N2oOnChangeEvent> getElementClass() {
        return N2oOnChangeEvent.class;
    }

    @Override
    public String getElementName() {
        return "on-change";
    }

    @Override
    public void io(Element e, N2oOnChangeEvent m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attributeEnum(e, "model", m::getModel, m::setModel, ReduxModel.class);
        p.attribute(e, "field-id", m::getFieldId, m::setFieldId);
        action(e, m, p);
    }
}
