package net.n2oapp.framework.mvc.callback.jsp;

import java.io.Serializable;
import java.util.Map;

/**
 * @author iryabov
 * @since 24.03.2016
 */
public class ModelAndView implements Serializable {
    private String view;
    private Map<String, Object> model;

    public ModelAndView(Map<String, Object> model, String view) {
        this.view = view;
        this.model = model;
    }

    public String getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
