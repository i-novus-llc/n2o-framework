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
import net.n2oapp.framework.config.persister.invocation.N2oJavaInvocationPersister;
import net.n2oapp.framework.config.persister.invocation.N2oRestInvocationPersister;

/**
 * Сохранение в xml-файл для тестов
 */
public class SelectiveStandardPersister extends SelectivePersister {

    public SelectiveStandardPersister addControlPersister() {
        return addPersister(new CheckboxGroupIOv2())
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

    public SelectiveStandardPersister addDataProviders() {
        return addPersister(new RestDataProviderIOv1())
                .addPersister(new SqlDataProviderIOv1())
                .addPersister(new JavaDataProviderIOv1())
                .addPersister(new TestDataProviderIOv1())
                .addPersister(new MongoDbDataProviderIOv1())
                .addPersister(new CamundaDataProviderIOv1());
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
