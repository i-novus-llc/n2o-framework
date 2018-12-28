package net.n2oapp.framework.config.util;

import org.jdom.output.Format;

/**
 * @author operehod
 * @since 23.04.2015
 */
public class XmlUtil {

    public static final Format N2O_FORMAT;

    static {
        N2O_FORMAT = Format.getRawFormat();
        N2O_FORMAT.setIndent("    ");
    }


}
