package net.n2oapp.framework.config.reader.tools;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.global.view.action.control.Redirect;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;

/**
 * User: iryabov
 * Date: 03.07.13
 * Time: 13:22
 */
public class RedirectReaderV1 implements TypedElementReader<Redirect> {
    private static final RedirectReaderV1 instance = new RedirectReaderV1();

    public static RedirectReaderV1 getInstance() {
        return instance;
    }


    @Override
    public Redirect read(Element element) {
        if (element == null) return null;
        Redirect res = new Redirect();
        res.setActionId(ReaderJdomUtil.getAttributeString(element, "action-id"));
        res.setHrefFieldId(ReaderJdomUtil.getAttributeString(element, "href-field-id"));
        res.setTarget(ReaderJdomUtil.getAttributeEnum(element, "target", Target.class));
        return res;
    }

    @Override
    public Class<Redirect> getElementClass() {
        return Redirect.class;
    }

    @Override
    public String getElementName() {
        return "redirect";
    }
}
