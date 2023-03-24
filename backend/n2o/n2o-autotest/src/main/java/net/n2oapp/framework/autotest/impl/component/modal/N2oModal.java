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

    private static final String MODAL_BODY = ".modal-body";
    private static final String MODAL_DIALOG_SCROLLABLE = ".modal-dialog-scrollable";
    private static final String MODAL_HEADER = ".modal-header";

    @Override
    public void shouldHaveTitle(String text) {
        element().$(".modal-header .modal-title, .white-space-pre-line")
                .shouldHave(Condition.text(text));
    }

    @Override
    public void shouldNotHaveHeader() {
        element().$(MODAL_HEADER).shouldNotBe(Condition.exist);
    }

    @Override
    public ModalToolbar toolbar() {
        return new N2oModalToolbar();
    }

    @Override
    public <T extends Page> T content(Class<T> pageClass) {
        return N2oSelenide.component(element().$(MODAL_BODY), pageClass);
    }

    @Override
    public void scrollUp() {
        Selenide.executeJavaScript(String.format("document.querySelector('%s').scrollTop = 0", MODAL_BODY));
    }

    @Override
    public void scrollDown() {
        Selenide.executeJavaScript(String.format("document.querySelector('%s').scrollTop = document.querySelector('%s').scrollHeight", MODAL_BODY, MODAL_BODY));
    }

    @Override
    public void shouldBeScrollable() {
        element().$(String.format("%s %s", MODAL_DIALOG_SCROLLABLE, MODAL_BODY)).should(Condition.exist);
    }

    @Override
    public void shouldNotBeScrollable() {
        element().$(String.format("%s %s", MODAL_DIALOG_SCROLLABLE, MODAL_BODY)).shouldNot(Condition.exist);
    }

    @Override
    public void close() {
        element().$(String.format("%s [aria-label=\"Close\"]", MODAL_HEADER)).click();
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
            return N2oSelenide.collection(element().$$(".modal-footer .n2o-modal-actions .toolbar_placement_bottomLeft .btn"), Toolbar.class);
        }

        @Override
        public Toolbar bottomRight() {
            return N2oSelenide.collection(element().$$(".modal-footer .n2o-modal-actions .toolbar_placement_bottomRight .btn"), Toolbar.class);
        }
    }

}
