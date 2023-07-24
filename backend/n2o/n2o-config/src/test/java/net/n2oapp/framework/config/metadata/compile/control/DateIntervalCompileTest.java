package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.DateInterval;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateIntervalCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new DateIntervalCompiler());
    }

    @Test
    void tabsDateInterval() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/mapping/testDateInterval.page.xml").get(new PageContext("testDateInterval"));
        Form form = (Form) page.getWidget();
        StandardField standardField = (StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(standardField.getLabel(), is("Date-interval"));
        DateInterval dateInterval = (DateInterval) standardField.getControl();
        assertThat(dateInterval.getId(), is("test"));
        assertThat(dateInterval.getDateFormat(), is("DD/MM/YYYY"));
        assertThat(dateInterval.getTimeFormat(), is("HH:mm:ss"));
        assertThat(dateInterval.getMin(), is("2012-12-05T00:00:00"));
        assertThat(dateInterval.getMax(), is("2021-12-15T00:00:00"));
        assertThat(dateInterval.getPlaceholder(), is("Введите дату"));
        assertThat(dateInterval.getUtc(), is(false));
    }
}
