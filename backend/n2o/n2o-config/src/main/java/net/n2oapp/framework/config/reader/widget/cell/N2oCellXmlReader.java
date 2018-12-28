package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;

/**
 * @author rgalina
 * @since 01.03.2016
 */
interface N2oCellXmlReader<T extends N2oCell>
        extends NamespaceReader<T>, ReaderFactoryAware<T,N2oCellXmlReader<T>> {
}
