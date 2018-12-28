package net.n2oapp.framework.config.io;

import net.n2oapp.framework.api.metadata.io.ElementIO;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;

import java.util.Map;

/**
 * Чтение\запись списка настроек
 */
public class PropertiesElementIO implements ElementIO<Map<String, Object>> {


    @Override
    public void io(Element e, Map<String, Object> t, IOProcessor p) {

    }
}
