package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.BaseColumn;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание данных в столбце таблицы
 */
@Component
public class AbstractColumnBinder implements BaseMetadataBinder<AbstractColumn> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return AbstractColumn.class;
    }

    @Override
    public AbstractColumn bind(AbstractColumn compiled, BindProcessor p) {
        if (compiled instanceof BaseColumn baseColumn) {
            baseColumn.getConditions().values().forEach(conditions ->
                    conditions.forEach(c -> c.setExpression(p.resolveTextWithQuotes(c.getExpression()))));
        }
        return compiled;
    }
}
