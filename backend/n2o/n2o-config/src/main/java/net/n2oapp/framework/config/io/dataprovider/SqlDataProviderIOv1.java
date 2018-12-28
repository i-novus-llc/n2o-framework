package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class SqlDataProviderIOv1 implements NamespaceIO<N2oSqlDataProvider>, DataProviderIOv1 {
    @Override
    public Class<N2oSqlDataProvider> getElementClass() {
        return N2oSqlDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "sql";
    }

    @Override
    public void io(Element e, N2oSqlDataProvider m, IOProcessor p) {
        p.attribute(e, "data-source", m::getDataSource, m::setDataSource);
        p.attribute(e, "file", m::getFilePath, m::setFilePath);
        p.attribute(e, "row-mapper", m::getRowMapper, m::setRowMapper);
        p.text(e, m::getQuery, m::setQuery);
    }
}
