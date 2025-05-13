package net.n2oapp.framework.config.metadata;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.config.io.MetadataParamHolder;
import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.control.v3.TextFieldIOv3;
import net.n2oapp.framework.config.io.page.v3.SimplePageElementIOv3;
import net.n2oapp.framework.config.io.page.v4.SimplePageElementIOv4;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XmlMetadataLoaderTest {

    /**
     * Проверка, что параметры переданные в load резолвятся в xml
     */
    @Test
    void testLoadWithParams() {
        XmlInfo info = new XmlInfo("testXmlInfo", N2oSimplePage.class, "net/n2oapp/framework/config/metadata/testXmlInfo.page.xml");
        XmlMetadataLoader xmlMetadataLoader = new XmlMetadataLoader(
                new ReaderFactoryByMap(null).register(new SimplePageElementIOv3()).register(new FormElementIOV4()).register(new InputTextIOv2()));
        SourceMetadata metadata = xmlMetadataLoader.load(info, "formName=Patients");
        assertThat(((N2oInputText) ((N2oForm) ((N2oSimplePage) metadata).getWidget()).getItems()[0]).getLabel(), is("Patients"));
        assertThat(MetadataParamHolder.getParams().isEmpty(), is(true));
    }

    /**
     * Проверка сообщения об ошибке
     */
    @Test
    void testReadException() {
        XmlInfo info = new XmlInfo("testException", N2oSimplePage.class, "net/n2oapp/framework/config/metadata/testException.page.xml");
        XmlMetadataLoader xmlMetadataLoader = new XmlMetadataLoader(
                new ReaderFactoryByMap(null).register(new SimplePageElementIOv4()).register(new FormElementIOV4()).register(new TextFieldIOv3()));
        N2oException exception = assertThrows(
                N2oException.class, () -> xmlMetadataLoader.load(info, null));
        assertEquals("Error reading metadata \"testException\".\nError on line 7, column 11: The element type \"text\" must be terminated by the matching end-tag \"</text>\".", exception.getMessage());
    }
}
