package net.n2oapp.framework.access.metadata.accesspoint.reader;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;


@Component
public class N2oObjectAccessPointReader extends AbstractN2oAccessPointReader<N2oObjectAccessPoint> {
    @Override
    public N2oObjectAccessPoint read(Element element) {
        N2oObjectAccessPoint res = new N2oObjectAccessPoint();
        String actions = ReaderJdomUtil.getAttributeString(element, "actions");
        if (actions != null)
            res.setAction(actions);
        res.setObjectId(ReaderJdomUtil.getAttributeString(element, "object-id"));
        res.setAccessFilters((ReaderJdomUtil.getChilds(element, element.getNamespace(), "filter", new TypedElementReader<N2oPreFilter>() {
            @Override
            public String getElementName() {
                return "filter";
            }

            @Override
            public N2oPreFilter read(Element element) {
                N2oPreFilter res = new N2oPreFilter();
                res.setFieldId(ReaderJdomUtil.getAttributeString(element, "field-id"));
                res.setType(ReaderJdomUtil.getAttributeEnum(element, "type", FilterType.class));
                res.setValueAttr(ReaderJdomUtil.getAttributeString(element, "value"));
                res.setValues(ReaderJdomUtil.getChildren(element, null, null, new TypedElementReader<String>() {
                    @Override
                    public String getElementName() {
                        return "value";
                    }

                    @Override
                    public String read(Element element) {
                        return ReaderJdomUtil.getElementText(element);
                    }

                    @Override
                    public Class<String> getElementClass() {
                        return String.class;
                    }
                }));

                return res;
            }

            @Override
            public Class<N2oPreFilter> getElementClass() {
                return N2oPreFilter.class;
            }
        })));
        res.setNamespaceUri(getNamespaceUri());
        return res;
    }

    @Override
    public Class<N2oObjectAccessPoint> getElementClass() {
        return N2oObjectAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "object-access";
    }
}
