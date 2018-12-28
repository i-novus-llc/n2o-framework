package net.n2oapp.framework.config.reader.invocation;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.global.dao.RestErrorMapping;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oRestInvocation;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.reader.RestErrorMappingReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

/**
 * User: operhod
 * Date: 20.01.14
 * Time: 13:04
 */

@Component
public class RestInvocationReaderV2 extends AbstractInvocationReaderV2<N2oRestInvocation> {
    @Override
    public N2oRestInvocation read(Element element, Namespace namespace) {
        if (!"rest".equals(element.getName()))
            MetadataReaderException.throwExpectedElement(element, "rest");
        N2oRestInvocation query = new N2oRestInvocation();
        query.setQuery(ReaderJdomUtil.getElementString(element, "query"));
        assert query.getQuery() != null;
        query.setMethod(ReaderJdomUtil.getAttributeString(element, "method", "GET"));
        query.setDateFormat(ReaderJdomUtil.getAttributeString(element, "date-format"));
        query.setProxyHost(ReaderJdomUtil.getAttributeString(element, "proxy-host"));
        query.setProxyPort(ReaderJdomUtil.getAttributeInteger(element, "proxy-port"));
        RestErrorMapping em = RestErrorMappingReader.read(element.getChild("error-mapping", namespace), namespace);
        if (em != null) {
            query.setErrorMapping(em);
        }
        return query;
    }


    @Override
    public Class<N2oRestInvocation> getElementClass() {
        return N2oRestInvocation.class;
    }

    @Override
    public String getElementName() {
        return "rest";
    }
}
