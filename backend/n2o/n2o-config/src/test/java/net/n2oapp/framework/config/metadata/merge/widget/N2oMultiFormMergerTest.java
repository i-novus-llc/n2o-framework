package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oMultiForm;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v5.MultiFormElementIOv5;
import net.n2oapp.framework.config.metadata.pack.N2oActionsIOV2Pack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsV5IOPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование слияния виджетов {@code <multi-form>}
 */
class N2oMultiFormMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oActionsIOV2Pack(), new N2oControlsV3IOPack(), new N2oFieldSetsV5IOPack())
                .ios(new MultiFormElementIOv5())
                .mergers(new N2oWidgetMerger<>(), new N2oMultiFormMerger());
    }

    @Test
    void testMergeMultiForm() {
        N2oMultiForm multiForm = merge(
                "net/n2oapp/framework/config/metadata/merge/widget/parentMultiForm.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childMultiForm.widget.xml")
                .get("parentMultiForm", N2oMultiForm.class);
        assertThat(multiForm, notNullValue());

        assertThat(multiForm.getPagination(), notNullValue());
        assertThat(multiForm.getPagination().getClassName(), is("childPagination"));
        assertThat(multiForm.getPagination().getNext(), is(false));
        assertThat(multiForm.getPagination().getPrev(), is(false));

        assertThat(multiForm.getToolbars().length, is(2));
        assertThat(multiForm.getToolbars()[0].getPlace(), is("topRight"));
        assertThat(multiForm.getToolbars()[1].getPlace(), is("bottomRight"));

        assertThat(multiForm.getFetchOnInit(), is(true));
        assertThat(multiForm.getAutoFocus(), is(false));

        assertThat(multiForm.getDatasource(), notNullValue());
        assertThat(multiForm.getDatasource().getQueryId(), is("parentQuery"));
        assertThat(multiForm.getDatasource().getObjectId(), is("parentObject"));

        assertThat(multiForm.getForm(), notNullValue());
        assertThat(multiForm.getForm().getItems().length, is(2));
        assertThat(multiForm.getUnsavedDataPrompt(), is(true));
    }
}