package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись GraphQL провайдера данных
 */
@Component
public class GraphqlDataProviderIOv1 implements NamespaceIO<N2oGraphqlDataProvider>, DataProviderIOv1 {

    @Override
    public Class<N2oGraphqlDataProvider> getElementClass() {
        return N2oGraphqlDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "graphql";
    }

    @Override
    public void io(Element e, N2oGraphqlDataProvider m, IOProcessor p) {
        p.attribute(e, "endpoint", m::getEndpoint, m::setEndpoint);
        p.attribute(e, "filter-separator", m::getFilterSeparator, m::setFilterSeparator);
        p.text(e, m::getQuery, m::setQuery);
    }
}
