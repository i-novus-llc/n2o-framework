package net.n2oapp.framework.config.register;

import java.util.function.Predicate;

/**
 * @author rgalina
 * @since 29.02.2016
 */
public class OriginPredicate {
    public static Predicate<XmlInfo> xml = i -> i.getOrigin().equals(Origin.xml);
}
