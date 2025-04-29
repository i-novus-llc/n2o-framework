package net.n2oapp.framework.config.register;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

/**
 * @author rgalina
 * @since 29.02.2016
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OriginPredicate {
    public static Predicate<XmlInfo> xml = i -> i.getOrigin().equals(OriginEnum.xml);
}
