package net.n2oapp.framework.config.metadata.compile.context;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public abstract class BaseCompileContext<D extends Compiled, S> implements CompileContext<D, S> {
    /**
     * Идентификатор исходной метаданной
     */
    private String sourceId;
    /**
     * Класс исходной метаданной
     */
    private Class<S> sourceClass;
    /**
     * Класс собранной метаданной
     */
    private Class<D> compiledClass;
    /**
     * Маршрут, по которому можно получить метаданную
     */
    private String route;
    /**
     * Связь query параметров в маршруте с моделями данных
     */
    private Map<String, ModelLink> queryRouteMapping;

    /**
     * Связь path параметров в маршруте с моделями данных
     */
    private Map<String, ModelLink> pathRouteMapping;

    /**
     * Ссылка на модель данных родителя
     */
    private ModelLink parentModelLink;

    public BaseCompileContext(String sourceId, Class<S> sourceClass, Class<D> compiledClass) {
        if (sourceId == null)
            throw new IllegalArgumentException("SourceId must not be null");
        if (sourceClass == null && compiledClass == null)
            throw new IllegalArgumentException("SourceClass or CompiledClass must not be null");
        this.sourceId = sourceId;
        this.sourceClass = sourceClass;
        this.compiledClass = compiledClass;
    }

    public BaseCompileContext(String route, String sourceId, Class<S> sourceClass, Class<D> compiledClass) {
        this(sourceId, sourceClass, compiledClass);
        if (route == null)
            throw new IllegalArgumentException("Route must not be null");
        this.route = route;
    }

    public BaseCompileContext(BaseCompileContext<D, S> context, CompileProcessor p) {
        this(context.sourceId, context.sourceClass, context.compiledClass);
        if (context.route != null) {
            this.route = context.getRoute(p);
        }
        this.pathRouteMapping = context.pathRouteMapping;
        this.queryRouteMapping = context.queryRouteMapping;
        this.parentModelLink = context.parentModelLink;
    }

    @Override
    public String getCompiledId(CompileProcessor p) {
        if (route != null) {
            String url = getRoute(p);
            return RouteUtil.convertPathToId(url);
        }
        if (StringUtils.hasLink(sourceId) && p != null) {
            return p.resolveText(sourceId, parentModelLink);
        }
        return sourceId;
    }

    private void checkProcessor(CompileProcessor p) {
        if (p == null) {
            throw new IllegalArgumentException("You try to get CompiledId for dynamic metadata without CompileProcessor!");
        }
    }

    @Override
    public String getSourceId(CompileProcessor p) {
        if (StringUtils.hasLink(sourceId)) {
            checkProcessor(p);
            return p.resolveText(sourceId, parentModelLink);
        }
        return sourceId;
    }

    public String getRoute(CompileProcessor p) {
        if (StringUtils.hasLink(sourceId)) {
            checkProcessor(p);
            return p.resolveUrlParams(route, parentModelLink);
        }
        return route;
    }

    @Override
    public Map<String, ModelLink> getQueryRouteMapping() {
        return queryRouteMapping;
    }

    public void setQueryRouteMapping(Map<String, ModelLink> queryRouteMapping) {
        if (queryRouteMapping != null)
            this.queryRouteMapping = Collections.unmodifiableMap(queryRouteMapping);
        else
            this.queryRouteMapping = null;
    }

    @Override
    public Map<String, ModelLink> getPathRouteMapping() {
        return pathRouteMapping;
    }

    public void setPathRouteMapping(Map<String, ModelLink> pathRouteMapping) {
        if (pathRouteMapping != null)
            this.pathRouteMapping = Collections.unmodifiableMap(pathRouteMapping);
        else
            this.pathRouteMapping = null;
    }

    public ModelLink getParentModelLink() {
        return parentModelLink;
    }

    public void setParentModelLink(ModelLink parentModelLink) {
        this.parentModelLink = parentModelLink;
    }

    @Override
    public Class<D> getCompiledClass() {
        return compiledClass;
    }

    @Override
    public Class<S> getSourceClass() {
        return sourceClass;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + sourceId + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseCompileContext)) return false;
        BaseCompileContext<?, ?> that = (BaseCompileContext<?, ?>) o;
        return this.sourceId.equals(that.sourceId) && this.compiledClass.equals(that.compiledClass);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(sourceId, compiledClass);
    }
}
