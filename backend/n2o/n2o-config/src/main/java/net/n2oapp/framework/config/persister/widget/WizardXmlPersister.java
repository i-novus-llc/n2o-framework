package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWizard;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */

@Component
public class WizardXmlPersister extends WidgetXmlPersister<N2oWizard> {

    @Override
    public Element getWidget(N2oWizard n2o, Namespace namespace) {
        Element element = new Element(getElementName(), namespace);
        persistWidget(element, n2o, namespace);
        NamespacePersister fieldSetPersister = (NamespacePersister) persisterFactory.produce(namespace, N2oFieldSet.class);
        PersisterJdomUtil.setChildren(element, "steps", "step", n2o.getSteps(), (s, n) -> {
            Element step = new Element("step", n.getURI());
            PersisterJdomUtil.setAttribute(step, "name", s.getName());
            PersisterJdomUtil.setAttribute(step, "description", s.getDescription());
            PersisterJdomUtil.setAttribute(step, "icon", s.getIcon());
            PersisterJdomUtil.setAttribute(step, "condition", s.getCondition());
            PersisterJdomUtil.setAttribute(step, "next-action-id", s.getNextActionId());
            PersisterJdomUtil.setAttribute(step, "next-label", s.getNextLabel());
            PersisterJdomUtil.setChildren(step, s.getFieldSets(), fieldSetPersister);
            return step;
        });
        PersisterJdomUtil.setChild(element, "finish", n2o.getFinish(), (f, n) -> {
            Element finish = new Element("finish", n.getURI());
            PersisterJdomUtil.setAttribute(finish, "name", f.getName());
            PersisterJdomUtil.setAttribute(finish, "finish-label", f.getFinishLabel());
            PersisterJdomUtil.setAttribute(finish, "finish-action-id", f.getActionId());
            PersisterJdomUtil.setChildren(finish, f.getFieldSets(), fieldSetPersister);
            return finish;
        });

        return element;
    }

    @Override
    public Class<N2oWizard> getElementClass() {
        return N2oWizard.class;
    }

    @Override
    public String getElementName() {
        return "wizard";
    }
}
