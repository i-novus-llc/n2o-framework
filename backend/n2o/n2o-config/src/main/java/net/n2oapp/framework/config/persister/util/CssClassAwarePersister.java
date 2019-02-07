package net.n2oapp.framework.config.persister.util;

import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import org.jdom.Element;

/**
 * User: operhod
 * Date: 22.04.14
 * Time: 16:56
 */
public class CssClassAwarePersister {

    private static CssClassAwarePersister instance = new CssClassAwarePersister();

    public static CssClassAwarePersister getInstance() {
        return instance;
    }

    public void persist(Element element, CssClassAware cssClassAware) {
        PersisterJdomUtil.setAttribute(element, "css-class", cssClassAware.getCssClass());
    }

}
