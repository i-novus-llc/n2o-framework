package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom.Namespace;

/**
 * Абстрактная реализация считывания ячейки таблицы
 */
public abstract class AbstractN2oCellXmlReader<E extends N2oCell> implements N2oCellXmlReader<E> {

    protected NamespaceReaderFactory readerFactory;
    private String namespaceUri = "http://n2oapp.net/framework/config/schema/n2o-cell-1.0";
    protected final static Namespace DEFAULT_EVENT_NAMESPACE_URI = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-event-1.0");

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }
}
