package net.n2oapp.framework.autotest.impl.component.modal;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.modal.ImagePreviewModal;

public class N2oImagePreviewModal extends N2oModal implements ImagePreviewModal {

    @Override
    public void close() {
        element().$(".n2o-image-uploader__modal--body .n2o-image-uploader__modal--icon-close").click();
    }

    @Override
    public void imageLink(String link) {
        element().parent().$(".n2o-image-uploader__modal--body .n2o-image-uploader__modal--image").shouldHave(Condition.attribute("src", link));
    }

}