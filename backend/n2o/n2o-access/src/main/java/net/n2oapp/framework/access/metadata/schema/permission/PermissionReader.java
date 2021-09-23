package net.n2oapp.framework.access.metadata.schema.permission;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom2.Element;

/**
 * @author V. Alexeev.
 */
public class PermissionReader implements TypedElementReader<N2oPermission>, ReaderFactoryAware {

    private NamespaceReaderFactory readerFactory;

    public PermissionReader(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    @Override
    public N2oPermission read(Element element) {
        N2oPermission n2oPermission = new N2oPermission();
        n2oPermission.setId(ReaderJdomUtil.getAttributeString(element, "id"));
        n2oPermission.setName(ReaderJdomUtil.getAttributeString(element, "name"));

        AccessPoint[] accessPoints = ReaderJdomUtil.getChildren(element, null, readerFactory,
                "http://n2oapp.net/framework/config/schema/n2o-access-point-1.0", AccessPoint.class);
        n2oPermission.setAccessPoints(accessPoints);
        return n2oPermission;
    }

    @Override
    public Class<N2oPermission> getElementClass() {
        return N2oPermission.class;
    }

    @Override
    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    @Override
    public String getElementName() {
        return "permission";
    }
}
