package net.n2oapp.framework.config.selective.persister;

import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.config.io.control.CustomFieldIOv2;
import net.n2oapp.framework.config.io.control.HiddenIOv2;
import net.n2oapp.framework.config.io.control.interval.DateIntervalIOv2;
import net.n2oapp.framework.config.io.control.list.*;
import net.n2oapp.framework.config.io.control.plain.*;
import net.n2oapp.framework.config.io.dataprovider.*;
import net.n2oapp.framework.config.io.fieldset.LineFieldsetElementIOv4;
import net.n2oapp.framework.config.io.fieldset.MultiFieldsetElementIOv4;
import net.n2oapp.framework.config.io.fieldset.SetFieldsetElementIOv4;
import net.n2oapp.framework.config.io.object.ObjectElementIOv3;
import net.n2oapp.framework.config.io.object.ObjectElementIOv4;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.persister.control.*;
import net.n2oapp.framework.config.persister.event.*;
import net.n2oapp.framework.config.persister.invocation.N2oJavaInvocationPersister;
import net.n2oapp.framework.config.persister.invocation.N2oRestInvocationPersister;

/**
 * Сохранение в xml-файл для тестов
 */
public class SelectiveStandardPersister extends SelectivePersister {

    public SelectiveStandardPersister addControlPersister() {
        return addEventPersister()
                .addPersister(new N2oInputTextPersister())
                .addPersister(new N2oPasswordPersister())
                .addPersister(new N2oCheckboxButtonsPersister())
                .addPersister(new N2oRadioButtonsPersister())
                .addPersister(new N2oCheckboxGroupPersister())
                .addPersister(new N2oCheckBoxPersister())
                .addPersister(new N2oClassifierPersister())
                .addPersister(new N2oCodeEditorPersister())
                .addPersister(new N2oCustomFieldPersister())
                .addPersister(new N2oDateIntervalPersister())
                .addPersister(new N2oTimeIntervalPersister())
                .addPersister(new N2oInputIntervalPersister())
                .addPersister(new N2oTimePersister())
                .addPersister(new N2oDateTimePersister())
                .addPersister(new N2oEditGridPersister())
                .addPersister(new N2oHiddenPersister())
                .addPersister(new N2oDebugPersister())
                .addPersister(new N2oMaskedInputPersister())
                .addPersister(new N2oMultiClassifierPersister())
                .addPersister(new N2oOutputTextPersister())
                .addPersister(new N2oRadioGroupPersister())
                .addPersister(new N2oSelectTreeXmlPersister())
                .addPersister(new N2oInputSelectTreeXmlPersister())
                .addPersister(new N2oSelectPersister())
                .addPersister(new N2oTextAreaPersister())
                .addPersister(new N2oTextEditorPersister())
                .addPersister(new N2oHtmlPersister())
                .addPersister(new N2oFileUploadPersister())
                .addPersister(new N2oGroupClassifierSinglePersister())
                .addPersister(new N2oGroupClassifierMultiPersister())
                .addPersister(new N2oInputSelectPersister())
                .addPersister(new N2oCodeMergePersister())
                .addPersister(new N2oCheckboxGridPersister())
                .addPersister(new N2oButtonFieldPersister())
                .addPersister(new CheckboxGroupIOv2())
                .addPersister(new CheckboxIOv2())
                .addPersister(new CustomFieldIOv2())
                .addPersister(new DateIntervalIOv2())
                .addPersister(new DatePickerIOv2())
                .addPersister(new HiddenIOv2())
                .addPersister(new HtmlIOv2())
                .addPersister(new InputSelectIOv2())
                .addPersister(new InputSelectTreeIOv2())
                .addPersister(new InputTextIOv2())
                .addPersister(new MaskedInputIOv2())
                .addPersister(new OutputTextIOv2())
                .addPersister(new PasswordIOv2())
                .addPersister(new RadioGroupIOv2())
                .addPersister(new PillsIOv2())
                .addPersister(new SelectTreeIOv2())
                .addPersister(new SelectIOv2())
                .addPersister(new SliderIOv2())
                .addPersister(new TextAreaIOv2())
                .addPersister(new TextEditorIOv2())
                .addPersister(new CodeEditorIOv2());
    }

    public SelectiveStandardPersister addFieldsetPersister() {
        return addControlPersister()
                .addPersister(new SetFieldsetElementIOv4())
                .addPersister(new LineFieldsetElementIOv4())
                .addPersister(new MultiFieldsetElementIOv4());
    }

    public SelectiveStandardPersister addObjectPersister() {
        return addPersister(new ObjectElementIOv4()).addPersister(new ObjectElementIOv3());
    }

    public SelectiveStandardPersister addInvocationObjectPersister() {
        return addObjectPersister()
                .addPersister(new N2oJavaInvocationPersister())
                .addPersister(new N2oRestInvocationPersister());
    }

    public SelectiveStandardPersister addQueryPersister() {
        return addPersister(new QueryElementIOv4());

    }

    public SelectiveStandardPersister addDataProviders() {
        return addPersister(new RestDataProviderIOv1())
                .addPersister(new SqlDataProviderIOv1())
                .addPersister(new JavaDataProviderIOv1())
                .addPersister(new TestDataProviderIOv1())
                .addPersister(new MongoDbDataProviderIOv1())
                .addPersister(new CamundaDataProviderIOv1());
    }

    public SelectiveStandardPersister addEventPersister() {
        return addPersister(AnchorPersister.getInstance())
                .addPersister(ShowModalFormPersister.getInstance())
                .addPersister(new InvokeActionPersister())
                .addPersister(OpenPagePersister.getInstance())
                .addPersister(ShowModalFromClassifierPersister.getInstance())
                .addPersister(ShowModalPersister.getInstance())
                .addPersister(OnClickPersister.getInstance())
                .addPersister(SetValueExpressionEventPersister.getInstance());
    }

    @Override
    public SelectiveStandardPersister addPersister(NamespacePersister persister) {
        return (SelectiveStandardPersister) super.addPersister(persister);
    }

    @Override
    public SelectiveStandardPersister addPersister(NamespaceIO io) {
        return (SelectiveStandardPersister) super.addPersister(io);
    }
}
