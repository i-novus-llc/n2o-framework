package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись MongoDb провайдера данных
 */
@Component
public class MongoDbDataProviderIOv1 implements NamespaceIO<N2oMongoDbDataProvider>, DataProviderIOv1 {

    @Override
    public Class<N2oMongoDbDataProvider> getElementClass() {
        return N2oMongoDbDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "mongodb";
    }

    @Override
    public void io(Element e, N2oMongoDbDataProvider m, IOProcessor p) {
        p.attribute(e, "connection-url", m::getConnectionUrl, m::setConnectionUrl);
        p.attribute(e, "database-name", m::getDatabaseName, m::setDatabaseName);
        p.attribute(e, "collection-name", m::getCollectionName, m::setCollectionName);
        p.attributeEnum(e, "operation", m::getOperation, m::setOperation, N2oMongoDbDataProvider.OperationEnum.class);
    }
}