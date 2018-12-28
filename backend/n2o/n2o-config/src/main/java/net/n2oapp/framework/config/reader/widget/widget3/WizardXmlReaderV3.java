package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWizard;
import net.n2oapp.framework.config.reader.fieldset.FieldSetReaderUtil;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */

@Component
public class WizardXmlReaderV3 extends WidgetBaseXmlReaderV3<N2oWizard> {

    @Override
    public String getElementName() {
        return "wizard";
    }

    @Override
    public N2oWizard read(Element element, Namespace namespace) {
        N2oWizard wizard = new N2oWizard();
        readWidgetDefinition(element, namespace, wizard);
        N2oWizard.Step[] steps = ReaderJdomUtil.getChildren(element, "steps", "step", (e) -> {
            N2oWizard.Step step = new N2oWizard.Step();
            step.setName(ReaderJdomUtil.getAttributeString(e, "name"));
            step.setDescription(ReaderJdomUtil.getAttributeString(e, "description"));
            step.setIcon(ReaderJdomUtil.getAttributeString(e, "icon"));
            step.setCondition(ReaderJdomUtil.getAttributeString(e, "condition"));
            step.setNextActionId(ReaderJdomUtil.getAttributeString(e, "next-action-id"));
            step.setNextLabel(ReaderJdomUtil.getAttributeString(e, "next-label"));
            step.setFieldSets(FieldSetReaderUtil.getFieldSet(e, e.getNamespace(), readerFactory));
            return step;
        }, N2oWizard.Step.class);
        N2oWizard.Finish finish = ReaderJdomUtil.getChild(element, "finish", (e) -> {
            N2oWizard.Finish f = new N2oWizard.Finish();
            f.setName(ReaderJdomUtil.getAttributeString(e, "name"));
            f.setFinishLabel(ReaderJdomUtil.getAttributeString(e, "finish-label"));
            f.setActionId(ReaderJdomUtil.getAttributeString(e, "finish-action-id"));
            f.setFieldSets(FieldSetReaderUtil.getFieldSet(e, e.getNamespace(), readerFactory));
            return f;
        });
        wizard.setSteps(steps);
        wizard.setFinish(finish);
        return wizard;
    }

    @Override
    public Class<N2oWizard> getElementClass() {
        return N2oWizard.class;
    }
}
