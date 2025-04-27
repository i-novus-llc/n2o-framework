package net.n2oapp.framework.config.metadata.compile.tablesettings;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oColumnsTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ColumnsButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.compile.toolbar.table.TableSettingsGeneratorUtil.generateColumns;

/**
 * Компиляция пользовательских настроек отображения таблицы
 */
@Component
public class ColumnsTableSettingCompiler implements BaseSourceCompiler<ColumnsButton, N2oColumnsTableSetting, CompileContext<?, ?>>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oColumnsTableSetting.class;
    }

    @Override
    public ColumnsButton compile(N2oColumnsTableSetting source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oButton button = generateColumns(source.isGeneratedForSubMenu(), p);
        PerformButton performButton = p.compile(button, context, source);
        ColumnsButton columnsButton = new ColumnsButton(performButton);
        columnsButton.setDefaultColumns(source.getDefaultValue());
        columnsButton.setLocked(source.getLocked());
        return columnsButton;
    }
}
