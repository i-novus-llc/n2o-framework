package net.n2oapp.framework.config.metadata.compile.tablesettings;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oResizeTableSetting;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ResizeButton;
import net.n2oapp.framework.config.metadata.compile.toolbar.PerformButtonCompiler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static net.n2oapp.framework.config.metadata.compile.toolbar.table.TableSettingsGeneratorUtil.generateResize;

/**
 * Компиляция пользовательских настроек отображения таблицы
 */
@Component
public class ResizeTableSettingCompiler extends PerformButtonCompiler<N2oResizeTableSetting, ResizeButton> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oResizeTableSetting.class;
    }

    @Override
    public ResizeButton compile(N2oResizeTableSetting source, CompileContext<?, ?> context, CompileProcessor p) {
        generateResize(source, p);
        ResizeButton resizeButton = super.compile(source, context, p);
        if (source.getSize() != null)
            resizeButton.setSize(Arrays.stream(source.getSize())
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new));
        return resizeButton;
    }

    @Override
    protected ResizeButton constructButton() {
        return new ResizeButton();
    }
}