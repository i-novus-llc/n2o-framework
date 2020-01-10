package net.n2oapp.framework.config.reader.tools;

import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import org.jdom.Element;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;

/**
 * User: operhod
 * Date: 22.04.14
 * Time: 16:56
 */
public class CssClassAwareReader {

    private static CssClassAwareReader instance = new CssClassAwareReader();

    public static CssClassAwareReader getInstance() {
        return instance;
    }

    public void read(CssClassAware cssClassAware, Element element) {
        cssClassAware.setCssClass(ReaderJdomUtil.getAttributeString(element, "css-class"));
    }

}
