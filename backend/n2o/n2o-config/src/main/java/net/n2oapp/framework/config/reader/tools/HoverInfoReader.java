package net.n2oapp.framework.config.reader.tools;

import net.n2oapp.framework.api.metadata.global.view.action.control.HoverInfo;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

/*
 /* @author enuzhdina 
 /* @since 14.01.2016
 */
public class HoverInfoReader implements TypedElementReader<HoverInfo> {

    private static HoverInfoReader instance = new HoverInfoReader();

    public static HoverInfoReader getInstance() {
        return instance;
    }

    @Override
    public HoverInfo read(Element element) {
        if (element == null)
            return null;
        HoverInfo hoverInfo = new HoverInfo();
        hoverInfo.setSrc(getAttributeString(element, "src"));
        hoverInfo.setElements(getChilds(element, element.getNamespace(), "element",
                new TypedElementReader<HoverInfo.Element>() {
                    @Override
                    public String getElementName() {
                        return "element";
                    }

                    @Override
                    public HoverInfo.Element read(Element element) {
                        HoverInfo.Element hiElement = new HoverInfo.Element();
                        hiElement.setLabel(getAttributeString(element, "label"));
                        hiElement.setFieldId(getAttributeString(element, "field-id"));
                        return hiElement;
                    }

                    @Override
                    public Class<HoverInfo.Element> getElementClass() {
                        return HoverInfo.Element.class;
                    }
                }));
        return hoverInfo;
    }

    @Override
    public Class<HoverInfo> getElementClass() {
        return HoverInfo.class;
    }

    @Override
    public String getElementName() {
        return "hover-info";
    }
}
