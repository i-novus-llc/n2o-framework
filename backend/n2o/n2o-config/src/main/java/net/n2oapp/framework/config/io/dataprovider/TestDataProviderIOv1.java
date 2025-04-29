package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class TestDataProviderIOv1 implements NamespaceIO<N2oTestDataProvider>, DataProviderIOv1 {

    @Override
    public Class<N2oTestDataProvider> getElementClass() {
        return N2oTestDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "test";
    }

    @Override
    public void io(Element e, N2oTestDataProvider m, IOProcessor p) {
        p.attribute(e, "file", m::getFile, m::setFile);
        p.attributeEnum(e, "operation", m::getOperation, m::setOperation, N2oTestDataProvider.OperationEnum.class);
        p.attributeEnum(e, "primary-key-type", m::getPrimaryKeyType, m::setPrimaryKeyType, N2oTestDataProvider.PrimaryKeyTypeEnum.class);
        p.attribute(e, "primary-key", m::getPrimaryKey, m::setPrimaryKey);
    }
}
