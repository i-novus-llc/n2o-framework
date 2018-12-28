package net.n2oapp.framework.config.persister.widget.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;

/**
 * @author rgalina
 * @since 03.03.2016
 */
public abstract class N2oCellXmlPersister<E extends N2oCell> extends AbstractN2oMetadataPersister<E> {
    public N2oCellXmlPersister() {
        super("http://n2oapp.net/framework/config/schema/n2o-cell-1.0", "cl");
    }

    @Override
    public void setNamespaceUri(String uri) {
        this.namespaceUri = uri;
    }

    @Override
    public void setNamespacePrefix(String prefix) {
        this.namespacePrefix = prefix;
    }
}
