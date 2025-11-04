package net.n2oapp.framework.config.metadata.compile.tablesettings;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oResetTableSetting;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.metadata.compile.toolbar.PerformButtonCompiler;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.compile.toolbar.table.TableSettingsGeneratorUtil.generateReset;

/**
 * Компиляция пользовательских настроек отображения таблицы
 */
@Component
public class ResetTableSettingCompiler extends PerformButtonCompiler<N2oResetTableSetting, PerformButton> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oResetTableSetting.class;
    }

    @Override
    public PerformButton compile(N2oResetTableSetting source, CompileContext<?, ?> context, CompileProcessor p) {
        generateReset(source, p);
        return super.compile(source, context, p);
    }
}
