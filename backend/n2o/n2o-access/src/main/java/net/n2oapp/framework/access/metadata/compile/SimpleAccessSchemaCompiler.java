package net.n2oapp.framework.access.metadata.compile;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.simple.N2oSimpleAccessSchema;
import net.n2oapp.framework.access.metadata.schema.simple.ReproducerAccessPoint;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static net.n2oapp.framework.access.functions.StreamUtil.safeStreamOf;

/**
 * Компиляции простой схемы прав доступа
 */
@Component
public class SimpleAccessSchemaCompiler extends  AccessSchemaCompiler<SimpleCompiledAccessSchema, N2oSimpleAccessSchema> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public SimpleCompiledAccessSchema compile(N2oSimpleAccessSchema source, AccessContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema compiled = new SimpleCompiledAccessSchema();
        compileAccess(compiled, source, context, p);
        if (source.getN2oPermissions() != null) {
            reproduce(source.getN2oPermissions(), N2oPermission::getAccessPoints, N2oPermission::setAccessPoints, p);
            compiled.setN2oPermissions(asList(source.getN2oPermissions()));
        }
        if (source.getN2oRoles() != null) {
            reproduce(source.getN2oRoles(), N2oRole::getAccessPoints, N2oRole::setAccessPoints, p);
            compiled.setN2oRoles(asList(source.getN2oRoles()));
        }
        if (source.getPermitAllPoints() != null) {
            compiled.setPermitAllPoints(reproduceAccessPoints(source.getPermitAllPoints(), p));
        }
        if (source.getAuthenticatedPoints() != null) {
            compiled.setAuthenticatedPoints(reproduceAccessPoints(source.getAuthenticatedPoints(), p));
        }
        if (source.getN2oUserAccesses() != null) {
            reproduce(source.getN2oUserAccesses(), N2oUserAccess::getAccessPoints, N2oUserAccess::setAccessPoints, p);
            compiled.setN2oUserAccesses(asList(source.getN2oUserAccesses()));
        }
        if (source.getAnonymousPoints() != null) {
            compiled.setAnonymousPoints(reproduceAccessPoints(source.getAnonymousPoints(), p));
        }

        return compiled;
    }

    private <T> void reproduce(T[] val, Function<T, AccessPoint[]> getter, BiConsumer<T, AccessPoint[]> setter, CompileProcessor p) {
        safeStreamOf(val).forEach(v -> {
                    List<AccessPoint> accessPoints = reproduceAccessPoints(getter.apply(v), p);
                    setter.accept(v, accessPoints.toArray(new AccessPoint[accessPoints.size()]));
                });
    }

    private List<AccessPoint> reproduceAccessPoints(AccessPoint[] points, CompileProcessor p) { //todo return array
        if (points == null) {
            return Collections.emptyList();
        }
        List<AccessPoint> accessPoints = new ArrayList<>(Arrays.asList(points));
        stream(points).forEach(ac -> reproduceAccessPoint(ac, accessPoints, p));
        return accessPoints;
    }

    private void reproduceAccessPoint(AccessPoint accessPoint, final List<AccessPoint> pointList, CompileProcessor p) {
        if (accessPoint instanceof N2oObjectAccessPoint ap) {
            ReproducerAccessPoint.reproduceAccessPoint(ap, pointList, p);
        } else if (accessPoint instanceof N2oModuleAccessPoint ap) {
            ReproducerAccessPoint.reproduceAccessPoint(ap, pointList);
        } else if (accessPoint instanceof N2oPageAccessPoint ap) {
            ReproducerAccessPoint.reproduceAccessPoint(ap, pointList);
        } else if (accessPoint instanceof N2oContainerAccessPoint ap) {
            ReproducerAccessPoint.reproduceAccessPoint(ap, pointList);
        } else if (accessPoint instanceof N2oMenuItemAccessPoint ap) {
            ReproducerAccessPoint.reproduceAccessPoint(ap, pointList);
        } else if (accessPoint instanceof N2oColumnAccessPoint ap) {
            ReproducerAccessPoint.reproduceAccessPoint(ap, pointList);
        } else if (accessPoint instanceof N2oFilterAccessPoint ap) {
            ReproducerAccessPoint.reproduceAccessPoint(ap, pointList);
        }
    }
}
