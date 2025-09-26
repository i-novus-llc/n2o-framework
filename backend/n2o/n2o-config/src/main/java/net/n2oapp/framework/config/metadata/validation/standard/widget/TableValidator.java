package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.ExportFormatEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oAbstractTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oColumnsTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oExportTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oGroup;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailAction;

@Component
public class TableValidator extends ListWidgetValidator<N2oTable> {

    @Override
    public void validate(N2oTable source, SourceProcessor p) {
        super.validate(source, p);

        MetaActions actions = getAllMetaActions(p.getScope(MetaActions.class), source.getActions(), p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource(), actions);

        if (source.getRows() != null && source.getRows().getRowClick() != null) {
            Arrays.stream(source.getRows().getRowClick().getActions()).forEach(item -> p.validate(item, widgetScope));
            checkOnFailAction(source.getRows().getRowClick().getActions());
        }

        if (source.getColumns() != null) {
            N2oBaseColumn[] columns = Arrays.stream(source.getColumns())
                    .filter(N2oBaseColumn.class::isInstance)
                    .map(N2oBaseColumn.class::cast)
                    .toArray(N2oBaseColumn[]::new);
            checkUniqueIds(columns, N2oBaseColumn::getId, source.getId(), "id");
            checkUniqueIds(columns, N2oBaseColumn::getTextFieldId, source.getId(), "text-field-id");

            Arrays.stream(source.getColumns())
                    .forEach(col -> p.validate(col, widgetScope));
        }

        if (source.getFilters() != null)
            p.safeStreamOf(source.getFilters().getItems()).forEach(item -> p.validate(item, widgetScope));

        checkEmptyToolbar(source);
        checkUniqueTableSetting(source, N2oColumnsTableSetting.class, "ts:columns");
        checkUniqueTableSetting(source, N2oExportTableSetting.class, "ts:export");
        checkExportTableSettingDefaultFormat(source);
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
     * Проверяет, что настройки таблицы указанного типа встречаются только один раз
     */
    private void checkUniqueTableSetting(N2oTable source, Class<?> settingClass, String elementName) {
        if (source.getToolbars() == null) return;

        int[] counter = new int[1];

        Arrays.stream(source.getToolbars())
                .filter(toolbar -> toolbar.getItems() != null)
                .forEach(toolbar -> countTableSettings(toolbar.getItems(), counter, settingClass));

        if (counter[0] > 1) {
            throw new N2oMetadataValidationException(
                    String.format("В таблице %s найдено несколько элементов <%s/>. Допускается только один элемент.",
                            ValidationUtils.getIdOrEmptyString(source.getId()), elementName));
        }
    }

    /**
     * Рекурсивно подсчитывает элементы указанного типа в тулбаре
     *
     * @param toolbarItems массив элементов тулбара
     * @param counter      счетчик для подсчета
     * @param targetClass  класс элементов, которые нужно подсчитать
     */
    private void countTableSettings(ToolbarItem[] toolbarItems, int[] counter, Class<?> targetClass) {
        if (toolbarItems == null) return;

        for (ToolbarItem toolbarItem : toolbarItems) {
            if (targetClass.isInstance(toolbarItem)) {
                counter[0]++;
                if (counter[0] > 1) return;
            } else if (toolbarItem instanceof N2oSubmenu submenu) {
                countTableSettings(submenu.getMenuItems(), counter, targetClass);
                if (counter[0] > 1) return;
            } else if (toolbarItem instanceof N2oGroup group) {
                countTableSettings(group.getItems(), counter, targetClass);
                if (counter[0] > 1) return;
            }
        }
    }

    /**
     * Проверка наличия уникальности идентификаторов в колонках
     */
    private void checkUniqueIds(N2oBaseColumn[] columns, Function<N2oBaseColumn, String> function,
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


    /**
     * Проверка настроек экспорта в таблице (default-format содержится в списке format)
     */
    private void checkExportTableSettingDefaultFormat(N2oTable source) {
        if (source.getToolbars() == null) return;

        N2oExportTableSetting export = Arrays.stream(source.getToolbars())
                .map(toolbar -> findTableSettingByType(toolbar.getItems(), N2oExportTableSetting.class))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);

        if (export == null || export.getDefaultFormat() == null) return;

        ExportFormatEnum defaultFormat = export.getDefaultFormat();
        ExportFormatEnum[] format = castDefault(export.getFormat(), ExportFormatEnum.values());
        boolean found = Arrays.stream(format).anyMatch(f -> f == defaultFormat);

        if (!found) {
            throw new N2oMetadataValidationException(
                    String.format("В таблице %s в элементе <ts:export/> значение default-format=\"%s\" не содержится в списке format",
                            ValidationUtils.getIdOrEmptyString(source.getId()),
                            defaultFormat.getId()));
        }
    }

    /**
     * Рекурсивно ищет первый элемент <ts:export> в тулбаре
     */
    private <T extends N2oAbstractTableSetting> T findTableSettingByType(ToolbarItem[] toolbarItems, Class<T> settingClass) {
        if (toolbarItems == null) return null;

        for (ToolbarItem toolbarItem : toolbarItems) {
            if (settingClass.isInstance(toolbarItem)) {
                return settingClass.cast(toolbarItem);
            } else if (toolbarItem instanceof N2oSubmenu submenu) {
                T export = findTableSettingByType(submenu.getMenuItems(), settingClass);
                if (export != null) {
                    return export;
                }
            } else if (toolbarItem instanceof N2oGroup group) {
                T export = findTableSettingByType(group.getItems(), settingClass);
                if (export != null) {
                    return export;
                }
            }
        }
        return null;
    }
}
