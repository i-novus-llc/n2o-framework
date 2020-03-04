package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oRating;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест компонента ввода и отображения рейтинга
 */
public class RatingAT extends AutoTestBase {

    private SimplePage page;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/rating/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        page = open(N2oSimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testRating() {
        N2oRating rating = page.single().widget(FormWidget.class).fields().field("Rating1")
                .control(N2oRating.class);
        rating.shouldExists();

        rating.shouldHaveValue("0");
        rating.val("3");
        rating.shouldHaveValue("3");
    }

    @Test
    public void testRatingWithHalf() {
        N2oRating rating = page.single().widget(FormWidget.class).fields().field("Rating2")
                .control(N2oRating.class);
        rating.shouldExists();

        rating.shouldHaveValue("3.5");
        rating.val("1.5");
        rating.shouldHaveValue("1.5");
    }
}
