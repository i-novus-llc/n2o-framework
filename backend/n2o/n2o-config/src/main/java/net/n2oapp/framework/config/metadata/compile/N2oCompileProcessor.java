package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.PlaceHoldersResolver;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.ExtensionAttributeMapperFactory;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.util.CompileUtil;
import org.apache.commons.collections.map.HashedMap;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.getParams;

/**
 * Реализация процессора сборки метаданных
 */
public class N2oCompileProcessor implements CompileProcessor, BindProcessor, ValidateProcessor {

    private static final PlaceHoldersResolver LINK_RESOLVER = new PlaceHoldersResolver("{", "}");
    private static final PlaceHoldersResolver URL_RESOLVER = new PlaceHoldersResolver(":", "", true);

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
        this.context = context;
        this.params = params;
        model = new DataModel();
        model.addAll(context.getQueryRouteMapping(), params);
        model.addAll(context.getPathRouteMapping(), params);
    }

    /**
     * Конструктор процессора сборки метаданных со связыванием и процессором вложенных моделей
     *
     * @param env                Окружение сборки метаданных
     * @param params             Параметры запроса
     * @param context            Входной контекст сборки(не используется для компиляции метаданных)
     * @param subModelsProcessor Процессор вложенных моделей
     */
    public N2oCompileProcessor(MetadataEnvironment env, CompileContext<?, ?> context, DataSet params,
                               SubModelsProcessor subModelsProcessor) {
        this(env);
        this.context = context;
        this.params = params;
        this.subModelsProcessor = subModelsProcessor;
        model = new DataModel();
        model.addAll(context.getQueryRouteMapping(), params);
        model.addAll(context.getPathRouteMapping(), params);
    }

    /**
     * Конструктор процессора внутренней сборки метаданных
     *
     * @param parent Родительский процессор сборки
     * @param scopes Метаданные, влияющие на сборку. Должны быть разных классов.
     */
    private N2oCompileProcessor(N2oCompileProcessor parent, Object... scopes) {
        this.env = parent.env;
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
        Object[] flattedScopes = Arrays.stream(scopes)
                .filter(Objects::nonNull)
                .flatMap(o -> o.getClass().isArray() ? Arrays.stream((Object[]) o) : Stream.of(o)).toArray();
        return compilePipeline.get(source, context, new N2oCompileProcessor(this, flattedScopes));
    }

    @Override
    public <D extends Compiled> void bind(D compiled) {
        if (compiled != null)
            bindPipeline.get(compiled, context, params, subModelsProcessor);
    }


    @Override
    public Map<String, Object> mapAttributes(ExtensionAttributesAware source) {
        if (source.getExtAttributes() == null || source.getExtAttributes().isEmpty())
            return null;
        ExtensionAttributeMapperFactory extensionAttributeMapperFactory = env.getExtensionAttributeMapperFactory();
        HashMap<String, Object> extAttributes = new HashMap<>();
        source.getExtAttributes().forEach((k, v) -> {
            Map<String, Object> res = extensionAttributeMapperFactory.mapAttributes(v, k.getUri());
            res = CompileUtil.resolveNestedAttributes(res, env.getDomainProcessor()::deserialize);
            if (!res.isEmpty()) {
                extAttributes.putAll(res);
            }
        });
        return extAttributes;
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
        Object value = resolveRequiredPlaceholder(placeholder);
        return (T) env.getDomainProcessor().deserialize(value, clazz);
    }

    @Override
    public Object resolve(String placeholder, String domain) {
        Object value = resolveRequiredPlaceholder(placeholder);
        return env.getDomainProcessor().deserialize(value, domain);
    }

    @Override
    public Object resolve(String placeholder) {
        Object value = resolvePlaceholder(placeholder);
        return env.getDomainProcessor().deserialize(value);
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
        Map<String, Object> resultParamsMap = new HashMap<>();
        if (params != null)
            params.forEach((k, v) -> resultParamsMap.put(k, params.get(k)));
        if (pathMappings != null) {
            removeCommonParams(pathMappings, resultParamsMap);
            resultUrl = URL_RESOLVER.resolve(resultUrl, k -> getValue(pathMappings, k));
        }
        if (queryMappings != null) {
            removeCommonParams(queryMappings, resultParamsMap);
            resultUrl = URL_RESOLVER.resolve(resultUrl, k -> getValue(queryMappings, k));
        }
        if (resultParamsMap.size() != 0) {
            DataSet resultParamsDataSet = new DataSet(resultParamsMap);
            resultUrl = URL_RESOLVER.resolve(resultUrl, resultParamsDataSet);
        } else {
            resultUrl = URL_RESOLVER.resolve(resultUrl, params);
        }
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
    public BindLink resolveLink(BindLink link, boolean observable) {
        if (link == null)
            return null;
        Optional<String> res = Optional.empty();
        if (context != null) {
            if (context.getQueryRouteMapping() != null) {
                res = context.getQueryRouteMapping().entrySet().stream().filter(kv -> kv.getValue().equalsLink(link)).map(Map.Entry::getKey).findAny();
            }
            if (res.isEmpty() && context.getPathRouteMapping() != null) {
                res = context.getPathRouteMapping().entrySet().stream().filter(kv -> kv.getValue().equalsLink(link)).map(Map.Entry::getKey).findAny();
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
                resultLink = new ModelLink((ModelLink)link);
                ((ModelLink)resultLink).setObserve(false);
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
            for (Object o : (List<?>)link.getValue()) {
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
    public <T extends Source> void validate(T metadata, Object... scope) {
        if (metadata == null)
            return;
        if (metadata instanceof RefIdAware && ((RefIdAware) metadata).getRefId() != null)
            return;

        env.getSourceValidatorFactory().validate(metadata, new N2oCompileProcessor(this, scope));
    }

    @Override
    public <T extends SourceMetadata> T getOrNull(String id, Class<T> metadataClass) {
        if (id == null)
            return null;
        if (!env.getMetadataRegister().contains(id, metadataClass))
            return null;
        try {
            return getSource(id, metadataClass);
        } catch (Exception e) {
            return null;
        }
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
            throw new N2oMetadataValidationException(getMessage(errorMessage, id));
    }

    @Override
    public void checkId(IdAware metadata, String errorMessage) {
        if (metadata == null || metadata.getId() == null)
            return;
        Pattern pattern = Pattern.compile(".*[а-яА-ЯёЁ].*");
        Matcher matcher = pattern.matcher(metadata.getId());
        if (matcher.find() || forbiddenIds.contains(metadata.getId())) {
            throw new N2oMetadataValidationException(getMessage(errorMessage, metadata.getId()));
        }
    }

    private Object resolvePlaceholder(String placeholder) {
        Object value = placeholder;
        if (StringUtils.isProperty(placeholder)) {
            value = env.getSystemProperties().resolvePlaceholders(placeholder);
        }
        return value;
    }

    private Object resolveRequiredPlaceholder(String placeholder) {
        if (StringUtils.isProperty(placeholder)) {
            return env.getSystemProperties().resolveRequiredPlaceholders(placeholder);
        } else
            return placeholder;
    }

    private void removeCommonParams(Map<String, ? extends BindLink> mapping, Map<String, Object> resultParams) {
        if (params != null)
            mapping.keySet().stream().filter(key -> params.get(key) != null).forEach(resultParams::remove);
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
}
