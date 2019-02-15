package net.n2oapp.framework.api.metadata.local.view.widget.control;

import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.interval.N2oIntervalField;
import net.n2oapp.framework.api.metadata.control.plain.N2oPlainField;

import java.util.*;

/**
 * User: operhod
 * Date: 06.06.14
 * Time: 15:25
 */
public class ControlModelBinder {
    private Collection<N2oStandardField> controls;

    public ControlModelBinder(Collection<N2oStandardField> controls) {
        this.controls = controls;
    }


    //работает только для простых полей типа id, gender.id
    //не работает для gender
    public List<N2oStandardField> bind(String modelField) {
        List<N2oStandardField> res = new ArrayList<>();
        bindPlainControls(modelField, res);
        bindComplexControls(modelField, res);
        return res;
    }


    private void bindPlainControls(String modelField, List<N2oStandardField> res) {
        for (N2oStandardField field : controls) {
            if (field instanceof N2oPlainField && field.getId().equals(modelField))
                res.add(field);
        }
    }

    private void bindComplexControls(String modelField, List<N2oStandardField> res) {
        String[] tmp = modelField.split("\\.");
        if (tmp.length <= 1) return;
        for (int i = 0; i < tmp.length - 1; i++) {
            findListControl(
                    res,
                    trim(toString(Arrays.copyOfRange(tmp, 0, i + 1))));
        }

    }

    private String trim(String s) {
        if (s.endsWith("*"))
            return s.substring(0, s.length() - 1);
        return s;
    }

    private void findListControl(List<N2oStandardField> res, String controlId) {
        for (N2oStandardField field : controls) {
            if (field.getId().equals(controlId) && (field instanceof N2oListField || field instanceof N2oIntervalField))
                res.add(field);
        }
    }

    private String toString(String[] array) {
        StringBuilder res = new StringBuilder();
        boolean begin = true;
        for (int i = 0; i < array.length; i++) {
            if (!begin) res.append('.');
            res.append(array[i]);
            begin = false;
        }
        return res.toString();
    }
}
