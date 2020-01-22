package net.n2oapp.framework.autotest.factory;

import net.n2oapp.framework.autotest.N2oSelector;
import net.n2oapp.framework.autotest.component.field.Field;
import net.n2oapp.framework.autotest.component.field.StandardField;

public class Fields {

    public Rows row(int index) {
        return null;
    }

    public StandardField field(String label) {
        return null;
    }

    public StandardField field(N2oSelector by) {
        return null;
    }


    public <T extends Field> T field(String label, Class<T> componentClass) {
        return null;
    }

    public <T extends Field> T field(N2oSelector by, Class<T> componentClass) {
        return null;
    }

    public static class Rows {
        public Fields col(int index) {
            return null;
        }
    }
}
