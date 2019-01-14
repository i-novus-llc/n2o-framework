package net.n2oapp.framework.access.metadata.schema.simple;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.config.metadata.validation.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.copy;
import static net.n2oapp.framework.access.functions.StreamUtil.safeStreamOf;

/**
 * Делает из одной точки доступа, с заданным через запятую параметром, несколько с одним
 */
public class ReproducerAccessPoint {

    public static void reproduceAccessPoint(N2oObjectAccessPoint accessPoint, final List<AccessPoint> pointList, CompileProcessor p) {
        split(accessPoint, accessPoint.getAction(), N2oObjectAccessPoint::setAction, pointList);
        if ("*".equals(accessPoint.getAction())) {
            if (accessPoint.getObjectId().contains("*")) {
                N2oObjectAccessPoint point = copy(accessPoint);
                point.setAction("update");
                pointList.add(point);
                point = copy(accessPoint);
                point.setAction("create");
                pointList.add(point);
                point = copy(accessPoint);
                point.setAction("delete");
                pointList.add(point);
            } else {
                collectAll(accessPoint, accessPoint.getAction(), () -> p.getSource(accessPoint.getObjectId(), N2oObject.class),
                        r -> safeStreamOf(r.getOperations()).map(N2oObject.Operation::getId), N2oObjectAccessPoint::setAction, pointList);
            }
        }

        boolean hasRead = pointList.stream()
                .filter(N2oObjectAccessPoint.class::isInstance)
                .map(N2oObjectAccessPoint.class::cast)
                .anyMatch(ap -> ap.getObjectId().equals(accessPoint.getObjectId())
                        && ap.getAction() != null && ap.getAction().equals("read"));
        if (!hasRead && (accessPoint.getAction() == null || accessPoint.getAction().isEmpty() || accessPoint.getAction().equals("*"))) {
            accessPoint.setAction("read");
        }
    }

    public static void reproduceAccessPoint(N2oModuleAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getModule(), N2oModuleAccessPoint::setModule, pointList);
    }

    public static void reproduceAccessPoint(N2oPageAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getPage(), N2oPageAccessPoint::setPage, pointList);
    }

    public static void reproduceAccessPoint(N2oContainerAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getContainer(), N2oContainerAccessPoint::setContainer, pointList);
        collectAll(accessPoint, accessPoint.getContainer(), () -> ValidationUtil.getOrNull(accessPoint.getPage(), N2oPage.class),
                r -> safeStreamOf(r.getContainers()).map(N2oWidget::getId),
                N2oContainerAccessPoint::setContainer, pointList);
    }

    public static void reproduceAccessPoint(N2oMenuItemAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getMenuItem(), N2oMenuItemAccessPoint::setMenuItem, pointList);
    }


    public static void reproduceAccessPoint(N2oColumnAccessPoint accessPoint, final List<AccessPoint> pointList) {
        split(accessPoint, accessPoint.getColumnId(), N2oColumnAccessPoint::setColumnId, pointList);
        collectAll(accessPoint, accessPoint.getColumnId(), () -> ValidationUtil.getOrNull(accessPoint.getPageId(), N2oPage.class),
                page -> safeStreamOf(page.getContainers()).filter(container -> container.getId().equals(accessPoint.getContainerId()))
                        .findFirst().map(N2oTable.class::cast)
                        .map(N2oTable::getColumns).flatMap(columns -> Optional.of(safeStreamOf(columns).map(AbstractColumn::getId)))
                        .orElse(Stream.empty()), N2oColumnAccessPoint::setColumnId, pointList);
    }

    public static void reproduceAccessPoint(N2oFilterAccessPoint accessPoint, final List<AccessPoint> pointList){
        split(accessPoint, accessPoint.getFilterId(), N2oFilterAccessPoint::setFilterId, pointList);
//        collectAll(accessPoint, accessPoint.getFilterId(), () -> CompilerHolder.get().get(accessPoint.getQueryId(), CompiledQuery.class),
//                compiledQuery -> safeStreamOf(compiledQuery.getFilterFields()), N2oFilterAccessPoint::setFilterId, pointList);
        //todo:закоментировано поскольку изменится компиляция
    }

    public static <T extends AccessPoint> void split(T accessPoint, String val, BiConsumer<T, String> setter,
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

    public static <T extends AccessPoint, R> void collectAll(T accessPoint, String val,
                                                              Supplier<R> compiler, Function<R, Stream<String>> getter,
                                                              BiConsumer<T, String> setter, final List<AccessPoint> pointList) {
        if (val != null && val.equals("*")) {
            R r = compiler.get();
            if (r != null) {
                List<T> points = getter.apply(r)
                        .map(str -> {
                            T point = copy(accessPoint);
                            setter.accept(point, str);
                            return point;
                        }).collect(Collectors.toList());
                pointList.addAll(points);
            }
        }
    }

}
