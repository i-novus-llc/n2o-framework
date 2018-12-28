package net.n2oapp.framework.ui.rs;

import java.util.ArrayList;
import java.util.List;

/**
 * Do not cache on client!
 *
 * @author esadykov
 * @since 24.01.13 15:56
 */
public class PageInfo {
    private List<String> templates = new ArrayList<String>();
    private List<String> scripts = new ArrayList<String>();
    private List<String> styles = new ArrayList<String>();


    public List<String> getScripts() {
        return scripts;
    }

    public List<String> getStyles() {
        return styles;
    }

    public List<String> getTemplates() {
        return templates;
    }
}
