package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.PersistersBuilder;
import net.n2oapp.framework.config.persister.control.*;

@Deprecated //use V2
public class N2oControlsV1PersistersPack implements MetadataPack<PersistersBuilder> {
    @Override
    public void build(PersistersBuilder b) {
        b.persisters(new N2oInputTextPersister(),
                new N2oPasswordPersister(),
                new N2oCheckboxButtonsPersister(),
                new N2oRadioButtonsPersister(),
                new N2oCheckboxGroupPersister(),
                new N2oCheckBoxPersister(),
                new N2oClassifierPersister(),
                new N2oCodeEditorPersister(),
                new N2oCustomFieldPersister(),
                new N2oDateIntervalPersister(),
                new N2oTimeIntervalPersister(),
                new N2oInputIntervalPersister(),
                new N2oTimePersister(),
                new N2oDateTimePersister(),
                new N2oEditGridPersister(),
                new N2oHiddenPersister(),
                new N2oDebugPersister(),
                new N2oMaskedInputPersister(),
                new N2oMultiClassifierPersister(),
                new N2oOutputTextPersister(),
                new N2oRadioGroupPersister(),
                new N2oSelectTreeXmlPersister(),
                new N2oInputSelectTreeXmlPersister(),
                new N2oSelectPersister(),
                new N2oTextAreaPersister(),
                new N2oTextEditorPersister(),
                new N2oHtmlPersister(),
                new N2oFileUploadPersister(),
                new N2oGroupClassifierSinglePersister(),
                new N2oGroupClassifierMultiPersister(),
                new N2oInputSelectPersister(),
                new N2oCodeMergePersister(),
                new N2oCheckboxGridPersister(),
                new N2oButtonFieldPersister(),
                new N2oSliderPersister());
    }
}
