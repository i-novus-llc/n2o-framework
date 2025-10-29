package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ClipboardButtonDataEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oClipboardButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ClipboardButton;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция кнопки {@code <clipboard-button>}
 */
@Component
public class ClipboardButtonCompiler extends BaseButtonCompiler<N2oClipboardButton, ClipboardButton> {

    @Override
    public Class<? extends N2oClipboardButton> getSourceClass() {
        return N2oClipboardButton.class;
    }

    @Override
    public ClipboardButton compile(N2oClipboardButton source, CompileContext<?, ?> context, CompileProcessor p) {
        ClipboardButton button = new ClipboardButton();
        initDefaults(source, context, p);
        compileBase(button, source, context, p);
        button.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.button.clipboard.src"), String.class)));
        button.setIcon(castDefault(source.getIcon(),
                () -> p.resolve(property("n2o.api.button.clipboard.icon"), String.class)));
        button.setColor(castDefault(source.getColor(),
                () -> p.resolve(property("n2o.api.button.clipboard.color"), String.class)));
        button.setType(castDefault(source.getType(),
                () -> p.resolve(property("n2o.api.button.clipboard.type"), ClipboardButtonDataEnum.class)));
        button.setData(p.resolveJS(source.getData()));
        button.setMessage(source.getMessage());
        compileDependencies(source, button, p);
        return button;
    }
}