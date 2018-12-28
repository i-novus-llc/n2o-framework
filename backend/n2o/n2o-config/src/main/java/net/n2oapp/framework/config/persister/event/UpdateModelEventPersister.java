package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.UpdateModelAction;
import net.n2oapp.framework.config.persister.tools.PreFilterPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохраняет событие update-model в xml-файл
 */
@Component
public class UpdateModelEventPersister extends N2oEventXmlPersister<UpdateModelAction> {

    private static final UpdateModelEventPersister instance = new UpdateModelEventPersister();

    public UpdateModelEventPersister() {
    }

    public static UpdateModelEventPersister getInstance() {
        return instance;
    }

    @Override
    public Element persist(UpdateModelAction updateModelEvent, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "query-id", updateModelEvent.getQueryId());
        setAttribute(root, "master-field-id", updateModelEvent.getMasterFieldId());
        setAttribute(root, "detail-field-id", updateModelEvent.getDetailFieldId());
        setAttribute(root, "target-field-id", updateModelEvent.getTargetFieldId());
        setAttribute(root, "value-field-id", updateModelEvent.getValueFieldId());
        setAttribute(root, "target", updateModelEvent.getTarget());
        PreFilterPersister.setPreFilter(updateModelEvent.getPreFilters(), root, namespace);
        return root;
    }

    @Override
    public Class<UpdateModelAction> getElementClass() {
        return UpdateModelAction.class;
    }

    @Override
    public String getElementName() {
        return "update-model";
    }
}
