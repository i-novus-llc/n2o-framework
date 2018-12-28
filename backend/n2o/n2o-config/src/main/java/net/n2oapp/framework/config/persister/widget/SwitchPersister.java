package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.Map;

/**
 * Created by enuzhdina on 22.07.2015.
 */
public class SwitchPersister {

    public static Element persist(N2oSwitch entity, Namespace namespace) {
        if ((entity.getCases() == null || entity.getCases().isEmpty()) && (entity.getDefaultCase() == null || entity.getDefaultCase().isEmpty()))
            return null;
        Element switchElement = new Element("switch", namespace);
        PersisterJdomUtil.setAttribute(switchElement, "value-field-id", entity.getValueFieldId());
        if (entity.getCases() != null) {
            for (Map.Entry entry : entity.getCases().entrySet()) {
                Element caseElement = PersisterJdomUtil.setElementString(switchElement, "case", entry.getValue().toString());
                PersisterJdomUtil.setAttribute(caseElement, "value", entry.getKey().toString());
            }
        }
        PersisterJdomUtil.setElementString(switchElement, "default", entity.getDefaultCase());
        return switchElement;
    }
}
