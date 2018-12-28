package net.n2oapp.framework.standard.header.reader;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import net.n2oapp.framework.standard.header.model.global.context.UserContextStructure;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * User: operhod
 * Date: 19.05.14
 * Time: 16:03
 */
public class UserContextStructureReader implements TypedElementReader<UserContextStructure> {

    private static UserContextStructureReader instance = new UserContextStructureReader();

    public static UserContextStructureReader getInstance() {
        return instance;
    }

    private UserContextStructureReader() {
    }

    @Override
    public UserContextStructure read(Element element) {
        if (element == null) return null;
        UserContextStructure res = new UserContextStructure();
        res.setSrc(getAttributeString(element, "src"));
        res.setQueryId(getAttributeString(element, "query-id"));
        res.setUserFieldId(getAttributeString(element, "username-field-id"));
        Element position = element.getChild("position", element.getNamespace());
        if (position != null) {
            UserContextStructure.Position p = new UserContextStructure.Position();
            p.setHintFieldId(getAttributeString(position, "hint-field-id"));
            p.setLabelFieldId(getAttributeString(position, "label-field-id"));
            p.setValueFieldId(getAttributeString(position, "value-field-id"));
            res.setPosition(p);
        }
        Element orgStructure = element.getChild("org-structure", element.getNamespace());
        if (orgStructure != null)
            res.setUnits(ReaderJdomUtil.getChilds(orgStructure, element.getNamespace(), "unit", UnitReader.instance));
        return res;
    }

    @Override
    public Class<UserContextStructure> getElementClass() {
        return UserContextStructure.class;
    }

    @Override
    public String getElementName() {
        return "user-context";
    }

    private static class UnitReader implements TypedElementReader<UserContextStructure.Unit> {

        private static UnitReader instance = new UnitReader();

        @Override
        public UserContextStructure.Unit read(Element element) {
            UserContextStructure.Unit res = new UserContextStructure.Unit();
            res.setLabelFieldId(getAttributeString(element, "label-field-id"));
            res.setValueFieldId(getAttributeString(element, "value-field-id"));
            res.setUnits(ReaderJdomUtil.getChilds(element, element.getNamespace(), "unit", this));
            return res;
        }

        @Override
        public Class<UserContextStructure.Unit> getElementClass() {
            return UserContextStructure.Unit.class;
        }

        @Override
        public String getElementName() {
            return "unit";
        }
    }

}
