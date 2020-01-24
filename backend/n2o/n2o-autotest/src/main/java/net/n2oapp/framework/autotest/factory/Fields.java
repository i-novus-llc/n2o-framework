package net.n2oapp.framework.autotest.factory;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.component.field.Field;
import net.n2oapp.framework.autotest.component.field.StandardField;
import net.n2oapp.framework.autotest.impl.N2oComponentsCollection;

public class Fields extends N2oComponentsCollection {

    public Fields(ElementsCollection elements, ComponentFactory factory) {
        super(elements, factory);
    }

    public Rows row(int index) {
        return null;
    }

    public StandardField field(String label) {
        return null;
    }

    public StandardField field(Condition by) {
        return null;
    }


    public <T extends Field> T field(String label, Class<T> componentClass) {
        return null;
    }

    public <T extends Field> T field(Condition by, Class<T> componentClass) {
        return null;
    }

    public static class Rows {
        public Fields col(int index) {
            return null;
        }
    }
}
