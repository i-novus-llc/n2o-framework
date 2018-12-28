package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oDynamicSwitch;
import net.n2oapp.framework.api.metadata.persister.ElementPersister;
import net.n2oapp.framework.api.metadata.persister.ElementPersisterFactory;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author V. Alexeev.
 */
public class DynamicSwitchPersister {

    @SuppressWarnings("unchecked")
    public static <T extends NamespaceUriAware> Element persist(N2oDynamicSwitch<T> entity, Namespace namespace, NamespacePersisterFactory<T, NamespacePersister<T>> persisterFactory) {
        if ((entity.getCases() == null || entity.getCases().isEmpty()) && entity.getDefaultCase() == null)
            return null;
        Element switchElement = new Element("switch", namespace);
        PersisterJdomUtil.setAttribute(switchElement, "value-field-id", entity.getValueFieldId());
        if (entity.getCases() != null) {
            List<Element> cases = entity.getCases().entrySet().stream()
                    .map(c -> {
                        Element caseElement = new Element("case", namespace);
                        caseElement.addContent(persisterFactory.produce(c.getValue()).persist(c.getValue(), namespace));
                        PersisterJdomUtil.setAttribute(caseElement, "value", c.getKey());
                        return caseElement;
                    }).collect(Collectors.toList());
            switchElement.addContent(cases);
        }
        if (entity.getDefaultCase() != null) {
            Element defaultValue = new Element("default", namespace);
            defaultValue.addContent(persisterFactory.produce(entity.getDefaultCase()).persist(entity.getDefaultCase(),namespace));
            switchElement.addContent(defaultValue);
        }

        return switchElement;
    }

}
