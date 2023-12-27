package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.PlaceHoldersResolver;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.util.CompileUtil;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.config.register.route.RouteUtil.getParams;

/**
 * Реализация процессора сборки метаданных
 */
public class N2oCompileProcessor implements CompileProcessor, BindProcessor, SourceProcessor {

    private static final PlaceHoldersResolver LINK_RESOLVER = new PlaceHoldersResolver("{", "}");
    private static final PlaceHoldersResolver URL_RESOLVER = new PlaceHoldersResolver(":", "", true);
    private static final Pattern FIELD_ID_PATTERN = Pattern.compile("[a-zA-Z_][\\w.\\[\\]*]*");

    /**
     * Сервисы окружения
     */
    private MetadataEnvironment env;
    /**
     * Процессор вложенных моделей
     */
    private SubModelsProcessor subModelsProcessor;
    /**
     * Переменные влияющие на сборку
     */
    private Map<Class<?>, Object> scope = Collections.emptyMap();
    /**
     * Контекст на входе в pipeline, используется не для компиляции, а для bind
     */
    private CompileContext<?, ?> context;
    /**
     * Параметры текущего запроса
     */
    private DataSet params;
    /**
     * Виртуальная модель данных клиента
     */
    private DataModel model;

    private CompileMode mode = CompileMode.READ;

    private BindTerminalPipeline bindPipeline;
    private CompileTerminalPipeline<?> compilePipeline;
    private ReadCompileTerminalPipeline<?> readCompilePipeline;
    private ReadTerminalPipeline<?> readPipeline;

    /**
     * Запрещенные имена идентификаторов
     */
    private Set<String> forbiddenIds;

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
        this.forbiddenIds = env.getSystemProperties().getProperty("n2o.config.field.forbidden_ids", Set.class);
    }

    /**
     * Конструктор процессора сборки метаданных со связыванием
     *
     * @param env     Окружение сборки метаданных
     * @param params  Параметры запроса
     * @param context Входной контекст сборки(не используется для компиляции метаданных)
     */
    public N2oCompileProcessor(MetadataEnvironment env, CompileContext<?, ?> context, DataSet params) {
        this(env);
        this.mode = CompileMode.COMPILE;
        this.context = context;
        this.params = params;
        model = new DataModel();
        model.addAll(context.getQueryRouteMapping(), params);
        model.addAll(context.getPathRouteMapping(), params);
    }

    /**
     * @param env     Окружение сборки метаданных
     * @param context Входной контекст сборки(не используется для компиляции метаданных)
     * @param params  Параметры запроса
     * @param scopes  Метаданные, влияющие на сборку. Должны быть разных классов.
     */
    public N2oCompileProcessor(MetadataEnvironment env, CompileContext<?, ?> context, DataSet params, Object... scopes) {
        this(env, context, params);
        Object[] flattedScopes = flatScopes(scopes);
        this.scope = new HashMap<>();
        Stream.of(Optional.ofNullable(flattedScopes).orElse(new Compiled[]{})).filter(Objects::nonNull)
                .forEach(s -> this.scope.put(s.getClass(), s));
    }

    /**
     * Конструктор процессора сборки метаданных со связыванием и процессором вложенных моделей
     *
     * @param env                Окружение сборки метаданных
     * @param params             Параметры запроса
     * @param context            Входной контекст сборки(не используется для компиляции метаданных)
     * @param subModelsProcessor Процессор вложенных моделей
     * @param scopes             Метаданные, влияющие на сборку. Должны быть разных классов.
     */
    public N2oCompileProcessor(MetadataEnvironment env, CompileContext<?, ?> context, DataSet params,
                               SubModelsProcessor subModelsProcessor, Object... scopes) {
        this(env);
        this.mode = CompileMode.BIND;
        this.context = context;
        this.params = params;
        this.subModelsProcessor = subModelsProcessor;
        model = new DataModel();
        model.addAll(context.getQueryRouteMapping(), params);
        model.addAll(context.getPathRouteMapping(), params);

        Object[] flattedScopes = flatScopes(scopes);
        this.scope = new HashMap<>();
        Stream.of(Optional.ofNullable(flattedScopes).orElse(new Compiled[]{})).filter(Objects::nonNull)
                .forEach(s -> this.scope.put(s.getClass(), s));
    }

    /**
     * Конструктор процессора внутренней сборки метаданных
     *
     * @param parent Родительский процессор сборки
     * @param scopes Метаданные, влияющие на сборку. Должны быть разных классов.
     */
    private N2oCompileProcessor(N2oCompileProcessor parent, Object... scopes) {
        this.env = parent.env;
        this.mode = parent.mode;
        this.scope = new HashMap<>(parent.scope);
        Stream.of(Optional.ofNullable(scopes).orElse(new Compiled[]{})).filter(Objects::nonNull)
                .forEach(s -> this.scope.put(s.getClass(), s));
        this.readPipeline = parent.readPipeline;
        this.readCompilePipeline = parent.readCompilePipeline;
        this.compilePipeline = parent.compilePipeline;
        this.params = parent.params;
        this.context = parent.context;
        this.forbiddenIds = parent.forbiddenIds;
    }

    @Override
    public <D extends Compiled, S> D compile(S source, CompileContext<?, ?> context, Object... scopes) {
        Object[] flattedScopes = flatScopes(scopes);
        return compilePipeline.get(source, context, new N2oCompileProcessor(this, flattedScopes));
    }

    @Override
    public <D extends Compiled> void bind(D compiled, Object... scopes) {
        Object[] flattedScopes = flatScopes(scopes);
        if (compiled != null)
            bindPipeline.get(compiled, context, params, subModelsProcessor, flattedScopes);
    }

    @Override
    public Map<String, Object> mapAttributes(ExtensionAttributesAware source) {
        if (source.getExtAttributes() == null || source.getExtAttributes().isEmpty())
            return null;
        ExtensionAttributeMapperFactory extensionAttributeMapperFactory = env.getExtensionAttributeMapperFactory();
        HashMap<String, Object> extAttributes = new HashMap<>();
        source.getExtAttributes().forEach((k, v) -> {
            Map<String, Object> res = extensionAttributeMapperFactory.mapAttributes(v, k.getUri(), this);
            res = CompileUtil.resolveNestedAttributes(res, env.getDomainProcessor()::deserialize);
            if (!res.isEmpty()) {
                extAttributes.putAll(res);
            }
        });
        return extAttributes;
    }

    @Override
    public Map<String, Object> mapAndResolveAttributes(ExtensionAttributesAware source) {
        if (source.getExtAttributes() == null || source.getExtAttributes().isEmpty())
            return null;
        Map<N2oNamespace, Map<String, String>> resolved = new HashMap<>();
        for (Map.Entry<N2oNamespace, Map<String, String>> entry : source.getExtAttributes().entrySet())
            resolved.put(entry.getKey(), entry.getValue().keySet().stream()
                    .collect(Collectors.toMap(k -> k, k -> this.resolveJS(entry.getValue().get(k)))));
        source.setExtAttributes(resolved);

        return mapAttributes(source);
    }

    @Override
    public <D extends Compiled> D getCompiled(CompileContext<D, ?> context) {
        return readCompilePipeline.get(context, new N2oCompileProcessor(this));
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
    public <S extends SourceMetadata> S getSource(String id, Class<S> sourceClass, CompileProcessor processor) {
        return readPipeline.get(id, sourceClass, new N2oCompileProcessor((N2oCompileProcessor) processor));
    }

    @Override
    public <D extends Compiled> void addRoute(CompileContext<D, ?> context) {
        env.getRouteRegister().addRoute(context.getRoute(this), context);
    }

    @Override
    public <D extends Compiled> void addRoute(String route, CompileContext<D, ?> context) {
        env.getRouteRegister().addRoute(route, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T resolve(String placeholder, Class<T> clazz) {
        Object value = resolveProperty(placeholder, true);
        value = resolveContext(value);
        return (T) env.getDomainProcessor().deserialize(value, clazz);
    }

    @Override
    public Object resolve(String placeholder, String domain) {
        Object value = resolveProperty(placeholder, true);
        value = resolveContext(value);
        return env.getDomainProcessor().deserialize(value, domain);
    }

    @Override
    public Object resolve(String placeholder) {
        Object value = resolveProperty(placeholder, false);
        value = resolveContext(value);
        return env.getDomainProcessor().deserialize(value);
    }

    @Override
    public Object resolve(Object value) {
        if (value instanceof String)
            return resolve((String) value);
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T resolve(Object value, Class<T> clazz) {
        if (value instanceof String)
            return resolve((String) value, clazz);
        return (T) value;
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
    public String getMessage(String messageCode, Object... arguments) {
        String defaultMessage = messageCode.contains("{0}") ? MessageFormat.format(messageCode, arguments) : messageCode;
        return env.getMessageSource().getMessage(messageCode, arguments, defaultMessage);
    }

    @Override
    public <S extends Source> S merge(S source, S override) {
        return env.getSourceMergerFactory().merge(source, override);
    }

    @Override
    public String getExternalFile(String fileUri) {
        return env.getExternalFilesLoader().getContentByUri(fileUri);
    }

    @Override
    public boolean canResolveParam(String param) {
        return params != null && params.containsKey(param);
    }

    @Override
    public Object resolveJS(String text, Class<?> clazz) {
        String value = ScriptProcessor.resolveLinks(text);
        return env.getDomainProcessor().deserialize(value, clazz);
    }

    @Override
    public String resolveUrl(String url) {
        return URL_RESOLVER.resolve(url, params);
    }

    @Override
    public String resolveUrl(String url,
                             Map<String, ? extends BindLink> pathMappings,
                             Map<String, ? extends BindLink> queryMappings) {
        String resultUrl = url;
        if (pathMappings != null)
            resultUrl = URL_RESOLVER.resolve(resultUrl, k -> getValue(pathMappings, k));
        if (queryMappings != null)
            resultUrl = URL_RESOLVER.resolve(resultUrl, k -> getValue(queryMappings, k));
        resultUrl = URL_RESOLVER.resolve(resultUrl, k -> ((pathMappings != null && pathMappings.containsKey(k))
                || (queryMappings != null && queryMappings.containsKey(k)) || params == null) ?
                null : params.get(k));
        return resultUrl;
    }

    @Override
    public String resolveUrl(String url, ModelLink link) {
        List<String> paramNames = getParams(url);
        if (paramNames.isEmpty() || params == null)
            return url;
        Map<String, String> valueParamMap = new HashMap<>();
        collectModelLinks(context.getPathRouteMapping(), link.getWidgetLink(), valueParamMap);
        collectModelLinks(context.getQueryRouteMapping(), link.getWidgetLink(), valueParamMap);
        for (String param : paramNames) {
            if (valueParamMap.containsKey(param) && params.containsKey(valueParamMap.get(param))) {
                url = url.replace(":" + param, params.get(valueParamMap.get(param)).toString());
            }
        }
        return url;
    }

    @Override
    public String resolveUrl(String url, List<ModelLink> links) {
        List<String> paramNames = getParams(url);
        if (paramNames.isEmpty() || params == null)
            return url;
        Map<String, String> valueParamMap = new HashMap<>();
        for (ModelLink link : links) {
            collectModelLinks(context.getPathRouteMapping(), link.getWidgetLink(), valueParamMap);
            collectModelLinks(context.getQueryRouteMapping(), link.getWidgetLink(), valueParamMap);
        }
        for (String param : paramNames) {
            if (valueParamMap.containsKey(param) && params.containsKey(valueParamMap.get(param))) {
                url = url.replace(":" + param, params.get(valueParamMap.get(param)).toString());
            }
        }
        return url;
    }

    @Override
    public BindLink resolveLink(BindLink link, boolean observable) {
        return resolveLink(link, observable, true);
    }

    @Override
    public BindLink resolveLink(BindLink link, boolean observable, boolean strongCompare) {
        if (link == null)
            return null;
        Optional<String> res = Optional.empty();
        if (context != null) {
            if (strongCompare) {
                res = getValueIfPossible(link, res, kv -> kv.getValue().equalsNormalizedLink(link));
            } else {
                res = getValueIfPossible(link, res, kv -> kv.getValue().equalsLink(link));
            }
        }
        Object value = null;
        if (res.isPresent()) {
            value = params.get(res.get());
        } else if (link instanceof ModelLink && ((ModelLink) link).getParam() != null) {
            if (observable || !((ModelLink) link).isObserve())
                value = params.get(((ModelLink) link).getParam());
        }
        if (value == null)
            return link;
        return createLink(link, value);
    }

    private Optional<String> getValueIfPossible(BindLink link, Optional<String> res,
                                                Predicate<Map.Entry<String, ModelLink>> filter) {
        if (context.getQueryRouteMapping() != null) {
            res = context.getQueryRouteMapping().entrySet().stream().filter(filter).map(Map.Entry::getKey).findAny();
        }
        if (res.isEmpty() && context.getPathRouteMapping() != null) {
            res = context.getPathRouteMapping().entrySet().stream().filter(filter).map(Map.Entry::getKey).findAny();
        }
        return res;
    }

    @Override
    public Object resolveLinkValue(ModelLink link) {
        return link.getParam() != null ? params.get(link.getParam()) : null;
    }

    private BindLink createLink(BindLink link, Object value) {
        if (value instanceof String)
            value = resolveText((String) value);
        if (value != null) {
            BindLink resultLink;
            if (link instanceof ModelLink) {
                resultLink = new ModelLink((ModelLink) link);
                ((ModelLink) resultLink).setObserve(false);
            } else
                resultLink = new BindLink(link.getBindLink());
            resultLink.setValue(value);
            return resultLink;
        }
        return null;
    }

    @Override
    public ModelLink resolveSubModels(ModelLink link) {
        if (link == null) return null;
        if (link.getSubModelQuery() == null) return link;
        if (link.getValue() == null) return link;
        if (link.getValue() instanceof Collection) {
            if (!link.getSubModelQuery().isMulti())
                throw new N2oException("Sub model [" + link.getSubModelQuery().getSubModel() + "] must be multi for value " + link.getValue());
            List<DataSet> dataList = new ArrayList<>();
            for (Object o : (List<?>) link.getValue()) {
                if (o instanceof DefaultValues) {
                    dataList.add(new DataSet(((DefaultValues) o).getValues()));
                } else {
                    dataList.add(new DataSet("id", o));
                }
            }
            DataSet dataSet = new DataSet(link.getSubModelQuery().getSubModel(), dataList);
            if (subModelsProcessor != null)
                subModelsProcessor.executeSubModels(Collections.singletonList(link.getSubModelQuery()), dataSet);
            ModelLink resolvedLink = link.getSubModelLink();
            List<DataSet> valueList = (List<DataSet>) dataSet.get(link.getSubModelQuery().getSubModel());
            resolvedLink.setValue(valueList.stream().map(DefaultValues::new).collect(Collectors.toList()));
            return resolvedLink;
        } else if (link.getValue() instanceof DefaultValues) {
            if (link.getSubModelQuery().isMulti())
                throw new N2oException("Sub model [" + link.getSubModelQuery().getSubModel() + "] must not be multi for value " + link.getValue());
            DataSet dataSet = new DataSet();
            dataSet.put(link.getSubModelQuery().getSubModel(), ((DefaultValues) link.getValue()).getValues());
            if (subModelsProcessor != null)
                subModelsProcessor.executeSubModels(Collections.singletonList(link.getSubModelQuery()), dataSet);
            ModelLink resolvedLink = link.getSubModelLink();
            resolvedLink.setValue(new DefaultValues((Map<String, Object>) dataSet.get(link.getSubModelQuery().getSubModel())));
            return resolvedLink;
        } else {
            DataSet dataSet = new DataSet();
            if (link.getSubModelQuery().isMulti()) {
                ArrayList<DataSet> list = new ArrayList<>();
                list.add(new DataSet("id", link.getValue()));
                dataSet.put(link.getSubModelQuery().getSubModel(), list);
            } else {
                dataSet.put(link.getSubModelQuery().getSubModel() + ".id", link.getValue());
            }
            if (subModelsProcessor != null)
                subModelsProcessor.executeSubModels(Collections.singletonList(link.getSubModelQuery()), dataSet);
            ModelLink resolvedLink = link.getSubModelLink();
            if (link.getSubModelQuery().isMulti()) {
                List<DataSet> valueList = (List<DataSet>) dataSet.get(link.getSubModelQuery().getSubModel());
                resolvedLink.setValue(valueList.stream().map(DefaultValues::new).collect(Collectors.toList()));
            } else {
                resolvedLink.setValue(new DefaultValues((Map<String, Object>) dataSet.get(link.getSubModelQuery().getSubModel())));
            }
            return resolvedLink;
        }
    }

    @Override
    public DataSet executeQuery(String queryId) {
        if (subModelsProcessor == null) return null;

        return ((List<DataSet>) subModelsProcessor.getQueryResult(queryId, params)
                .getCollection()).get(0);
    }

    @Override
    public String resolveText(String text, ModelLink link) {
        String resolved = resolveText(text);
        if (link != null)
            return LINK_RESOLVER.resolve(resolved, model.getDataIfAbsent(link, subModelsProcessor));
        else
            return resolved;
    }

    @Override
    public String resolveText(String text, List<ModelLink> links) {
        String resolved = resolveText(text);
        if (links != null && !links.isEmpty()) {
            for (ModelLink link : links)
                resolved = LINK_RESOLVER.resolve(resolved, model.getDataIfAbsent(link, subModelsProcessor));
        }
        return resolved;
    }

    @Override
    public <T extends Source> void validate(T metadata, Object... scope) {
        if (metadata == null)
            return;
        if (metadata instanceof RefIdAware && ((RefIdAware) metadata).getRefId() != null)
            return;

        env.getSourceValidatorFactory().validate(metadata, new N2oCompileProcessor(this, scope));
    }

    @Override
    public <T extends SourceMetadata> T getOrThrow(String id, Class<T> metadataClass) {
        if (id == null)
            return null;
        if (!env.getMetadataRegister().contains(id, metadataClass))
            return null;
        return getSource(id, metadataClass);
    }

    @Override
    public <T extends SourceMetadata> void checkForExists(String id, Class<T> metadataClass, String errorMessage) {
        if (id == null)
            return;
        if (StringUtils.hasWildcard(id) || StringUtils.hasLink(id))
            return;
        if (!env.getMetadataRegister().contains(id, metadataClass))
            throw new N2oMetadataValidationException(errorMessage);
    }

    @Override
    public void checkId(IdAware metadata, String errorMessage) {
        if (metadata == null || metadata.getId() == null)
            return;
        Matcher matcher = FIELD_ID_PATTERN.matcher(metadata.getId());
        if (!matcher.matches() || forbiddenIds.contains(metadata.getId())) {
            throw new N2oMetadataValidationException(String.format(errorMessage, metadata.getId()));
        }
    }

    private Object resolveProperty(Object placeholder, boolean strong) {
        if (!(placeholder instanceof String))
            return placeholder;
        Object value = placeholder;
        if (StringUtils.isProperty((String) placeholder)) {
            if (strong)
                value = env.getSystemProperties().resolveRequiredPlaceholders((String) placeholder);
            else
                value = env.getSystemProperties().resolvePlaceholders((String) placeholder);
        }
        return value;
    }

    private Object resolveContext(Object placeholder) {
        if (!(placeholder instanceof String))
            return placeholder;
        Object value = placeholder;
        if (isBinding() && StringUtils.isContext((String) placeholder)) {
            value = env.getContextProcessor().resolve(placeholder);
        }
        return value;
    }

    private void collectModelLinks(Map<String, ModelLink> linkMap, ModelLink link, Map<String, String> resultMap) {
        if (linkMap != null) {
            linkMap.forEach((k, v) -> {
                if (v.equalsLink(link)) {
                    // для данных, которые мапятся напрямую
                    resultMap.put(k, k);//todo это нужно для resolve url нужно вынести в другой метод
                    // для данных, которые мапятся через параметр
                    resultMap.put(v.getFieldId(), k);
                }
            });
        }
    }

    /**
     * Получает значение по ключу и если оно существует, удаляет этот ключ из маппинга
     *
     * @param mapping Маппинг
     * @param key     Ключ
     * @return Значение
     */
    private Object getValue(Map<String, ? extends BindLink> mapping, String key) {
        if (!mapping.containsKey(key))
            return null;
        BindLink bindLink = mapping.get(key);
        if (bindLink instanceof ModelLink) {
            Object value = model.getValue((ModelLink) bindLink);
            if (value != null)
                mapping.remove(key);
            return value;
        } else
            return null;
    }

    private Object[] flatScopes(Object[] scopes) {
        if (scopes != null && Stream.of(scopes).filter(Objects::nonNull).anyMatch(o -> o.getClass().isArray()))
            return flatScopes(Stream.of(scopes)
                    .filter(Objects::nonNull)
                    .flatMap(o -> o.getClass().isArray() ? Arrays.stream((Object[]) o) : Stream.of(o))
                    .filter(Objects::nonNull).toArray());
        else
            return scopes;
    }

    private boolean isBinding() {
        return mode.equals(CompileMode.BIND);
    }

    enum CompileMode {
        READ, COMPILE, BIND
    }
}
