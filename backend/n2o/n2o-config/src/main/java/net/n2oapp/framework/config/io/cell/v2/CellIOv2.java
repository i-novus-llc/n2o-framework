package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import org.jdom2.Namespace;

/**
 * Интерфейс ячейки
 */
public interface CellIOv2 extends NamespaceUriAware, BaseElementClassAware<N2oCell> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-cell-2.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default void setNamespaceUri(String namespaceUri) {
    }

    @Override
    default Class<N2oCell> getBaseElementClass() {
        return N2oCell.class;
    }
}
