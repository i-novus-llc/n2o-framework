package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;

import java.util.function.Supplier;

/**
 *
 */
public class DummyFactoredReader extends AbstractFactoredReader {
    private N2oMetadata metadata;
    private Supplier<SourceMetadata> metadataSupplier;
    private Class<? extends SourceMetadata> metadataClass;

    public SourceMetadata getMetadata() {
        return metadata != null ? metadata : metadataSupplier.get();
    }

    public DummyFactoredReader(N2oMetadata metadata) {
        this.metadata = metadata;
    }

    public DummyFactoredReader(Supplier<SourceMetadata> metadataSupplier, Class<? extends SourceMetadata> metadataClass) {
        this.metadataSupplier = metadataSupplier;
        this.metadataClass = metadataClass;
    }

    @Override
    public NamespaceUriAware read(Element element, Namespace namespace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class getElementClass() {
        return metadata != null ? metadata.getClass() : metadataClass;
    }

    @Override
    public String getNamespaceUri() {
        return null;
    }

    @Override
    public String getElementName() {
        return null;
    }
}
