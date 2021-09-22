package net.n2oapp.framework.access.metadata.schema.role;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaReaderV1;
import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom2.Element;

/**
 * @author V. Alexeev.
 */
public class RoleReader implements TypedElementReader<N2oRole>, ReaderFactoryAware {

    private NamespaceReaderFactory readerFactory;

    public RoleReader(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    @Override
    public N2oRole read(Element element) {
        N2oRole n2oRole = new N2oRole();
        n2oRole.setId(ReaderJdomUtil.getAttributeString(element, "id"));
        n2oRole.setName(ReaderJdomUtil.getAttributeString(element, "name"));
        AccessPoint[] accessPoints = ReaderJdomUtil.getChildren(element, null, readerFactory,
                SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB, AccessPoint.class);
        n2oRole.setAccessPoints(accessPoints);
        return n2oRole;
    }

    @Override
    public Class<N2oRole> getElementClass() {
        return N2oRole.class;
    }

    @Override
    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    @Override
    public String getElementName() {
        return "role";
    }
}
