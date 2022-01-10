package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.v3.*;
import net.n2oapp.framework.config.io.control.v3.interval.DateIntervalIOv3;
import net.n2oapp.framework.config.io.control.v3.list.*;
import net.n2oapp.framework.config.io.control.v3.plain.*;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.v2.SubmenuIOv2;

public class N2oControlsV3IOPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.ios(new CheckboxGroupIOv3(),
                new CheckboxIOv3(),
                new CustomFieldIOv3(),
                new DateIntervalIOv3(),
                new DatePickerIOv3(),
                new HiddenIOv3(),
                new HtmlIOv3(),
                new InputSelectIOv3(),
                new InputSelectTreeIOv3(),
                new InputTextIOv3(),
                new InputMoneyIOV3(),
                new MaskedInputIOv3(),
                new OutputTextIOv3(),
                new OutputListIOv3(),
                new PasswordIOv3(),
                new RadioGroupIOv3(),
                new SelectTreeIOv3(),
                new SelectIOv3(),
                new PillsIOv3(),
                new TextAreaIOv3(),
                new TextEditorIOv3(),
                new CodeEditorIOv3(),
                new FileUploadIOv3(),
                new ImageUploadIOv3(),
                new CodeEditorIOv3(),
                new CodeViewerIOv3(),
                new SearchButtonsIOv3(),
                new CustomControlIOv3(),
                new TextFieldIOv3(),
                new SliderIOv3(),
                new RatingIOv3(),
                new AlertIOv3(),
                new AutoCompleteIOv3(),
                new ProgressIOv3(),
                new StatusFieldIOv3(),
                new ButtonIOv2(),
                new SubmenuIOv2(),
                new ButtonFieldIOv3(),
                new RatingIOv3(),
                new IntervalFieldIOv3(),
                new ImageFieldIOv3(),
                new NumberPickerIOv3(),
                new TimePickerIOv3());
    }
}