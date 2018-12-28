package net.n2oapp.framework.access.metadata.accesspoint.io;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;

/**
 * Абстракция для IO наследников точки доступа AccessPoint v2.0
 * @param <T> - наследник AccessPoint
 */
public abstract class AccessPointElementIOv2<T extends AccessPoint> implements NamespaceIO<T>, AccessPointIOv2 {

}
