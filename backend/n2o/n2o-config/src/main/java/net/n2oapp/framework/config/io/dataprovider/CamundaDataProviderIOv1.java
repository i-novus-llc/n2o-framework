package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oCamundaDataProvider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись Camunda провайдера данных
 */
@Component
public class CamundaDataProviderIOv1 implements NamespaceIO<N2oCamundaDataProvider>, DataProviderIOv1 {
    @Override
    public Class<N2oCamundaDataProvider> getElementClass() {
        return N2oCamundaDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "camunda";
    }

    @Override
    public void io(Element e, N2oCamundaDataProvider m, IOProcessor p) {
        p.attributeEnum(e, "operation", m::getOperation, m::setOperation, N2oCamundaDataProvider.OperationEnum.class);
    }

}
