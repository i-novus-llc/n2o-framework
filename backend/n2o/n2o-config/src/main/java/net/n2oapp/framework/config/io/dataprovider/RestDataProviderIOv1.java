package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class RestDataProviderIOv1 implements NamespaceIO<N2oRestDataProvider>, DataProviderIOv1 {

    @Override
    public Class<N2oRestDataProvider> getElementClass() {
        return N2oRestDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "rest";
    }

    @Override
    public void io(Element e, N2oRestDataProvider m, IOProcessor p) {
        p.attributeEnum(e, "method", m::getMethod, m::setMethod, N2oRestDataProvider.Method.class);
        p.attribute(e, "filters-separator", m::getFiltersSeparator, m::setFiltersSeparator);
        p.attribute(e, "sorting-separator", m::getSortingSeparator, m::setSortingSeparator);
        p.attribute(e, "select-separator", m::getSelectSeparator, m::setSelectSeparator);
        p.attribute(e, "join-separator", m::getJoinSeparator, m::setJoinSeparator);
        p.attribute(e, "content-type", m::getContentType, m::setContentType);
        p.attribute(e, "proxy-host", m::getProxyHost, m::setProxyHost);
        p.attribute(e, "forwarded-headers", m::getForwardedHeaders, m::setForwardedHeaders);
        p.attribute(e, "forwarded-cookies", m::getForwardedCookies, m::setForwardedCookies);
        p.attributeInteger(e, "proxy-port", m::getProxyPort, m::setProxyPort);
        p.text(e, m::getQuery, m::setQuery);
    }

}
