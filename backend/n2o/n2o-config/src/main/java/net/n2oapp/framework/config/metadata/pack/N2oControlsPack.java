package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.control.*;
import net.n2oapp.framework.config.metadata.compile.control.filters_buttons.ClearButtonCompiler;
import net.n2oapp.framework.config.metadata.compile.control.filters_buttons.SearchButtonCompiler;
import net.n2oapp.framework.config.metadata.compile.control.filters_buttons.SearchButtonsCompiler;
import net.n2oapp.framework.config.metadata.compile.control.masked.*;

public class N2oControlsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oControlsV2IOPack(), new N2oControlsV3IOPack());
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
                new CodeViewerCompiler(),
                new MaskedInputCompiler(),
                new HtmlCompiler(),
                new TextAreaCompiler(),
                new RadioGroupCompiler(),
                new OutputTextCompiler(),
                new OutputListCompiler(),
                new HiddenCompiler(),
                new FileUploadCompiler(),
                new ImageUploadCompiler(),
                new CustomFieldCompiler(),
                new SearchButtonsCompiler(),
                new SearchButtonCompiler(),
                new ClearButtonCompiler(),
                new CustomControlCompiler(),
                new TextCompiler(),
                new SliderCompiler(),
                new RatingCompiler(),
                new AlertFieldCompiler(),
                new ButtonFieldCompiler(),
                new AutoCompleteCompiler(),
                new ProgressCompiler(),
                new StatusFieldCompiler(),
                new IntervalFieldCompiler<>(),
                new ImageFieldCompiler(),
                new NumberPickerCompiler(),
                new TimePickerCompiler(),
                new MarkdownCompiler(),
                new UuidFieldCompiler(),
                new EmailFieldCompiler(),
                new PhoneFieldCompiler(),
                new SnilsFieldCompiler()
        );
        b.binders(new ListControlBinder(),
                new FieldBinder());
    }
}
