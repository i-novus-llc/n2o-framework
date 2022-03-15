package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.pipeline.CompilePipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.register.JavaInfo;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Тестирование компиляции динамического тулбара
 */
public class ToolbarCompileDynamicTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .sources(new JavaInfo("testDynamic", N2oWidget.class))
                .providers(new TestToolbarDynamicProvider())
                .loaders(new JavaSourceLoader(builder.getEnvironment().getDynamicMetadataProviderFactory()));
    }

    @Test
    public void testDynamicToolbarCompiler() {
        builder.scan();
        N2oToolbar toolbar = new N2oToolbar();
        N2oButton deleteButton = new N2oButton();
        N2oButton addButton = new N2oButton();
        deleteButton.setType(LabelType.icon);
        deleteButton.setIcon("fa fa-pencil");
        deleteButton.setColor("danger");
        addButton.setType(LabelType.icon);
        addButton.setIcon("fa fa-pencil");
        addButton.setColor("primary");

        toolbar.setItems(new ToolbarItem[]{deleteButton, addButton});
        CompilePipeline pipeline = N2oPipelineSupport.compilePipeline(builder.getEnvironment());
        CompileContext<?, ?> context = new WidgetContext("");

        //Add external scope
        IndexScope indexScope = new IndexScope(5);
        Toolbar filterField = pipeline.compile().get(toolbar, context, indexScope);

        AbstractButton button = filterField.getButton("mi5");
        assertThat(button, notNullValue());

        AbstractButton button2 = filterField.getButton("mi6");
        assertThat(button2, notNullValue());
    }

    @Test
    public void testDynamicToolbarCompilerNoScope() {
        builder.scan();
        N2oToolbar toolbar = new N2oToolbar();
        N2oButton deleteButton = new N2oButton();
        N2oButton addButton = new N2oButton();
        deleteButton.setType(LabelType.icon);
        deleteButton.setIcon("fa fa-pencil");
        deleteButton.setColor("danger");
        addButton.setType(LabelType.icon);
        addButton.setIcon("fa fa-pencil");
        addButton.setColor("primary");

        toolbar.setItems(new ToolbarItem[]{deleteButton, addButton});
        CompilePipeline pipeline = N2oPipelineSupport.compilePipeline(builder.getEnvironment());
        CompileContext<?, ?> context = new WidgetContext("");

        //Add Scope in ToolBarCompiler
        Toolbar filterField = pipeline.compile().get(toolbar, context);
        AbstractButton button = filterField.getButton("mi0");

        assertThat(button, notNullValue());
    }

}
