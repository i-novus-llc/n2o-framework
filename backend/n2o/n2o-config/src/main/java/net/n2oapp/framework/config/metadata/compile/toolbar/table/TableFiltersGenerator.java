package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Генерация кнопки отображения фильтров таблицы
 */
@Component
public class TableFiltersGenerator implements ButtonGenerator {
    @Override
    public String getCode() {
        return "filters";
    }

    @Override
    public List<ToolbarItem> generate(N2oToolbar toolbar, CompileContext context, CompileProcessor p) {
        return Collections.singletonList(TableSettingsGeneratorUtil.generateFilters(toolbar.isGeneratedForSubMenu(), p));
    }
}
