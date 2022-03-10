package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphQlDataProvider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись GraphQL провайдера данных
 */
@Component
public class GraphQlDataProviderIOv1 implements NamespaceIO<N2oGraphQlDataProvider>, DataProviderIOv1 {

    @Override
    public Class<N2oGraphQlDataProvider> getElementClass() {
        return N2oGraphQlDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "graphql";
    }

    @Override
    public void io(Element e, N2oGraphQlDataProvider m, IOProcessor p) {
        p.attribute(e, "endpoint", m::getEndpoint, m::setEndpoint);
        p.attribute(e, "filter-separator", m::getFilterSeparator, m::setFilterSeparator);
        p.attribute(e, "page-mapping", m::getPageMapping, m::setPageMapping);
        p.attribute(e, "size-mapping", m::getSizeMapping, m::setSizeMapping);
        p.attribute(e, "access-token", m::getAccessToken, m::setAccessToken);
        p.text(e, m::getQuery, m::setQuery);
    }
}
