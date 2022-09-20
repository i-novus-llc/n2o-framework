package net.n2oapp.framework.access.metadata.schema.user;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Точки доступа пользователя
 */

@Getter
@Setter
public class N2oUserAccess implements Source {

    private String id;
    private AccessPoint[] accessPoints;

}
