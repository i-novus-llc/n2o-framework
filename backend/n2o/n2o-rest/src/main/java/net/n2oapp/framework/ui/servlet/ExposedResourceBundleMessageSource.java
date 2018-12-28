package net.n2oapp.framework.ui.servlet;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class ExposedResourceBundleMessageSource extends ResourceBundleMessageSource {
    public Set<String> getKeys(String basename, Locale locale) {
        ResourceBundle bundle = getResourceBundle(basename, locale);
        if (bundle == null) return Collections.EMPTY_SET;
        return bundle.keySet();
    }
}
