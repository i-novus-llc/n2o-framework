package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.Rating;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест компонента ввода и отображения рейтинга
 */
public class RatingAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/rating/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testRating() {
        Rating rating = page.widget(FormWidget.class).fields().field("Rating1")
                .control(Rating.class);
        rating.shouldExists();

        rating.shouldHaveValue("0");
        rating.val("3");
        rating.shouldHaveValue("3");
    }

    @Test
    public void testRatingWithHalf() {
        Rating rating = page.widget(FormWidget.class).fields().field("Rating2")
                .control(Rating.class);
        rating.shouldExists();

        rating.shouldHaveValue("3.5");
        rating.val("1.5");
        rating.shouldHaveValue("1.5");
    }
}
