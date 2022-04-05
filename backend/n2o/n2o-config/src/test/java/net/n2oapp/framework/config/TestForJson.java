package net.n2oapp.framework.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.jackson.ComponentTypeResolver;
import net.n2oapp.framework.api.metadata.jackson.SingletonTypeIdHandlerInstantiator;
import net.n2oapp.framework.config.io.page.v3.*;
import net.n2oapp.framework.config.io.page.v4.*;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.v4.*;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.XmlIOReader;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.mockito.Mockito.mock;

public class TestForJson  extends SourceCompileTestBase {

    private ObjectMapper mapper;
    XmlIOReader reader;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack());
        builder.scanComponentTypes("net.n2oapp.framework");
        builder.componentTypes(N2oPage.class);
        SingletonTypeIdHandlerInstantiator instantiator = new SingletonTypeIdHandlerInstantiator();
        ComponentTypeResolver typeIdResolver = new ComponentTypeResolver();
        typeIdResolver.setRegister(builder.getEnvironment().getComponentTypeRegister());
        instantiator.addTypeIdResolver(ComponentTypeResolver.class, typeIdResolver);
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setHandlerInstantiator(instantiator);
        reader = new XmlIOReader();
        reader.ios(new SimplePageElementIOv3(),
                new StandardPageElementIOv3(),
                new LeftRightPageElementIOv3(),
                new TopLeftRightPageElementIOv3(),
                new SearchablePageElementIOv3(),
                new StandardPageElementIOv4(),
                new SimplePageElementIOv4(),
                new LeftRightPageElementIOv4(),
                new SearchablePageElementIOv4(),
                new TopLeftRightPageElementIOv4(),
                new FormElementIOV4(),
                new TableElementIOV4(),
                new ListWidgetElementIOv4(),
                new HtmlWidgetElementIOv4(),
                new CustomWidgetIOv4(),
                new TreeElementIOv4(),
                new ChartWidgetIOv4(),
                new CalendarWidgetIOv4(),
                new ButtonIO(),
                new SubmenuIO(),
                new TilesWidgetIOV4(),
                new CardsWidgetIOV4());
    }

    @Test
    public void testTo() throws IOException {
        N2oSimplePage page = read("net/n2oapp/framework/config/jsontest.page.xml")
                .get("jsontest", N2oSimplePage.class);
        String json = mapper.writeValueAsString(page);
        N2oPage sp = mapper.readValue(json, N2oPage.class);
        reader.persistAndCompareWithSample(sp, FileSystemUtil.getContentFromResource(new ClassPathResource("net/n2oapp/framework/config/jsontest.page.xml")));
    }

}