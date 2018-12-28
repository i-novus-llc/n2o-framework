package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;

import java.util.List;
import java.util.Map;

public class N2oButtonGeneratorFactory extends BaseMetadataFactory<ButtonGenerator> implements ButtonGeneratorFactory {

    public N2oButtonGeneratorFactory() {
    }

    public N2oButtonGeneratorFactory(Map<String, ButtonGenerator> beans) {
        super(beans);
    }

    @Override
    public List<ToolbarItem> generate(String code, N2oToolbar toolbar, CompileContext context, CompileProcessor p) {
        ButtonGenerator generator = produce((tg, c) -> tg.getCode().equals(c), code);
        return generator.generate(toolbar, context, p);
    }
}
