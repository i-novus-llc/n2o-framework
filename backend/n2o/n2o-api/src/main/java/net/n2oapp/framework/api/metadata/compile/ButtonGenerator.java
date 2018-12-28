package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;

import java.util.List;

/**
 * Генерация кнопок по коду
 */
public interface ButtonGenerator {

    String getCode();

    List<ToolbarItem> generate(N2oToolbar toolbar, CompileContext context, CompileProcessor p);
}
