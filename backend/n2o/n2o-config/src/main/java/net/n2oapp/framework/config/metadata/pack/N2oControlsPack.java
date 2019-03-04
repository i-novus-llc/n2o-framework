package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.control.*;

public class N2oControlsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oControlsV1IOPack(), new N2oControlsV2IOPack());
        b.compilers(new InputTextCompiler(),
                new InputMoneyCompiler(),
                new DatePickerCompiler(),
                new DateIntervalCompiler(),
                new PasswordCompiler(),
                new SelectCompiler(),
                new InputSelectCompiler(),
                new InputSelectTreeCompiler(),
                new CheckboxCompiler(),
                new CheckboxGroupCompiler(),
                new TextEditorCompiler(),
                new CodeEditorCompiler(),
                new MaskedInputCompiler(),
                new HtmlControlCompiler(),
                new TextAreaCompiler(),
                new RadioGroupCompiler(),
                new OutputTextCompiler(),
                new OutputTextCompiler(),
                new HiddenCompiler(),
                new FileUploadCompiler(),
                new CustomFieldCompiler(),
                new SearchButtonsCompiler(),
                new CustomControlCompiler(),
                new TextCompiler()
        );
    }
}
