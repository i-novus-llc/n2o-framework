package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.SourceComponent;

/**
 * Интерфейс, помечающий все элементы,
 * способные находиться внутри филдсета
 */
public interface FieldsetItem extends SourceComponent {
   FieldsetItem[] getItems();
}
