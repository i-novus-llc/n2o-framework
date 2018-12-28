package net.n2oapp.framework.access.metadata.schema.user;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaReaderV1;
import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;

/**
 * Класс для чтения элемента доступа user из схемы доступа и преобразования его в инстанс N2oUserAccess
 * @author dednakov
 * @since 29.12.2016
 */
public class UserReader implements TypedElementReader<N2oUserAccess>, ReaderFactoryAware {

    private NamespaceReaderFactory readerFactory;

    public UserReader(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    @Override
    public N2oUserAccess read(Element element) {
        N2oUserAccess n2OUserAccess = new N2oUserAccess();
        n2OUserAccess.setId(ReaderJdomUtil.getAttributeString(element, "id"));
        AccessPoint[] accessPoints = ReaderJdomUtil.getChildren(element, null, readerFactory,
                SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB, AccessPoint.class);
        n2OUserAccess.setAccessPoints(accessPoints);
        return n2OUserAccess;
    }

    @Override
    public Class<N2oUserAccess> getElementClass() {
        return N2oUserAccess.class;
    }

    @Override
    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    @Override
    public String getElementName() {
        return "user";
    }
}
