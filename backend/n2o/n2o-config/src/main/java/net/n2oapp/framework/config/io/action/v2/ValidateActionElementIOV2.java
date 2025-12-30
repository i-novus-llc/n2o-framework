package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oValidateAction;
import net.n2oapp.framework.api.metadata.action.ValidateBreakOnEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия валидации версии 2.0
 */
@Component
public class ValidateActionElementIOV2 extends AbstractActionElementIOV2<N2oValidateAction> {

    @Override
    public void io(Element e, N2oValidateAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "datasource", a::getDatasource, a::setDatasource);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModelEnum.class);
        p.attributeEnum(e, "break-on", a::getBreakOn, a::setBreakOn, ValidateBreakOnEnum.class);

        p.children(e, null, "field",
                a::getFields, a::setFields,
                N2oValidateAction.Field::new, this::field);
    }

    private void field(Element e, N2oValidateAction.Field f, IOProcessor p) {
        p.attribute(e, "id", f::getId, f::setId);
    }

    @Override
    public String getElementName() {
        return "validate";
    }

    @Override
    public Class<N2oValidateAction> getElementClass() {
        return N2oValidateAction.class;
    }
}