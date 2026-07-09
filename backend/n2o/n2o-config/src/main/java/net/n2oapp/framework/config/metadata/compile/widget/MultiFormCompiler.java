package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oMultiForm;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.metadata.meta.widget.multiform.MultiForm;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;

@Component
public class MultiFormCompiler extends BaseListWidgetCompiler<MultiForm, N2oMultiForm> {

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.multi_form.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiForm.class;
    }

    @Override
    public MultiForm compile(N2oMultiForm source, CompileContext<?, ?> context, CompileProcessor p) {
        MultiForm multiForm = new MultiForm();
        compileBaseWidget(multiForm, source, context, p);

        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledQuery query = getQuery(datasource, p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModelEnum.DATASOURCE, p);
        MetaActions widgetActions = initMetaActions(source, p);
        if (source.getForm() != null) {
            multiForm.getForm().setPrompt(castDefault(source.getUnsavedDataPrompt(),
                    () -> p.resolve(property("n2o.api.widget.multi_form.unsaved_data_prompt"), Boolean.class)));
            List<FieldSet> fieldSets = initFieldSets(source.getForm().getItems(), context, p,
                    widgetScope, query, object, widgetActions,
                    new ModelsScope(ReduxModelEnum.DATASOURCE, widgetScope.getClientDatasourceId(), p.getScope(Models.class), "[index]"),
                    castDefault(p.getScope(SubModelsScope.class), SubModelsScope::new),
                    new MomentScope(N2oValidation.ServerMomentEnum.BEFORE_OPERATION),
                    castDefault(p.getScope(CopiedFieldScope.class), CopiedFieldScope::new),
                    new WidgetParamScope(),
                    new ComponentScope(source),
                    castDefault(p.getScope(ValidationScope.class), ValidationScope::new),
                    new MultiFormScope(true, datasource.getId()));
            multiForm.getForm().setFieldsets(fieldSets);
            multiForm.getForm().setModelPrefix("datasource");
        }
        compileMetaActions(source, context, p, p.getScope(PageIndexScope.class), widgetActions, widgetScope,
                object, null, new MultiFormScope(false, datasource.getId()));
        multiForm.setToolbar(compileToolbar(source, "n2o.api.widget.toolbar.place", context, p, object,
                widgetActions, widgetScope, null, new MultiFormScope(false, datasource.getId())));
        multiForm.setPaging(compilePaging(source, p.resolve(property("n2o.api.widget.list.size"), Integer.class), p, widgetScope));
        return multiForm;
    }
}
