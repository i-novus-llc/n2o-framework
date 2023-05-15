package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Генерация системных кнопок таблицы
 */
@Component
public class TableSettingsGenerator implements ButtonGenerator {
    @Override
    public String getCode() {
        return "tableSettings";
    }

    @Override
    public List<ToolbarItem> generate(N2oToolbar toolbar, CompileContext context, CompileProcessor p) {
        List<ToolbarItem> result = new ArrayList<>();
        result.add(TableSettingsGeneratorUtil.generateFilters(toolbar, p));
        result.add(TableSettingsGeneratorUtil.generateColumns(p));
        result.add(TableSettingsGeneratorUtil.generateRefresh(p));
        result.add(TableSettingsGeneratorUtil.generateResize(p));
        result.add(TableSettingsGeneratorUtil.generateWordWrap(p));
        result.add(TableSettingsGeneratorUtil.generateExport(p));
        return result;
    }
}
