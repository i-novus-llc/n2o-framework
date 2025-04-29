package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.control.list.CheckingStrategyEnum;
import net.n2oapp.framework.api.metadata.meta.control.InputSelectTree;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента ввода с выбором в выпадающем списке в виде дерева
 */
class InputSelectTreeCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelect.query.xml"));
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oControlsV3IOPack());
        builder.compilers(new InputSelectTreeCompiler());
    }

    @Test
    void testInputSelectTree() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testInputSelectTree.page.xml")
                .get(new PageContext("testInputSelectTree"));
        Form form = (Form) page.getWidget();
        InputSelectTree ist = (InputSelectTree) ((StandardField) form.getComponent().getFieldsets()
                .get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(ist.getSrc(), is("InputSelectTree"));
        assertThat(ist.getParentFieldId(), is("testParentFieldId"));
        assertThat(ist.getHasChildrenFieldId(), is("testHasChildrenFieldId"));
        assertThat(ist.getCheckingStrategy(), is(CheckingStrategyEnum.child));
        assertThat(ist.getMaxTagCount(), is(5));
        assertThat(ist.getSize(), is(200));
        assertThat(ist.isHasCheckboxes(), is(true));
        assertThat(ist.getMaxTagTextLength(), is(20));
        assertThat(ist.getPlaceholder(), is("`select`"));
        assertThat(ist.getSearchMinLength(), is(2));
        assertThat(ist.getThrottleDelay(), is(100));

        ist = (InputSelectTree) ((StandardField) form.getComponent().getFieldsets()
                .get(0).getRows().get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(ist.getId(), is("defaults"));
        assertThat(ist.getParentFieldId(), is("testId"));
        assertThat(ist.getSearchMinLength(), is(0));
        assertThat(ist.getThrottleDelay(), is(300));
        assertThat(ist.isAjax(), is(false));
        assertThat(ist.isHasCheckboxes(), is(false));
        assertThat(ist.getCheckingStrategy(), is(CheckingStrategyEnum.all));
    }
}
