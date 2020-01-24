package net.n2oapp.framework.config.metadata.compile.dependency;

import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции зависимости между полем и  кнопками
 */
public class ButtonDependencyCompileTest extends SourceCompileTestBase {
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
    public void testButtonDependency() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/dependency/testButtonDependency.page.xml")
                .get(new PageContext("testButtonDependency"));
        List<AbstractButton> buttons = page.getWidgets().get("testButtonDependency_tab1").getToolbar().get("topLeft").get(0).getButtons();
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(0).getConditions().get(ValidationType.visible).get(0).getModelLink(), is("models.resolve['testButtonDependency_tab1']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(1).getConditions().get(ValidationType.enabled).get(0).getModelLink(), is("models.resolve['testButtonDependency_tab1']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(2).getConditions().get(ValidationType.visible).get(0).getModelLink(), is("models.resolve['testButtonDependency_tab1']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(2).getConditions().get(ValidationType.visible).get(0).getModelLink(), is("models.resolve['testButtonDependency_tab1']"));

        assertThat(buttons.get(1).getVisible(), is(false));
        assertThat(buttons.get(1).getEnabled(), is(false));
        assertThat(buttons.get(2).getDependencies().get(0).getType(), is(ValidationType.reset));
        assertThat(buttons.get(2).getDependencies().get(0).getOn().get(0), is("field1"));
        assertThat(buttons.get(2).getDependencies().get(0).getApplyOnInit(), is(false));
        assertThat(buttons.get(2).getDependencies().get(0).getExpression(), is("a==b"));
        assertThat(buttons.get(2).getDependencies().get(1).getType(), is(ValidationType.visible));
        assertThat(buttons.get(2).getDependencies().get(1).getOn().get(0), is("field1"));
        assertThat(buttons.get(2).getDependencies().get(1).getApplyOnInit(), is(false));
        assertThat(buttons.get(2).getDependencies().get(1).getExpression(), is("a==b"));
        assertThat(buttons.get(2).getDependencies().get(2).getType(), is(ValidationType.enabled));
        assertThat(buttons.get(2).getDependencies().get(2).getOn().isEmpty(), is(true));
        assertThat(buttons.get(2).getDependencies().get(2).getApplyOnInit(), is(true));
        assertThat(buttons.get(2).getDependencies().get(2).getExpression(), is("c==d"));

        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(0).getVisible(), is(false));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(0).getEnabled(), is(false));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(0).getType(), is(ValidationType.reset));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(0).getOn(), is(Arrays.asList("field1", "field2")));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(0).getApplyOnInit(), is(false));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(0).getExpression(), is("a==b"));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(1).getType(), is(ValidationType.visible));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(1).getOn(), is(Arrays.asList("field1", "field2")));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(1).getApplyOnInit(), is(false));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(1).getExpression(), is("a==b"));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(2).getType(), is(ValidationType.enabled));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(2).getOn().isEmpty(), is(true));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(2).getApplyOnInit(), is(true));
        assertThat(((Submenu) buttons.get(3)).getSubMenu().get(1).getDependencies().get(2).getExpression(), is("c==d"));
    }
}
