package net.n2oapp.framework.config.metadata;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.page.v3.SimplePageElementIOv3;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.io.MetadataParamHolder;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class XmlMetadataLoaderTest {

    /**
     * Проверка, что параметры переданные в load резолвятся в xml
     */
    @Test
    void testLoadWithParams(){
        XmlInfo info = new XmlInfo("testXmlInfo", N2oSimplePage.class, "net/n2oapp/framework/config/metadata/testXmlInfo.page.xml");
        XmlMetadataLoader xmlMetadataLoader = new XmlMetadataLoader(
                new ReaderFactoryByMap().register(new SimplePageElementIOv3()).register(new FormElementIOV4()).register(new InputTextIOv2()));
        SourceMetadata metadata = xmlMetadataLoader.load(info, "formName=Patients");
        assertThat(((N2oInputText)((N2oForm)((N2oSimplePage)metadata).getWidget()).getItems()[0]).getLabel(), is("Patients"));
        assertThat(MetadataParamHolder.getParams().isEmpty(), is(true));
    }
}
