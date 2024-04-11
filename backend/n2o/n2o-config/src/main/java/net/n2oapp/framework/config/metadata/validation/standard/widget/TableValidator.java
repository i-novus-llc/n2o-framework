package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Component
public class TableValidator extends ListWidgetValidator<N2oTable> {

    @Override
    public void validate(N2oTable source, SourceProcessor p) {
        super.validate(source, p);

        MetaActions actions = getAllMetaActions(p.getScope(MetaActions.class), source.getActions(), p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource(), actions);

        if (source.getRows() != null && source.getRows().getRowClick() != null)
            Arrays.stream(source.getRows().getRowClick().getActions()).forEach(item -> p.validate(item, widgetScope));

        if (source.getColumns() != null) {
            checkUniqueIds(source.getColumns(), AbstractColumn::getId, source.getId(), "id");
            checkUniqueIds(source.getColumns(), AbstractColumn::getTextFieldId, source.getId(), "text-field-id");
            Arrays.stream(source.getColumns())
                    .forEach(col -> p.validate(col, widgetScope));
        }

        if (source.getFilters() != null)
            p.safeStreamOf(source.getFilters().getItems()).forEach(item -> p.validate(item, widgetScope));

        checkEmptyToolbar(source);
    }

    private static void checkEmptyToolbar(N2oTable source) {
        if (source.getRows() != null && source.getRows().getRowOverlay() != null
                && source.getRows().getRowOverlay().getToolbar() != null
                && source.getRows().getRowOverlay().getToolbar().getGenerate() == null
                && source.getRows().getRowOverlay().getToolbar().getItems() == null)
            throw new N2oMetadataValidationException(
                    String.format("Не заданы элементы или атрибут 'generate' в тулбаре в <overlay> таблицы %s",
                            ValidationUtils.getIdOrEmptyString(source.getId())));
    }

    /**
     * Проверка наличия уникальности идентификаторов в колонках
     */
    private void checkUniqueIds(AbstractColumn[] columns, Function<AbstractColumn, String> function,
                                String sourceId, String attributeName) {
        Set<String> uniques = new HashSet<>();
        Arrays.stream(columns)
                .forEach(col -> {
                    String id = function.apply(col);
                    if (id != null) {
                        if (uniques.contains(id))
                            throw new N2oMetadataValidationException(
                                    String.format("Таблица %s содержит повторяющиеся значения %s=\"%s\" в <column>",
                                            ValidationUtils.getIdOrEmptyString(sourceId), attributeName, id));
                        uniques.add(id);
                    }
                });
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
