package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;

/**
 * Исключение, когда страница не найдена
 */
public class PageNotFoundException extends ReferentialIntegrityViolationException {

    public PageNotFoundException(String pageId) {
        super(pageId, N2oPage.class);
        setUserMessage("n2o.pageNotFound");
        setHttpStatus(404);
    }

}
