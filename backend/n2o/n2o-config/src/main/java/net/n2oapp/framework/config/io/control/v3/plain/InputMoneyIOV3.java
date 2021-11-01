package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.FractionFormatting;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputMoney;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.plain.PlainFieldIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента input-money версии 3.0
 */
@Component
public class InputMoneyIOV3 extends PlainFieldIOv3<N2oInputMoney> {

    @Override
    public void io(Element e, N2oInputMoney m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "suffix", m::getSuffix, m::setSuffix);
        p.attribute(e, "prefix", m::getPrefix, m::setPrefix);
        p.attribute(e, "thousands-separator", m::getThousandsSeparator, m::setThousandsSeparator);
        p.attribute(e, "decimal-separator", m::getDecimalSeparator, m::setDecimalSeparator);
        p.attributeInteger(e, "integer-limit", m::getIntegerLimit, m::setIntegerLimit);
        p.attributeEnum(e, "fraction-formatting", m::getFractionFormatting, m::setFractionFormatting, FractionFormatting.class);
    }

    @Override
    public Class<N2oInputMoney> getElementClass() {
        return N2oInputMoney.class;
    }

    @Override
    public String getElementName() {
        return "input-money";
    }
}
