package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oAbstractTable;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.config.metadata.validation.ValidationUtil;
import org.springframework.stereotype.Component;

/**
 * Проверяет, что в таблице пары column-field-id + link_id уникальны, это необходимо для эвентов, так как
 * id эвентов для линков в колонке таблицы формируется как  tc_(column_field_id)_(link id)
 */
@Component
public class UniqueLinkIdsInTableCells extends TypedMetadataValidator<N2oAbstractTable> {
    @Override
    public Class<N2oAbstractTable> getMetadataClass() {
        return N2oAbstractTable.class;
    }

    @Override
    public void check(N2oAbstractTable n2oTable) {
        if (n2oTable.getColumns() != null && n2oTable.getColumns().length != 0) {
            ValidationUtil.checkIdsUnique(n2oTable.getColumns(), "В таблице встречаются колонки с неуникальным id");
        }
    }

}
