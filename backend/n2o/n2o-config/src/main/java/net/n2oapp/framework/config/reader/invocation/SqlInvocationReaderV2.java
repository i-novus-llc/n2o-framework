package net.n2oapp.framework.config.reader.invocation;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oSqlQuery;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class SqlInvocationReaderV2 extends AbstractInvocationReaderV2<N2oSqlQuery> {

    @Override
    public N2oSqlQuery read(Element element, Namespace namespace) {
        if (!"sql".equals(element.getName()))
            MetadataReaderException.throwExpectedElement(element, "sql");
        N2oSqlQuery query = new N2oSqlQuery();
        query.setQuery(element.getText());
        query.setDataSource(ReaderJdomUtil.getAttributeString(element, "data-source"));
        return query;
    }

    @Override
    public Class<N2oSqlQuery> getElementClass() {
        return N2oSqlQuery.class;
    }

    @Override
    public String getElementName() {
        return "sql";
    }
}
