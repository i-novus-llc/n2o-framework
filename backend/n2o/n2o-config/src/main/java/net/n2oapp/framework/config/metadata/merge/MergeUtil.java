package net.n2oapp.framework.config.metadata.merge;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.reader.IncorrectMergeTypeException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MergeUtil {

    /**
     * Слияние метаданных через ref-id
     *
     * @param m           Элемент с ref-id
     * @param environment Окружение сборки
     * @param elementName Имя тега элемента
     */
    public static void merge(RefIdAware m, MetadataEnvironment environment, String elementName) {
        if (m.getRefId() == null)
            return;

        var readPipeline = new N2oPipelineSupport(environment).read();
        SourceMetadata refSource = readPipeline.get(m.getRefId(), m.getClass());
        refSource.setId(null);
        if (!refSource.getClass().isAssignableFrom(m.getClass()))
            throw new IncorrectMergeTypeException(m.getRefId(), elementName);
        environment.getSourceMergerFactory().merge(refSource, m);
    }
}
