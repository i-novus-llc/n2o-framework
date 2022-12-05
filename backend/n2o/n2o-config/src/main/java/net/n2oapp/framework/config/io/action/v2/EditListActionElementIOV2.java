package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oEditListAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.action.editlist.ListOperationType;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия редактирования записи списка
 */
@Component
public class EditListActionElementIOV2 extends AbstractActionElementIOV2<N2oEditListAction> {
    @Override
    public Class<N2oEditListAction> getElementClass() {
        return N2oEditListAction.class;
    }

    @Override
    public String getElementName() {
        return "edit-list";
    }

    @Override
    public void io(Element e, N2oEditListAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attributeEnum(e, "operation", a::getOperation, a::setOperation, ListOperationType.class);
        p.attribute(e, "primary-key", a::getPrimaryKey, a::setPrimaryKey);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasourceId);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.attribute(e, "list-field-id", a::getListFieldId, a::setListFieldId);
        p.attribute(e, "item-datasource", a::getItemDatasourceId, a::setItemDatasourceId);
        p.attributeEnum(e, "item-model", a::getItemModel, a::setItemModel, ReduxModel.class);
        p.attribute(e, "item-field-id", a::getItemFieldId, a::setItemFieldId);
    }
}
