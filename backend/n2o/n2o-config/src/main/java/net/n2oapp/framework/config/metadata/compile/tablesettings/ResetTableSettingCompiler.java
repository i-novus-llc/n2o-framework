package net.n2oapp.framework.config.metadata.compile.tablesettings;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oResetTableSetting;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.compile.toolbar.table.TableSettingsGeneratorUtil.generateReset;

/**
 * Компиляция пользовательских настроек отображения таблицы
 */
@Component
public class ResetTableSettingCompiler implements BaseSourceCompiler<PerformButton, N2oResetTableSetting, CompileContext<?, ?>>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oResetTableSetting.class;
    }

    @Override
    public PerformButton compile(N2oResetTableSetting source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oButton button = generateReset(source.isGeneratedForSubMenu(), p);
        return p.compile(button, context, source);
    }
}
