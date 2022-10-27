package net.n2oapp.framework.access.metadata.schema.permission;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Точки доступа права
 */
@Getter
@Setter
public class N2oPermission implements Source {

    private String id;
    private String name;
    private AccessPoint[] accessPoints;

}
