package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oMarkdown;
import net.n2oapp.framework.api.metadata.meta.control.Markdown;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.compileAction;

/**
 * Компиляция компонента markdown,
 * который позволяет задавать текст согласно markdown разметки, а отображать его в виде html.
 */
@Component
public class MarkdownCompiler extends FieldCompiler<Markdown, N2oMarkdown> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.markdown.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMarkdown.class;
    }

    @Override
    public Markdown compile(N2oMarkdown source, CompileContext<?, ?> context, CompileProcessor p) {
        Markdown field = new Markdown();
        field.setContent(p.resolveJS(source.getContent()));
        if (!ArrayUtils.isEmpty(source.getActionIds())) {
            MetaActions metaActions = p.getScope(MetaActions.class);
            if (metaActions == null)
                throw new N2oException("Actions " + String.join(",", source.getActionIds()) + " are not init!");
            field.setActions(new HashMap<>());
            for (String actionId : source.getActionIds()) {
                if (!metaActions.containsKey(actionId))
                    throw new N2oException("Action " + actionId + " is not init on form!");
                field.getActions().put(actionId, compileAction(metaActions.get(actionId).getN2oActions(), null, context,p));
            }
        }
        initDefaults(source, context, p);
        compileField(field, source, context, p);
        return field;
    }
}
