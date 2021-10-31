package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.interval.DateIntervalIOv2;
import net.n2oapp.framework.config.io.control.list.*;
import net.n2oapp.framework.config.io.control.plain.*;
import net.n2oapp.framework.config.io.control.v2.*;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;

public class N2oControlsV2IOPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.ios(new CheckboxGroupIOv2(),
                new CheckboxIOv2(),
                new CustomFieldIOv2(),
                new DateIntervalIOv2(),
                new DatePickerIOv2(),
                new HiddenIOv2(),
                new HtmlIOv2(),
                new InputSelectIOv2(),
                new InputSelectTreeIOv2(),
                new InputTextIOv2(),
                new InputMoneyIOV2(),
                new MaskedInputIOv2(),
                new OutputTextIOv2(),
                new OutputListIOv2(),
                new PasswordIOv2(),
                new RadioGroupIOv2(),
                new SelectTreeIOv2(),
                new SelectIOv2(),
                new PillsIOv2(),
                new TextAreaIOv2(),
                new TextEditorIOv2(),
                new CodeEditorIOv2(),
                new FileUploadIOv2(),
                new ImageUploadIOv2(),
                new CodeEditorIOv2(),
                new CodeViewerIOv2(),
                new SearchButtonsIOv2(),
                new CustomControlIOv2(),
                new TextFieldIOv2(),
                new SliderIOv2(),
                new RatingIOv2(),
                new AlertIOv2(),
                new AutoCompleteIOv2(),
                new ProgressIOv2(),
                new StatusFieldIOv2(),
                new ButtonIO(),
                new SubmenuIO(),
                new ButtonFieldIOv2(),
                new RatingIOv2(),
                new IntervalFieldIOv2(),
                new ImageFieldIOv2(),
                new NumberPickerIOv2(),
                new TimePickerIOv2());
    }
}