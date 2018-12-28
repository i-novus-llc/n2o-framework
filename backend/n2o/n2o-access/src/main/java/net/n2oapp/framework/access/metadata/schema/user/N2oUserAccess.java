package net.n2oapp.framework.access.metadata.schema.user;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

import java.io.Serializable;

/**
 * Точки доступа пользователя
 */

@Getter
@Setter
public class N2oUserAccess implements Serializable {

    private String id;
    private AccessPoint[] accessPoints;

}
