package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;

import java.util.List;

/**
 * Фабрика генераторов кнопок по коду {@link ButtonGenerator}
 */
public interface ButtonGeneratorFactory extends MetadataFactory<ButtonGenerator> {

    List<ToolbarItem> generate(String code, N2oToolbar toolbar, CompileContext context, CompileProcessor p);
}
