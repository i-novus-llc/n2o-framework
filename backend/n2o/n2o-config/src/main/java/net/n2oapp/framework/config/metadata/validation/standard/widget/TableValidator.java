package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class TableValidator implements SourceValidator<N2oTable>, SourceClassAware {

    @Override
    public void validate(N2oTable source, SourceProcessor p) {
        MetaActions actions = new MetaActions(
                p.safeStreamOf(source.getActions()).collect(Collectors.toMap(ActionBar::getId, Function.identity()))
        );
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource(), actions);

        if (nonNull(source.getRows()) && nonNull(source.getRows().getRowClick()))
            Arrays.stream(source.getRows().getRowClick().getActions()).forEach(item -> p.validate(item, widgetScope));

        if (nonNull(source.getColumns())) {
            checkUniqueIds(source.getColumns(), AbstractColumn:: getId, source.getId(), "id");
            checkUniqueIds(source.getColumns(), AbstractColumn::getTextFieldId, source.getId(), "text-field-id");
            Arrays.stream(source.getColumns())
                    .forEach(col -> p.validate(col, widgetScope));
        }

        p.safeStreamOf(source.getFilters()).forEach(item -> p.validate(item, widgetScope));
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
                    if (nonNull(id)) {
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
