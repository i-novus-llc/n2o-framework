package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;

/**
 * Контекст подстраницы, используемой в регионе `<sub-page>`
 */
@Getter
@Setter
public class SubPageContext extends PageContext {

    /**
     * url страницы как в браузерной строке
     * для sub-page отличается от route для получения метаданной
     */
    String subPageRoute;


    public SubPageContext(String sourcePageId, String route) {
        super(sourcePageId, route);
    }
}
