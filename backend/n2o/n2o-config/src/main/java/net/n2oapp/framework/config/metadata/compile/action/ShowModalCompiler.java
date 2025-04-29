package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.ModalSizeEnum;
import net.n2oapp.framework.api.metadata.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModalPayload;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция действия открытия модального окна
 */
@Component
public class ShowModalCompiler extends AbstractModalCompiler<ShowModal, N2oShowModal> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oShowModal.class;
    }

    @Override
    public ShowModal compile(N2oShowModal source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        ShowModal showModal = new ShowModal();
        compileModal(source, showModal, context, p);
        showModal.setType(p.resolve(property("n2o.api.action.show_modal.type"), String.class));
        return showModal;
    }

    protected void compilePayload(N2oShowModal source, ShowModal showModal, PageContext pageContext, CompileProcessor p) {
        ShowModalPayload payload = showModal.getPayload();
        payload.setSize(castDefault(source.getModalSize(),
                () -> p.resolve(property("n2o.api.action.show_modal.size"), ModalSizeEnum.class)));
        payload.setScrollable(castDefault(source.getScrollable(),
                () -> p.resolve(property("n2o.api.action.show_modal.scrollable"), Boolean.class)));
        payload.setCloseButton(true);
        payload.setPrompt(pageContext.getUnsavedDataPromptOnClose());
        payload.setHasHeader(castDefault(source.getHasHeader(),
                () -> p.resolve(property("n2o.api.action.show_modal.has_header"), Boolean.class)));
        payload.setClassName(source.getClassName());
        String backdrop = castDefault(source.getBackdrop(),
                () -> p.resolve(property("n2o.api.action.show_modal.backdrop"), String.class));
        payload.setBackdrop("true".equals(backdrop) || "false".equals(backdrop) ? Boolean.valueOf(backdrop) : backdrop);
        payload.setStyle(StylesResolver.resolveStyles(source.getStyle()));
    }
}
