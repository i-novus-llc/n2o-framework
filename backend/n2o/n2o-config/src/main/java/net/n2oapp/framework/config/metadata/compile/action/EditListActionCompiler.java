package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oEditListAction;
import net.n2oapp.framework.api.metadata.meta.action.editlist.EditListAction;
import net.n2oapp.framework.api.metadata.meta.action.editlist.EditListActionPayload;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция действия редактирования записи списка
 */
@Component
public class EditListActionCompiler extends AbstractActionCompiler<EditListAction, N2oEditListAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oEditListAction.class;
    }

    @Override
    public EditListAction compile(N2oEditListAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        EditListAction action = new EditListAction();
        compileAction(action, source, p);
        action.setType(p.resolve(property("n2o.api.action.edit_list.type"), String.class));

        action.getPayload().setOperation(source.getOperation());
        action.getPayload().setPrimaryKey(source.getPrimaryKey());
        action.getPayload().setItem(constructEditInfo(source.getItemDatasourceId(), source.getItemModel(), source.getItemFieldId(), p));
        action.getPayload().setList(constructEditInfo(source.getDatasourceId(), source.getModel(), source.getListFieldId(), p));
        return action;
    }

    @Override
    protected void initDefaults(N2oEditListAction source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        source.setPrimaryKey(p.cast(source.getPrimaryKey(),
                p.resolve(property("n2o.api.action.edit_list.primary_key"), String.class)));
        source.setItemDatasourceId(p.cast(source.getItemDatasourceId(), getLocalDatasourceId(p)));
        if (source.getItemDatasourceId() == null) {
            throw new N2oException("Item datasource is undefined for edit-list action");
        }

        source.setItemModel(p.cast(source.getItemModel(), getModelFromComponentScope(p)));
        source.setDatasourceId(p.cast(source.getDatasourceId(), source.getItemDatasourceId()));
        source.setModel(p.cast(source.getModel(), source.getItemModel()));
    }

    private EditListActionPayload.EditInfo constructEditInfo(String datasourceId, ReduxModel model, String fieldId, CompileProcessor p) {
        return new EditListActionPayload.EditInfo(getClientDatasourceId(datasourceId, p), model, fieldId);
    }
}
