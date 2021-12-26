package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.control.list.CheckingStrategy;
import net.n2oapp.framework.api.metadata.meta.control.InputSelectTree;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента ввода с выбором в выпадающем списке в виде дерева
 */
public class InputSelectTreeCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelect.query.xml"));
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new InputSelectTreeCompiler());
    }

    @Test
    public void testInputSelectTree() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testInputSelectTree.page.xml")
                .get(new PageContext("testInputSelectTree"));
        Form form = (Form) page.getWidget();
        InputSelectTree ist = (InputSelectTree) ((StandardField) form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(ist.getSrc(), is("InputSelectTree"));
        assertThat(ist.getParentFieldId(), is("testParentFieldId"));
        assertThat(ist.getHasChildrenFieldId(), is("testHasChildrenFieldId"));
        assertThat(ist.getCheckingStrategy(), is(CheckingStrategy.child));
        assertThat(ist.getMaxTagCount(), is(5));
        assertThat(ist.getSize(), is(35));
        assertThat(ist.isHasCheckboxes(), is(true));
        assertThat(ist.getMaxTagTextLength(), is(20));
    }
}
