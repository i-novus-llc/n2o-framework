package net.n2oapp.framework.config.metadata.header;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.HeaderContext;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * Тест на получение json модели для header
 */
public class SimpleHeaderJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/header/testPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/header/testPage1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/header/testJsonSimpleHeaderWithIcon.header.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/header/testJsonSimpleHeader.header.xml"))
                .packs(new N2oPagesPack(), new N2oHeaderPack())
                .extensions(new TestExtAttributeMapper());
    }

    @Test
    public void simpleHeader() {
        check("plugins/Header/SimpleHeader/simpleHeaderData.json")
                .exclude("extraItems", "activeId", "items[0].security",
                        "items[0].extObject")
                .assertEquals(new HeaderContext("testJsonSimpleHeader"));
    }

    @Test
    public void testExtAttributes() {
        check(new ClassPathResource("net/n2oapp/framework/config/metadata/header/simpleHeaderData.json"))
                .exclude("extraItems", "activeId", "items[0].security")
                .assertEquals(new HeaderContext("testJsonSimpleHeaderWithIcon"));
    }

}
