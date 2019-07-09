package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.reader.control.*;

@Deprecated //use V2
public class N2oControlsV1ReadersPack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.readers(new N2oCustomXmlReaderV1(),
                new N2oInputTextXmlReaderV1(),
                new N2oMaskedInputXmlReaderV1(),
                new N2oDateTimeXmlReaderV1(),
                new N2oCheckboxGridXmlReaderV1(),
                new N2oCheckboxGroupXmlReaderV1(),
                new N2oButtonFieldXmlReaderV1(),
                new N2oClassifierXmlReaderV1(),
                new N2oCodeEditorXmlReaderV1(),
                new N2oCodeMergeXmlReaderV1(),
                new N2oCheckboxXmlReaderV1(),
                new N2oButtonsXmlReaderV1(),
                new N2oDateIntervalXmlReaderV1(),
                new N2oHiddenXmlReaderV1(),
                new N2oHtmlReaderV1(),
                new N2oEditGridXmlReaderV1(),
                new N2oFileUploadXmlReaderV1(),
                new N2oGroupClassifierXmlReaderV1(),
                new N2oDebugReaderV1(),
                new N2oTextAreaXmlReaderV1(),
                new N2oSelectXmlReaderV1(),
                new N2oInputSelectXmlReaderV1(),
                new N2oTimeIntervalXmlReaderV1(),
                new N2oTimeXmlReaderV1(),
                new N2oTextEditorReaderV1(),
                new N2oSelectTreeXmlReaderV1(),
                new N2oRadioGroupXmlReaderV1(),
                new N2oPasswordXmlReaderV1(),
                new N2oOutputTextXmlReaderV1(),
                new N2oInputIntervalReaderV1(),
                new N2oMultiClassifierXmlReaderV1(),
                new N2oInputSelectTreeXmlReaderV1(),
                new N2oSliderXmlReaderV1());
    }
}
