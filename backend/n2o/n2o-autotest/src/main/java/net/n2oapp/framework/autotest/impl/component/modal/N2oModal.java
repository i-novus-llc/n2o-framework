package net.n2oapp.framework.autotest.impl.component.modal;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oModal extends N2oComponent implements Modal {

    @Override
    public void shouldHaveTitle(String text) {
        element().$(".modal-header .modal-title")
                .shouldHave(Condition.text(text));
    }

    @Override
    public void shouldNotHaveHeader() {
        element().$(".modal-header").shouldNotBe(Condition.exist);
    }

    @Override
    public ModalToolbar toolbar() {
        return new N2oModalToolbar();
    }

    @Override
    public <T extends Page> T content(Class<T> pageClass) {
        return N2oSelenide.component(element().$(".modal-body"), pageClass);
    }

    @Override
    public void scrollUp() {
        Selenide.executeJavaScript("document.querySelector('.modal-body').scrollTop = 0");
    }

    @Override
    public void scrollDown() {
        Selenide.executeJavaScript("document.querySelector('.modal-body').scrollTop = document.querySelector('.modal-body').scrollHeight");
    }

    @Override
    public void shouldBeScrollable() {
        element().$(".modal-dialog-scrollable .modal-body").should(Condition.exist);
    }

    @Override
    public void shouldNotBeScrollable() {
        element().$(".modal-dialog-scrollable .modal-body").shouldNot(Condition.exist);
    }

    @Override
    public void close() {
        element().$(".modal-header [aria-label=\"Close\"]").click();
    }

    @Override
    public void closeByEsc() {
        element().parent().parent().pressEscape();
    }

    @Override
    public void clickBackdrop() {
        int widthOffset = element().getSize().getWidth() / 2 + 10;
        element().click(ClickOptions.usingDefaultMethod().offsetX(widthOffset));
    }

    public class N2oModalToolbar implements ModalToolbar {

        @Override
        public Toolbar bottomLeft() {
            return N2oSelenide.collection(element().$$(".modal-footer .n2o-modal-actions").first().$$(".btn"), Toolbar.class);
        }

        @Override
        public Toolbar bottomRight() {
            return N2oSelenide.collection(element().$$(".modal-footer .n2o-modal-actions").last().$$(".btn"), Toolbar.class);
        }
    }

}
