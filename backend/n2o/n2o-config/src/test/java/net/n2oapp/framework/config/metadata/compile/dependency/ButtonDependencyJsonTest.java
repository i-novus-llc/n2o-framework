package net.n2oapp.framework.config.metadata.compile.dependency;


import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга зависимости между полем и  кнопками
 */
public class ButtonDependencyJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testJsonForm.object.xml"));
    }

    @Test
    public void testButtonDependencyWidgetV3() {
        check("net/n2oapp/framework/config/metadata/compile/dependency/testButtonDependencyV3.widget.xml",
                "components/actions/ButtonDependency.meta.json")
                .cutJson("Page_Table.toolbar.topRight[0]")
                .exclude("id",
                        "buttons[0].confirm",
                        "buttons[1].confirm",
                        "buttons[2].confirm",
                        "buttons[0].conditions.visible[0].modelLink",
                        "buttons[1].conditions.enabled[0].modelLink",
                        "buttons[2].conditions.visible[0].modelLink",
                        "buttons[2].conditions.enabled[0].modelLink",
                        "buttons[0].hint",
                        "buttons[0].hintPosition",
                        "buttons[1].hint",
                        "buttons[1].hintPosition",
                        "buttons[2].hint",
                        "buttons[2].hintPosition",
                        "buttons[3].hint",
                        "buttons[3].subMenu[0].conditions.visible[0].modelLink",
                        "buttons[3].subMenu[0].conditions.enabled[0].modelLink",
                        "buttons[3].subMenu[1].conditions.visible[0].modelLink",
                        "buttons[3].subMenu[1].conditions.enabled[0].modelLink",
                        "buttons[3].subMenu[0].hintPosition",
                        "buttons[3].subMenu[0].hintPosition",
                        "buttons[3].subMenu[1].hintPosition",
                        "buttons[3].subMenu[1].hintPosition"
                )
                .cutXml("toolbar.topLeft[0]")
                .assertEquals();
    }

    @Test
    public void testButtonDependencyWidgetV4() {
        check("net/n2oapp/framework/config/metadata/compile/dependency/testButtonDependencyV4.widget.xml",
                "components/actions/ButtonDependency.meta.json")
                .cutJson("Page_Table.toolbar.topRight[0]")
                .exclude("id",
                        "buttons[0].confirm",
                        "buttons[1].confirm",
                        "buttons[2].confirm",
                        "buttons[0].hintPosition",
                        "buttons[1].hintPosition",
                        "buttons[2].hintPosition",
                        "buttons[0].conditions.visible[0].modelLink",
                        "buttons[1].conditions.enabled[0].modelLink",
                        "buttons[2].conditions.visible[0].modelLink",
                        "buttons[2].conditions.enabled[0].modelLink",
                        "buttons[3].hint",
                        "buttons[3].subMenu[0].conditions.visible[0].modelLink",
                        "buttons[3].subMenu[0].conditions.enabled[0].modelLink",
                        "buttons[3].subMenu[1].conditions.visible[0].modelLink",
                        "buttons[3].subMenu[1].conditions.enabled[0].modelLink",
                        "buttons[3].subMenu[0].hintPosition",
                        "buttons[3].subMenu[1].hintPosition")
                .cutXml("toolbar.topLeft[0]")
                .assertEquals();
    }
}

