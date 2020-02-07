package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.*;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Тестирвоание компиляции страницы с левыми и правыми регионами
 */
public class LeftRightPageCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utObjectField.object.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/page/utObjectField.page.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"));
    }

    @Test
    public void leftRightPage() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testLeftRightPage.page.xml")
                .get(new PageContext("testLeftRightPage"));

        List<Region> right = page.getRegions().get("right");
        List<Region> left = page.getRegions().get("left");

        assertThat(page.getRegions().size(), is(2));
        assertThat(right.size(), is(3));
        assertThat(left.size(), is(1));
        assertThat(page.getWidth().getLeft(), is("70%"));
        assertThat(page.getWidth().getRight(), nullValue());
        assertThat(left.get(0).getClass(), CoreMatchers.is(equalTo(LineRegion.class)));
        assertThat(right.get(0).getClass(), CoreMatchers.is(equalTo(PanelRegion.class)));
        assertThat(right.get(1).getClass(), CoreMatchers.is(equalTo(TabsRegion.class)));
        assertThat(right.get(2).getClass(), CoreMatchers.is(equalTo(CustomRegion.class)));
    }
}
