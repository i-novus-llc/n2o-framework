package net.n2oapp.framework.config.register.dynamic;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import net.n2oapp.framework.api.register.DynamicMetadataProviderFactory;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import net.n2oapp.framework.config.register.JavaInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * Считыватель динамических метаданных
 */
public class JavaSourceLoader implements SourceLoader<JavaInfo> {

    private DynamicMetadataProviderFactory providerFactory;
    private Consumer<SourceMetadata> cacheService;

    public JavaSourceLoader(DynamicMetadataProviderFactory providerFactory) {
        this.providerFactory = providerFactory;
    }

    public JavaSourceLoader(N2oDynamicMetadataProviderFactory providerFactory,
                            Consumer<SourceMetadata> cacheService) {
        this(providerFactory);
        this.cacheService = cacheService;
    }

    @Override
    public <S extends SourceMetadata> S load(JavaInfo info, String params) {
        return readContent(info.getId(), (Class<S>) info.getBaseSourceClass(), params);
    }


    @SuppressWarnings("unchecked")
    private <T extends SourceMetadata> T readContent(String id, Class<T> metadataClass, String params) {
        DynamicMetadataProvider provider = providerFactory.produce(id);

        List<SourceMetadata> metadataList = provider.read(params);

        //проверяем что id динамические
        metadataList.stream().filter(m -> m.getId() == null).forEach(m -> m.setId(provider.getCode() + "?" + params));
        DynamicUtil.checkDynamicIds(metadataList.stream().map(SourceMetadata::getId).collect(Collectors.toList()),
                provider.getCode());

        //кэшируем все что нашли
        if (cacheService != null && provider.cache(params))
            cache(metadataList);

        //ищем ту единственную
        Optional<? extends SourceMetadata> optional = metadataList.stream()
                .filter(m -> m.getId().equals(id + "?" + params))
                .filter(m -> metadataClass.isAssignableFrom(m.getClass()))
                .findAny();
        if (optional.isPresent())
            return (T) optional.get();
        else
            throw new ReferentialIntegrityViolationException(id, metadataClass);
    }


    protected void cache(List<SourceMetadata> metadataList) {
        for (SourceMetadata metadata : metadataList) {
            cacheService.accept(metadata);
        }
    }

    public void setProviderFactory(N2oDynamicMetadataProviderFactory providerFactory) {
        this.providerFactory = providerFactory;
    }

}
