package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.ExtensionAttributeMapperFactory;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;

import java.util.*;
import java.util.stream.Stream;

import static net.n2oapp.framework.config.register.route.RouteUtil.getParams;

/**
 * Реализация процессора сборки метаданных
 */
public class N2oCompileProcessor implements CompileProcessor {

    private MetadataEnvironment env;
    private Map<Class<?>, Object> scope = Collections.emptyMap();
    private DataSet data;
    private BindTerminalPipeline bindPipeline;
    private CompileTerminalPipeline<?> compilePipeline;
    private ReadCompileTerminalPipeline<?> readCompilePipeline;
    private ReadTerminalPipeline<?> readPipeline;
    /**
     * Контекст на входе в pipeline, используется не для компиляции, а для bind
     */
    private CompileContext<?, ?> context;

    /**
     * Конструктор процессора сборки метаданных
     *
     * @param env Окружение сборки метаданных
     */
    public N2oCompileProcessor(MetadataEnvironment env) {
        this.env = env;
        PipelineSupport pipelineSupport = new N2oPipelineSupport(env);
        this.bindPipeline = env.getBindPipelineFunction().apply(pipelineSupport);
        this.readPipeline = env.getReadPipelineFunction().apply(pipelineSupport);
        this.readCompilePipeline = env.getReadCompilePipelineFunction().apply(pipelineSupport);
        this.compilePipeline = env.getCompilePipelineFunction().apply(pipelineSupport);
    }

    /**
     * Конструктор процессора сборки метаданных со связыванием
     *
     * @param env     Окружение сборки метаданных
     * @param data    Данные для связывания
     * @param context Входной контекст сборки(не используется для компиляции метаданных)
     */
    public N2oCompileProcessor(MetadataEnvironment env, CompileContext<?, ?> context, DataSet data) {
        this(env);
        this.data = data;
        this.context = context;
    }

    /**
     * Конструктор процессора внутренней сборки метаданных
     *
     * @param parent Родительский процессор сборки
     * @param scope  Метаданные, влияющие на сборку. Должны быть разных классов.
     */
    private N2oCompileProcessor(N2oCompileProcessor parent, Object... scope) {
        this.env = parent.env;
        this.scope = new HashMap<>(parent.scope);
        Stream.of(Optional.ofNullable(scope).orElse(new Compiled[]{})).filter(Objects::nonNull)
                .forEach(s -> this.scope.put(s.getClass(), s));
        this.readPipeline = parent.readPipeline;
        this.readCompilePipeline = parent.readCompilePipeline;
        this.compilePipeline = parent.compilePipeline;
        this.data = parent.data;
        this.context = parent.context;
    }

    @Override
    public <D extends Compiled, S> D compile(S source, CompileContext<?, ?> context, Object... scope) {
        return compilePipeline.get(source, context, new N2oCompileProcessor(this, scope));
    }

    @Override
    public <D extends Compiled> void bind(D compiled) {
        bindPipeline.get(compiled, context, data);
    }

    @Override
    public Map<String, Object> mapAttributes(ExtensionAttributesAware source) {
        if (source.getExtAttributes() == null)
            return null;
        ExtensionAttributeMapperFactory extensionAttributeMapperFactory = env.getExtensionAttributeMapperFactory();
        HashMap<String, Object> extAttributes = new HashMap<>();
        source.getExtAttributes().forEach((k, v) -> {
            Map<String, Object> res = extensionAttributeMapperFactory.mapAttributes(v, k.getUri());
            if (res != null) {
                extAttributes.putAll(res);
            }
        });
        return extAttributes;
    }

    @Override
    public <D extends Compiled> D getCompiled(CompileContext<D, ?> context) {
        return readCompilePipeline.get(context);
    }

    @Override
    public <D> D getScope(Class<D> scopeClass) {
        return (D) scope.get(scopeClass);
    }

    @Override
    public <S extends SourceMetadata> S getSource(String id, Class<S> sourceClass) {
        return readPipeline.get(id, sourceClass);
    }

    @Override
    public <D extends Compiled> void addRoute(String urlPattern, CompileContext<D, ?> context) {
        env.getRouteRegister().addRoute(new RouteInfo(urlPattern, context));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T resolve(String placeholder, Class<T> clazz) {
        Object value = placeholder;
        if (StringUtils.isProperty(placeholder)) {
            value = env.getSystemProperties().resolvePlaceholders(placeholder);
        }
        if (StringUtils.isContext(placeholder)) {
            value = env.getContextProcessor().resolve(placeholder);
        }
        return (T) env.getDomainProcessor().deserialize(value, clazz);
    }

    @Override
    public Object resolve(String value, String domain) {
        return env.getDomainProcessor().doDomainConversion(domain, value);
    }

    @Override
    public Object resolve(String value) {
        return env.getDomainProcessor().doDomainConversion(null, value);
    }

    @Override
    public String resolveText(String text) {
        if (StringUtils.hasProperty(text))
            return env.getSystemProperties().resolvePlaceholders(text);
        if (StringUtils.hasContext(text))
            return env.getContextProcessor().resolveText(text);
        return text;
    }

    @Override
    public String resolveParams(String text) {
        return StringUtils.resolveLinks(text, data);
    }

    @Override
    public String resolveUrl(String url, Map<String, ? extends BindLink> pathMappings, Map<String, ? extends BindLink> queryMappings) {
        Set<String> params = new HashSet<>(getParams(url));
        Set<String> paramsForRemove = new HashSet<>();
        Set<String> except = new HashSet<>();
        if (pathMappings != null) {
            if (context.getPathRouteInfos() != null) {
                pathMappings.keySet().stream().filter(k -> !context.getPathRouteInfos().containsKey(k))
                        .forEach(k -> except.add(k));
            } else {
                except.addAll(pathMappings.keySet());
            }
        }
        if (queryMappings != null) {
            if (context.getQueryRouteInfos() != null) {
                queryMappings.keySet().stream().filter(k -> !context.getQueryRouteInfos().containsKey(k))
                        .forEach(k -> except.add(k));
            } else {
                except.addAll(queryMappings.keySet());
            }
        }
        for (String param : params) {
            if (!except.contains(param)) {
                Object value = data.get(param);
                if (value != null) {
                    url = url.replace(":" + param, value.toString());
                    paramsForRemove.add(param);
                }
            }
        }
        if (pathMappings != null) {
            paramsForRemove.forEach(k -> {
                pathMappings.remove(k);
            });
        }
        if (queryMappings != null) {
            paramsForRemove.forEach(k -> {
                queryMappings.remove(k);
            });
        }
        return url;
    }

    @Override
    public String resolveUrlParams(String url, Set<String> params) {
        for (String param : params) {
            Object value = data.get(param);
            if (value != null) {
                url = url.replace(":" + param, value.toString());
            }
        }
        return url;
    }


    @Override
    public ModelLink resolveLink(ModelLink link) {
        if (link == null || link.getBindLink() == null || context == null || context.getQueryRouteInfos() == null)
            return link;
        Optional<String> res = Optional.empty();
        if (context.getQueryRouteInfos() != null) {
            res = context.getQueryRouteInfos().keySet().stream().filter(ri -> context.getQueryRouteInfos().get(ri).equals(link)).findAny();
        }
        if (!res.isPresent() && context.getPathRouteInfos() != null) {
            res = context.getPathRouteInfos().keySet().stream().filter(ri -> context.getPathRouteInfos().get(ri).equals(link)).findAny();
        }
        if (res.isPresent()) {
            Object param = data.get(res.get());
            if (param != null) {
                return new ModelLink(param);
            }
        }
        return link;
    }

    @Override
    public String getMessage(String messageCode, Object... arguments) {
        return env.getMessageSource().getMessage(messageCode, arguments);
    }

    @Override
    public Object resolveJS(String text, Class<?> clazz) {
        String value = ScriptProcessor.resolveLinks(text);
        return env.getDomainProcessor().deserialize(value, clazz);
    }
}
