package net.n2oapp.framework.autotest.impl.component.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.page.TopLeftRightPage;
import net.n2oapp.framework.autotest.impl.collection.N2oRegions;

import static net.n2oapp.framework.autotest.N2oSelenide.collection;

/**
 * Страница с тремя регионами для автотестирования
 */
public class N2oTopLeftRightPage extends N2oPage implements TopLeftRightPage {

    private static final String REGION = "div.n2o-page__%s .n2o-page__fixed-container > div";

    @Override
    public Regions top() {
        return collection(element().$$(String.format(REGION, "top")), N2oRegions.class);
    }

    @Override
    public Regions left() {
        return collection(element().$$(String.format(REGION, "left")), N2oRegions.class);
    }

    @Override
    public Regions right() {
        return collection(element().$$(String.format(REGION, "right")), N2oRegions.class);
    }

    @Override
    public void shouldHaveScrollToTopButton() {
        scrollToTopButton().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveScrollToTopButton() {
        scrollToTopButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void clickScrollToTopButton() {
        scrollToTopButton().click();
    }

    protected SelenideElement scrollToTopButton() {
        return element().$(".n2o-page__scroll-to-top--show");
    }
}
