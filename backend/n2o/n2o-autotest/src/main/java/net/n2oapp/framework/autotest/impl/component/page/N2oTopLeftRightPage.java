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
    @Override
    public Regions top() {
        return collection(element().$$("div.n2o-page__top .n2o-page__fixed-container > div"), N2oRegions.class);
    }

    @Override
    public Regions left() {
        return collection(element().$$("div.n2o-page__left .n2o-page__fixed-container > div"), N2oRegions.class);
    }

    @Override
    public Regions right() {
        return collection(element().$$("div.n2o-page__right .n2o-page__fixed-container > div"), N2oRegions.class);
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
