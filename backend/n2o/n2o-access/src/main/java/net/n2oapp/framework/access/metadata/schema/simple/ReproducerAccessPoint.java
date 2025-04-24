package net.n2oapp.framework.access.metadata.schema.simple;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.copy;

/**
 * Делает из одной точки доступа, с заданным через запятую параметром, несколько с одним
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReproducerAccessPoint {

    public static void reproduceAccessPoint(N2oObjectAccessPoint accessPoint, final List<AccessPoint> pointList, CompileProcessor p) {
        split(accessPoint, accessPoint.getAction(), N2oObjectAccessPoint::setAction, pointList);
    }

    public static void reproduceAccessPoint(N2oModuleAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getModule(), N2oModuleAccessPoint::setModule, pointList);
    }

    public static void reproduceAccessPoint(N2oPageAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getPage(), N2oPageAccessPoint::setPage, pointList);
    }

    public static void reproduceAccessPoint(N2oContainerAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getContainer(), N2oContainerAccessPoint::setContainer, pointList);
    }

    public static void reproduceAccessPoint(N2oMenuItemAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getMenuItem(), N2oMenuItemAccessPoint::setMenuItem, pointList);
    }


    public static void reproduceAccessPoint(N2oColumnAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getColumnId(), N2oColumnAccessPoint::setColumnId, pointList);
    }

    public static void reproduceAccessPoint(N2oFilterAccessPoint accessPoint, final List<AccessPoint> pointList){
        split(accessPoint, accessPoint.getFilterId(), N2oFilterAccessPoint::setFilterId, pointList);
    }

    private static <T extends AccessPoint> void split(T accessPoint, String val, BiConsumer<T, String> setter,
                                                      final List<AccessPoint> pointList) {
        if (val != null && val.contains(",")) {
            String[] split = val.replaceAll("\\s+", "").split(",");
            List<T> list = stream(split)
                    .map(str -> {
                        T point = copy(accessPoint);
                        setter.accept(point, str);
                        return point;
                    }).collect(Collectors.toList());
            pointList.remove(accessPoint);
            pointList.addAll(list);
        }
    }

}
