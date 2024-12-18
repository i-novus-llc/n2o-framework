package net.n2oapp.framework.config.metadata.compile.context;

/**
 * Контекст подстраницы, используемой в регионе `<sub-page>`
 */
public class SubPageContext extends PageContext {

    public SubPageContext(String sourcePageId, String route) {
        super(sourcePageId, route);
    }
}
