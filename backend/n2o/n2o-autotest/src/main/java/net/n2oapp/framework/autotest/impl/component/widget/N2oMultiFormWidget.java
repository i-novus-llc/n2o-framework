package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.MultiFormWidget;
import net.n2oapp.framework.autotest.api.component.widget.Paging;

/**
 * Виджет - мульти-форма для автотестирования
 */
public class N2oMultiFormWidget extends N2oStandardWidget implements MultiFormWidget {

    private static final String FORM = ".n2o-fieldset";

    @Override
    public void shouldHaveSize(int size) {
        form().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public FormWidget form(int index) {
        N2oFormWidget formWidget = new N2oFormWidget();
        formWidget.setElement(form().get(index));
        return formWidget;
    }

    @Override
    public Paging paging() {
        return new N2oPaging(element());
    }

    private ElementsCollection form() {
        return element().$$(FORM);
    }
}
