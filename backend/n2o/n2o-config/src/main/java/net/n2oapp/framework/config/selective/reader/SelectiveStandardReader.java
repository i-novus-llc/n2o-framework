package net.n2oapp.framework.config.selective.reader;

import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
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
import net.n2oapp.framework.config.io.page.*;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.io.region.LineRegionIOv1;
import net.n2oapp.framework.config.io.region.PanelRegionIOv1;
import net.n2oapp.framework.config.io.region.TabsRegionIOv1;
import net.n2oapp.framework.config.metadata.pack.N2oInvocationV2ReadersPack;
import net.n2oapp.framework.config.reader.event.*;
import net.n2oapp.framework.config.reader.query.QueryElementReaderV3;
import net.n2oapp.framework.config.reader.widget.widget3.CustomWidgetXmlReaderV3;
import net.n2oapp.framework.config.reader.widget.widget3.TreeXmlReaderV3;

/**
 * Чтение файлов для тестов
 */
public class SelectiveStandardReader extends SelectiveReader {

    public SelectiveStandardReader() {
    }

    public SelectiveStandardReader addPage2() {
        return addReader(new SimplePageElementIOv2())
                .addReader(new StandardPageElementIOv2())
                .addReader(new LeftRightPageElementIOv2())
                .addReader(new TopLeftRightPageElementIOv2())
                .addReader(new SearchablePageElementIOv2())
                .addReader(new LineRegionIOv1())
                .addReader(new PanelRegionIOv1())
                .addReader(new TabsRegionIOv1());
    }


    @Override
    public SelectiveStandardReader addReader(NamespaceReader reader) {
        return (SelectiveStandardReader) super.addReader(reader);
    }

    @Override
    public SelectiveStandardReader addReader(NamespaceIO io) {
        return (SelectiveStandardReader) super.addReader(io);
    }

    public SelectiveStandardReader addControlReader() {
        this.addReader(new CheckboxGroupIOv2())
                .addReader(new CheckboxIOv2())
                .addReader(new CustomFieldIOv2())
                .addReader(new DateIntervalIOv2())
                .addReader(new DatePickerIOv2())
                .addReader(new HiddenIOv2())
                .addReader(new HtmlIOv2())
                .addReader(new InputSelectIOv2())
                .addReader(new InputSelectTreeIOv2())
                .addReader(new InputTextIOv2())
                .addReader(new MaskedInputIOv2())
                .addReader(new OutputTextIOv2())
                .addReader(new PasswordIOv2())
                .addReader(new RadioGroupIOv2())
                .addReader(new PillsIOv2())
                .addReader(new SelectTreeIOv2())
                .addReader(new SelectIOv2())
                .addReader(new TextAreaIOv2())
                .addReader(new TextEditorIOv2())
                .addReader(new CodeEditorIOv2());
        return this;
    }

    public SelectiveStandardReader addFieldSet4Reader() {
        return addControlReader()
                .addReader(new SetFieldsetElementIOv4())
                .addReader(new LineFieldsetElementIOv4())
                .addReader(new MultiFieldsetElementIOv4());
    }

    public SelectiveStandardReader addEventsReader() {
        addReader(InvokeActionReaderV1.getInstance());
        addReader(new AnchorReaderV1());
        addReader(new OnClickReaderV1());
        addReader(new SetValueExpressionEventReaderV1());
        addReader(new OpenPageReaderV1());
        addReader(new ShowModalFormReaderV1());
        addReader(new ShowModalWithActionReaderV1());
        addReader(new ActionAndCloseReaderV1());
        addReader(new CloseEventReaderV1());
        addReader(new ValidateEventReaderV1());
        return this;
    }


    public SelectiveStandardReader addObjectReader() {
        return addReader(new ObjectElementIOv4()).addReader(new ObjectElementIOv3());
    }

    public SelectiveStandardReader addInvocationsReader2() {
        new N2oInvocationV2ReadersPack().build(this);
        return this;
    }

    public SelectiveStandardReader addDataProviders() {
        return addReader(new RestDataProviderIOv1())
                .addReader(new SqlDataProviderIOv1())
                .addReader(new JavaDataProviderIOv1())
                .addReader(new TestDataProviderIOv1())
                .addReader(new MongoDbDataProviderIOv1())
                .addReader(new CamundaDataProviderIOv1());
    }

    public SelectiveStandardReader addQueryReader() {
        return addReader(new QueryElementReaderV3())
                .addReader(new JavaDataProviderIOv1())
                .addReader(new QueryElementIOv4())
                .addReader(new TestDataProviderIOv1());
    }

    public SelectiveStandardReader addWidgetReaderV3() {
        // http://n2oapp.net/framework/config/schema/n2o-widget-3.0
        addReader(new TreeXmlReaderV3());
        addReader(new CustomWidgetXmlReaderV3());

        return this;
    }

}
