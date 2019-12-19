package net.n2oapp.framework.config.metadata.header;

import net.n2oapp.framework.api.metadata.header.CompiledHeader;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.HeaderContext;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Тест сборки простого хедера
 */
public class SimpleHeaderCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        builder.getEnvironment().getContextProcessor().set("username", "test");
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack());
    }

    @Test
    public void inlineMenu() {
        CompiledHeader header = (CompiledHeader) compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/header/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/header/headerWithMenu.header.xml").get(new HeaderContext("headerWithMenu"));

        Assert.assertEquals("/pageRoute", header.getHomePageUrl());
        Assert.assertEquals("10px", header.getStyle().get("marginLeft"));
        Assert.assertEquals(3, header.getItems().size());
        Assert.assertEquals(2, header.getItems().get(0).getSubItems().size());
        Assert.assertEquals("test2", header.getItems().get(0).getSubItems().get(0).getLabel());
        Assert.assertEquals("testAttribute", header.getItems().get(0).getSubItems().get(0).getProperties().get("testAttr"));
        Assert.assertEquals("testAttribute", header.getItems().get(0).getSubItems().get(0).getJsonProperties().get("testAttr"));
        Assert.assertEquals("headerLabel", header.getItems().get(1).getLabel());

        Assert.assertEquals("#{username}", header.getExtraItems().get(0).getLabel());
        Assert.assertEquals("Test", header.getExtraItems().get(0).getSubItems().get(0).getLabel());
        Assert.assertEquals("https://ya.ru/", header.getExtraItems().get(0).getSubItems().get(0).getHref());
        Assert.assertEquals("test-icon", header.getExtraItems().get(0).getSubItems().get(0).getIcon());
        Assert.assertEquals(HeaderItem.LinkType.outer, header.getExtraItems().get(0).getSubItems().get(0).getLinkType());
    }

    @Test
    public void externalMenu() {
        CompiledHeader header = (CompiledHeader) compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/header/headerWithExternalMenu.header.xml",
                "net/n2oapp/framework/config/metadata/header/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/header/testMenu.menu.xml").get(new HeaderContext("headerWithExternalMenu"));

        Assert.assertEquals("/pageRoute", header.getHomePageUrl());
        Assert.assertEquals(3, header.getItems().size());
        Assert.assertEquals(2, header.getItems().get(0).getSubItems().size());
        Assert.assertEquals("test2", header.getItems().get(0).getSubItems().get(0).getLabel());
        Assert.assertEquals("testAttribute", header.getItems().get(0).getSubItems().get(0).getProperties().get("testAttr"));
        Assert.assertEquals("testAttribute", header.getItems().get(0).getSubItems().get(0).getJsonProperties().get("testAttr"));
        Assert.assertEquals("headerLabel", header.getItems().get(1).getLabel());
    }

    @Test
    public void testBind() {
        CompiledHeader header = (CompiledHeader) compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/header/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/header/headerWithMenu.header.xml")
                .bind().get(new HeaderContext("headerWithMenu"), null);

        Assert.assertEquals("test", header.getExtraItems().get(0).getLabel());
    }
}
