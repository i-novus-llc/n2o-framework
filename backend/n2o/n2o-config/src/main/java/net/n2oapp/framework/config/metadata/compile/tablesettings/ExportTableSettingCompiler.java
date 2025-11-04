package net.n2oapp.framework.config.metadata.compile.tablesettings;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oExportTableSetting;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.metadata.compile.toolbar.PerformButtonCompiler;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.compile.toolbar.table.TableSettingsGeneratorUtil.generateExport;

/**
 * Компиляция пользовательских настроек отображения таблицы
 */
@Component
public class ExportTableSettingCompiler extends PerformButtonCompiler<N2oExportTableSetting, PerformButton> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oExportTableSetting.class;
    }

    @Override
    public PerformButton compile(N2oExportTableSetting source, CompileContext<?, ?> context, CompileProcessor p) {
        generateExport(source, p);
        return super.compile(source, context, p);
    }
}
