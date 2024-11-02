package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oConfirmAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmActionPayload;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.StylesResolver.resolveStyles;

/**
 * Сборка действия подтверждения
 */
@Component
public class ConfirmActionCompiler extends AbstractActionCompiler<ConfirmAction, N2oConfirmAction> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConfirmAction.class;
    }

    @Override
    public ConfirmAction compile(N2oConfirmAction source, CompileContext<?, ?> context, CompileProcessor p) {
        ConfirmAction compiled = new ConfirmAction();
        compileAction(compiled, source, p);
        compiled.setType(p.resolve(property("n2o.api.action.confirm.type"), String.class));
        compiled.getPayload().setTitle(p.resolveJS(castDefault(source.getTitle(),
                () -> p.getMessage("n2o.api.action.confirm.title"))));
        compiled.getPayload().setText(p.resolveJS(castDefault(source.getText(),
                () -> p.getMessage("n2o.api.action.confirm.text"))));
        compiled.getPayload().setClassName(source.getClassName());
        compiled.getPayload().setStyle(resolveStyles(source.getStyle()));
        compiled.getPayload().setMode(castDefault(source.getType(),
                p.resolve(property("n2o.api.action.confirm.mode"), ConfirmType.class)));
        compiled.getPayload().setCloseButton(castDefault(source.getCloseButton(),
                p.resolve(property("n2o.api.action.confirm.close_button"), Boolean.class)));
        compiled.getPayload().setModel(getLocalModel(p));
        compiled.getPayload().setDatasource(getClientDatasourceId(getLocalDatasourceId(p), p));
        if (source.getConfirmButtons() != null && source.getConfirmButtons().length == 2) {
            if (source.getConfirmButtons()[0] instanceof N2oConfirmAction.OkButton) {
                compiled.getPayload().setReverseButtons(false);
                compiled.getPayload().setOk(compileOkButton(source.getConfirmButtons()[0], p));
                compiled.getPayload().setCancel(compileCancelButton(source.getConfirmButtons()[1], p));
            } else {
                compiled.getPayload().setReverseButtons(true);
                compiled.getPayload().setOk(compileOkButton(source.getConfirmButtons()[1], p));
                compiled.getPayload().setCancel(compileCancelButton(source.getConfirmButtons()[0], p));
            }
        } else {
            compiled.getPayload().setReverseButtons(false);
            compiled.getPayload().setOk(compileOkButton(new N2oConfirmAction.OkButton(), p));
            compiled.getPayload().setCancel(compileCancelButton(new N2oConfirmAction.CancelButton(), p));
        }
        return compiled;
    }

    private ConfirmActionPayload.ConfirmButton compileButton(N2oConfirmAction.ConfirmButton source) {
        ConfirmActionPayload.ConfirmButton button = new ConfirmActionPayload.ConfirmButton();
        button.setClassName(source.getCssClass());
        button.setStyle(resolveStyles(source.getStyle()));
        return button;
    }

    private ConfirmActionPayload.ConfirmButton compileOkButton(N2oConfirmAction.ConfirmButton source, CompileProcessor p) {
        ConfirmActionPayload.ConfirmButton button = compileButton(source);
        button.setLabel(castDefault(source.getLabel(), () -> p.getMessage("n2o.api.action.confirm.ok_label")));
        button.setColor(castDefault(source.getColor(), () -> p.resolve(property("n2o.api.action.confirm.ok_color"), String.class)));
        return button;
    }

    private ConfirmActionPayload.ConfirmButton compileCancelButton(N2oConfirmAction.ConfirmButton source, CompileProcessor p) {
        ConfirmActionPayload.ConfirmButton button = compileButton(source);
        button.setLabel(castDefault(source.getLabel(), () -> p.getMessage("n2o.api.action.confirm.cancel_label")));
        button.setColor(castDefault(source.getColor(), () -> p.resolve(property("n2o.api.action.confirm.cancel_color"), String.class)));
        return button;
    }
}
